// Copyright 2013 Structure Eng Inc.

package com.structureeng.persistence.dao.impl.product;

import static com.google.common.base.Preconditions.checkNotNull;

import com.structureeng.persistence.dao.DAOException;
import com.structureeng.persistence.dao.ProductOrganizationDAO;
import com.structureeng.persistence.dao.QueryContainer;
import com.structureeng.persistence.dao.impl.ActiveDAOImpl;
import com.structureeng.persistence.model.product.ProductOrganization;
import com.structureeng.persistence.model.product.ProductOrganization_;
import com.structureeng.persistence.model.product.ProductPrice;
import com.structureeng.persistence.model.product.ProductPrice_;
import com.structureeng.persistence.model.stock.Stock;
import com.structureeng.persistence.model.stock.Stock_;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.LockModeType;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.JoinType;

/**
 * Data Access Object for {@code Product}.
 *
 * @author Edgar Rico (edgar.martinez.rico@gmail.com)
 */
public final class ProductOrganizationDAOImpl implements ProductOrganizationDAO {

    private EntityManager entityManager;
    private final ActiveDAOImpl<ProductOrganization, Long> catalogDAO;
    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Inject
    public ProductOrganizationDAOImpl() {
        catalogDAO = new ActiveDAOImpl<ProductOrganization, Long>(ProductOrganization.class,
                Long.class);
    }

    @Override
    public void persist(ProductOrganization entity) throws DAOException {
        long count = countProductstWithSameOrganization(entity);
        if (count > 0) {
            throw DAOException.Builder.build(ProductErrorCode.PRODUCT_TYPE_UK);
        }
        catalogDAO.persist(entity);
    }

    @Override
    public ProductOrganization merge(ProductOrganization entity) throws DAOException {
        long count = countProductstWithSameOrganizationButDiffId(entity);
        if (count > 0) {
            throw DAOException.Builder.build(ProductErrorCode.PRODUCT_TYPE_UK);
        }
        return catalogDAO.merge(entity);
    }

    @Override
    public void remove(ProductOrganization entity) throws DAOException {
        if (entity.getActive()) {
            DAOException daoException = null;
            if (countProductPricesForProduct(entity) > 0) {
                daoException = DAOException.Builder.build(ProductErrorCode.PRODUCT_PRICES_FK);
            }
            if (countStocksWithProduct(entity) > 0) {
                daoException = DAOException.Builder.build(ProductErrorCode.PRODUCT_FK_STOCK,
                        daoException);
            }
            if (daoException != null) {
                throw daoException;
            }
        }
        catalogDAO.remove(entity);
    }

    @Override
    public void refresh(ProductOrganization entity) {
        catalogDAO.refresh(entity);
    }

    @Override
    public void refresh(ProductOrganization entity, LockModeType modeType) {
        catalogDAO.refresh(entity, modeType);
    }

    @Override
    public ProductOrganization find(Long id) {
        return catalogDAO.find(id);
    }

    @Override
    public ProductOrganization find(Long id, long version) throws DAOException {
        return catalogDAO.find(id, version);
    }

    @Override
    public List<ProductOrganization> findRange(int start, int count) {
        return catalogDAO.findRange(start, count);
    }

    @Override
    public long count() {
        return catalogDAO.count();
    }

    @Override
    public long countInactive() {
        return catalogDAO.countInactive();
    }

    @Override
    public void flush() {
        catalogDAO.flush();
    }

    @Override
    public boolean hasVersionChanged(ProductOrganization entity) throws DAOException {
        return catalogDAO.hasVersionChanged(entity);
    }

    private long countProductstWithSameOrganization(ProductOrganization productOrganization) {
        QueryContainer<Long, ProductOrganization> qc = QueryContainer.newQueryContainerCount(
                getEntityManager(), ProductOrganization.class);
        qc.getCriteriaQuery().where(qc.getCriteriaBuilder().and(
                qc.getCriteriaBuilder()
                    .equal(qc.getRoot().get(ProductOrganization_.organization),
                    productOrganization.getOrganization()),
                qc.getCriteriaBuilder()
                    .equal(qc.getRoot().get(ProductOrganization_.product),
                        productOrganization.getProduct())));
        return qc.getSingleResult();
    }

    private long countProductstWithSameOrganizationButDiffId(
            ProductOrganization productOrganization) {
        QueryContainer<Long, ProductOrganization> qc = QueryContainer.newQueryContainerCount(
                getEntityManager(), ProductOrganization.class);
        qc.getCriteriaQuery().where(qc.getCriteriaBuilder().and(
                qc.getCriteriaBuilder()
                    .equal(qc.getRoot().get(ProductOrganization_.organization),
                        productOrganization.getOrganization()),
                qc.getCriteriaBuilder()
                    .notEqual(qc.getRoot().get(ProductOrganization_.product),
                        productOrganization.getProduct())));
        return qc.getSingleResult();
    }

    private long countProductPricesForProduct(final ProductOrganization product) {
        QueryContainer<Long, ProductPrice> qc = QueryContainer.newQueryContainerCount(
                getEntityManager(), ProductPrice.class);
        qc.getRoot().join(ProductPrice_.product, JoinType.INNER);
        qc.getCriteriaQuery().where(qc.getCriteriaBuilder().and(
                qc.getCriteriaBuilder()
                .equal(qc.getRoot().get(ProductPrice_.product), product),
                qc.activeEntities(qc.getRoot())));
        return qc.getSingleResult();
    }

    private long countStocksWithProduct(final ProductOrganization product) {
        QueryContainer<Long, Stock> qc = QueryContainer.newQueryContainerCount(getEntityManager(),
                Stock.class);
        qc.getRoot().join(Stock_.productOrganization, JoinType.INNER);
        qc.getCriteriaQuery().where(qc.getCriteriaBuilder().and(
                qc.getCriteriaBuilder()
                .equal(qc.getRoot().get(Stock_.productOrganization), product),
                qc.activeEntities(qc.getRoot())));
        return qc.getSingleResult();
    }

    @PersistenceContext
    public void setEntityManager(EntityManager entityManager) {
        this.entityManager = checkNotNull(entityManager);
        catalogDAO.setEntityManager(entityManager);
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
