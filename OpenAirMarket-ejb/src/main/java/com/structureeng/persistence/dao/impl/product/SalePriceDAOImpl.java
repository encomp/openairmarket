// Copyright 2013 Structure Eng Inc.

package com.structureeng.persistence.dao.impl.product;

import static com.google.common.base.Preconditions.checkNotNull;

import com.structureeng.persistence.dao.DAOException;
import com.structureeng.persistence.dao.QueryContainer;
import com.structureeng.persistence.dao.SalePriceDAO;
import com.structureeng.persistence.dao.impl.ActiveDAOImpl;
import com.structureeng.persistence.model.product.ProductOrganization;
import com.structureeng.persistence.model.product.SalePrice;
import com.structureeng.persistence.model.product.SalePrice_;
import com.structureeng.persistence.model.sale.SaleDetail;
import com.structureeng.persistence.model.sale.SaleDetail_;

import com.google.common.collect.ImmutableList;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.LockModeType;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.JoinType;
import javax.persistence.criteria.Predicate;

/**
 * Data Access Object for {@code SalePrice}.
 *
 * @author Edgar Rico (edgar.martinez.rico@gmail.com)
 */
public class SalePriceDAOImpl implements SalePriceDAO {

    private EntityManager entityManager;
    private final ActiveDAOImpl<SalePrice, Long> activeDAO;
    private final Logger logger = LoggerFactory.getLogger(getClass());

    public SalePriceDAOImpl() {
        this.activeDAO = new ActiveDAOImpl<SalePrice, Long>(SalePrice.class, Long.class);
    }

    @Override
    public void persist(SalePrice entity) throws DAOException {
        validateUK(entity);
        activeDAO.persist(entity);
    }

    @Override
    public SalePrice merge(SalePrice entity) throws DAOException {
        validateUK(entity);
        return activeDAO.merge(entity);
    }

    @Override
    public void remove(SalePrice entity) throws DAOException {
        long count = countSaleDetailsWithSalePrice(entity);
        if (count > 0) {
            throw DAOException.Builder.build(ProductErrorCode.PRODUCT_SALE_PRICE_FK);
        }
        activeDAO.remove(entity);
    }

    @Override
    public void refresh(SalePrice entity) {
        activeDAO.refresh(entity);
    }

    @Override
    public void refresh(SalePrice entity, LockModeType modeType) {
        activeDAO.refresh(entity, modeType);
    }

    @Override
    public SalePrice find(Long id) {
        return activeDAO.find(id);
    }

    @Override
    public SalePrice find(Long id, long version) throws DAOException {
        return activeDAO.find(id, version);
    }

    @Override
    public List<SalePrice> find(ProductOrganization product) {
        QueryContainer<SalePrice, SalePrice> query =
                QueryContainer.newQueryContainer(getEntityManager(), SalePrice.class);
        query.getRoot().fetch(SalePrice_.product, JoinType.INNER);
        query.getCriteriaQuery().where(
                query.getCriteriaBuilder()
                .equal(query.getRoot().get(SalePrice_.product), product),
                query.activeEntities(query.getRoot()));
        return query.getResultList();
    }

    @Override
    public SalePrice find(ProductOrganization product, BigDecimal quantity) {
        return find(product, quantity, true);
    }

    @Override
    public List<SalePrice> findRange(int start, int count) {
        return activeDAO.findRange(start, count);
    }

    @Override
    public SalePrice findInactive(ProductOrganization product, BigDecimal quantity) {
        return find(product, quantity, false);
    }

    private SalePrice find(ProductOrganization product, BigDecimal quantity, boolean active) {
        QueryContainer<SalePrice, SalePrice> query =
                QueryContainer.newQueryContainer(getEntityManager(), SalePrice.class);
        query.getRoot().fetch(SalePrice_.product, JoinType.INNER);
        query.getCriteriaQuery().where(query.getCriteriaBuilder().and(
                query.getCriteriaBuilder()
                .equal(query.getRoot().get(SalePrice_.product), product),                
                active
                ? query.activeEntities(query.getRoot()) : query.inactiveEntities(query.getRoot())));
        try {
            return query.getSingleResult();
        } catch (NoResultException exc) {
            logger.warn(String.format(
                    "Could not find an [%s] Sale Price for Product [%d] with quantity [%d].",
                    active ? "ACTIVE" : "INACTIVE", product.getId(), quantity));
            return null;
        }
    }

    @Override
    public long count() {
        return activeDAO.count();
    }

    @Override
    public long countInactive() {
        return activeDAO.countInactive();
    }

    @Override
    public void flush() {
        activeDAO.flush();
    }

    @Override
    public boolean hasVersionChanged(SalePrice entity) throws DAOException {
        return activeDAO.hasVersionChanged(entity);
    }

    private void validateUK(SalePrice salePrice) throws DAOException {
        long count = countSalePrices(salePrice);
        if (count > 0) {
            throw DAOException.Builder.build(ProductErrorCode.PRODUCT_SALE_PRICE_KEY_UK);
        }
    }

    private Long countSalePrices(final SalePrice salePrice) {
        QueryContainer<Long, SalePrice> qc =
                QueryContainer.newQueryContainerCount(getEntityManager(), SalePrice.class);
        qc.getRoot().join(SalePrice_.product, JoinType.INNER);
        ImmutableList.Builder<Predicate> builder = ImmutableList.builder();
        builder.add(qc.getCriteriaBuilder()
                .equal(qc.getRoot().get(SalePrice_.product), salePrice.getProduct()));        
        if (salePrice.getId() != null) {
            builder.add(qc.getCriteriaBuilder()
                .notEqual(qc.getRoot().get(SalePrice_.id), salePrice.getId()));
        }
        qc.getCriteriaQuery().where(
                qc.getCriteriaBuilder().and(builder.build().toArray(new Predicate[]{})));
        return qc.getSingleResult();
    }

    private long countSaleDetailsWithSalePrice(final SalePrice salePrice) {
        QueryContainer<Long, SaleDetail> qc =
                QueryContainer.newQueryContainerCount(getEntityManager(), SaleDetail.class);
        qc.getRoot().join(SaleDetail_.productPrice, JoinType.INNER);
        qc.getCriteriaQuery().where(qc.getCriteriaBuilder().and(
                qc.getCriteriaBuilder()
                .equal(qc.getRoot().get(SaleDetail_.productPrice), salePrice),
                qc.activeEntities(qc.getRoot())));
        return qc.getSingleResult();
    }

    @PersistenceContext
    public void setEntityManager(EntityManager entityManager) {
        this.entityManager = checkNotNull(entityManager);
        activeDAO.setEntityManager(entityManager);
    }

    /**
     * Provides the {@code EntityManager} that is being use by the dao.
     *
     * @return - the instance
     */
    public EntityManager getEntityManager() {
        return entityManager;
    }

    /**
     * Provides the {@code Logger} of the concrete class.
     *
     * @return - the logger instance of the class.
     */
    public Logger getLogger() {
        return logger;
    }
}
