// Copyright 2013 Structure Eng Inc.

package com.structureeng.persistence.dao.impl.product;

import static com.google.common.base.Preconditions.checkNotNull;

import com.structureeng.persistence.dao.DAOException;
import com.structureeng.persistence.dao.PurchasePriceDAO;
import com.structureeng.persistence.dao.QueryContainer;
import com.structureeng.persistence.dao.impl.ActiveDAOImpl;
import com.structureeng.persistence.model.product.ProductOrganization;
import com.structureeng.persistence.model.product.PurchasePrice;
import com.structureeng.persistence.model.product.PurchasePrice_;

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
 * Data Access Object for {@code PurchasePrice}.
 *
 * @author Edgar Rico (edgar.martinez.rico@gmail.com)
 */
public class PurchasePriceDAOImpl implements PurchasePriceDAO {

    private EntityManager entityManager;
    private final ActiveDAOImpl<PurchasePrice, Long> activeDAO;
    private final Logger logger = LoggerFactory.getLogger(getClass());

    public PurchasePriceDAOImpl() {
        this.activeDAO = new ActiveDAOImpl<PurchasePrice, Long>(PurchasePrice.class, Long.class);
    }

    @Override
    public void persist(PurchasePrice entity) throws DAOException {
        validateUK(entity);
        activeDAO.persist(entity);
    }

    @Override
    public PurchasePrice merge(PurchasePrice entity) throws DAOException {
        validateUK(entity);
        return activeDAO.merge(entity);
    }

    @Override
    public void remove(PurchasePrice entity) throws DAOException {
        activeDAO.remove(entity);
    }

    @Override
    public void refresh(PurchasePrice entity) {
        activeDAO.refresh(entity);
    }

    @Override
    public void refresh(PurchasePrice entity, LockModeType modeType) {
        activeDAO.refresh(entity, modeType);
    }

    @Override
    public PurchasePrice find(Long id) {
        return activeDAO.find(id);
    }

    @Override
    public PurchasePrice find(Long id, long version) throws DAOException {
        return activeDAO.find(id, version);
    }

    @Override
    public List<PurchasePrice> find(ProductOrganization product) {
        QueryContainer<PurchasePrice, PurchasePrice> query =
                QueryContainer.newQueryContainer(getEntityManager(), PurchasePrice.class);
        query.getRoot().fetch(PurchasePrice_.product, JoinType.INNER);
        query.getCriteriaQuery().where(
                query.getCriteriaBuilder()
                        .equal(query.getRoot().get(PurchasePrice_.product), product),
                query.activeEntities(query.getRoot()));
        return query.getResultList();
    }

    @Override
    public PurchasePrice find(ProductOrganization product, BigDecimal quantity) {
        return find(product, quantity, true);
    }

    @Override
    public PurchasePrice findInactive(ProductOrganization product, BigDecimal quantity) {
        return find(product, quantity, false);
    }

    @Override
    public List<PurchasePrice> findRange(int start, int count) {
        return activeDAO.findRange(start, count);
    }

    private PurchasePrice find(ProductOrganization product, BigDecimal quantity, boolean active) {
        QueryContainer<PurchasePrice, PurchasePrice> query =
                QueryContainer.newQueryContainer(getEntityManager(), PurchasePrice.class);
        query.getRoot().fetch(PurchasePrice_.product, JoinType.INNER);
        query.getCriteriaQuery().where(query.getCriteriaBuilder().and(
                query.getCriteriaBuilder()
                        .equal(query.getRoot().get(PurchasePrice_.product), product),                
                active
                        ? query.activeEntities(query.getRoot())
                        : query.inactiveEntities(query.getRoot())));
        try {
            return query.getSingleResult();
        } catch (NoResultException exc) {
            logger.warn(String.format(
                    "Could not find an [%s] Purchase Price for Product [%d] with quantity [%d].",
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
    public boolean hasVersionChanged(PurchasePrice entity) throws DAOException {
        return activeDAO.hasVersionChanged(entity);
    }

    private void validateUK(PurchasePrice purchasePrice) throws DAOException {
        long count = countPurchasePrices(purchasePrice);
        if (count > 0) {
            throw DAOException.Builder.build(ProductErrorCode.PRODUCT_PURCHASE_PRICE_KEY_UK);
        }
    }

    private Long countPurchasePrices(final PurchasePrice purchasePrice) {
        QueryContainer<Long, PurchasePrice> qc =
                QueryContainer.newQueryContainerCount(getEntityManager(), PurchasePrice.class);
        qc.getRoot().join(PurchasePrice_.product, JoinType.INNER);
        ImmutableList.Builder<Predicate> builder = ImmutableList.builder();
        builder.add(qc.getCriteriaBuilder()
                .equal(qc.getRoot().get(PurchasePrice_.product), purchasePrice.getProduct()));        
        if (purchasePrice.getId() != null) {
            builder.add(qc.getCriteriaBuilder()
                    .notEqual(qc.getRoot().get(PurchasePrice_.id), purchasePrice.getId()));
        }
        qc.getCriteriaQuery().where(
                qc.getCriteriaBuilder().and(builder.build().toArray(new Predicate[]{})));
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
