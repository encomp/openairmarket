// Copyright 2013 Structure Eng Inc.

package com.structureeng.persistence.dao.impl.product;

import static com.google.common.base.Preconditions.checkNotNull;

import com.structureeng.persistence.dao.DAOException;
import com.structureeng.persistence.dao.ProductDAO;
import com.structureeng.persistence.dao.QueryContainer;
import com.structureeng.persistence.dao.impl.CatalogDAOImpl;
import com.structureeng.persistence.model.product.Product;
import com.structureeng.persistence.model.product.Product_;
import com.structureeng.persistence.model.product.ProductPrice;
import com.structureeng.persistence.model.product.ProductPrice_;
import com.structureeng.persistence.model.stock.Stock;
import com.structureeng.persistence.model.stock.Stock_;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigInteger;
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
public final class ProductDAOImpl implements ProductDAO {

    private EntityManager entityManager;
    private final CatalogDAOImpl<Product, Long, BigInteger> catalogDAO;
    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Inject
    public ProductDAOImpl() {
        catalogDAO = new CatalogDAOImpl<Product, Long, BigInteger>(Product.class, Long.class,
                BigInteger.class);
    }

    @Override
    public void persist(Product entity) throws DAOException {
        long count = countProductstWithSameDefinitionAndType(entity);
        if (count > 0) {
            throw DAOException.Builder.build(ProductErrorCode.PRODUCT_TYPE_UK);
        }
        catalogDAO.persist(entity);
    }

    @Override
    public Product merge(Product entity) throws DAOException {
        long count = countProductstWithSameDefinitionAndTypeButDiffId(entity);
        if (count > 0) {
            throw DAOException.Builder.build(ProductErrorCode.PRODUCT_TYPE_UK);
        }
        return catalogDAO.merge(entity);
    }

    @Override
    public void remove(Product entity) throws DAOException {
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
    public void refresh(Product entity) {
        catalogDAO.refresh(entity);
    }

    @Override
    public void refresh(Product entity, LockModeType modeType) {
        catalogDAO.refresh(entity, modeType);
    }

    @Override
    public Product find(Long id) {
        return catalogDAO.find(id);
    }

    @Override
    public Product find(Long id, long version) throws DAOException {
        return catalogDAO.find(id, version);
    }

    @Override
    public Product findByReferenceId(BigInteger referenceId) {
        return catalogDAO.findByReferenceId(referenceId);
    }

    @Override
    public Product findInactiveByReferenceId(BigInteger referenceId) {
        return catalogDAO.findInactiveByReferenceId(referenceId);
    }

    @Override
    public List<Product> findRange(int start, int count) {
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
    public boolean hasVersionChanged(Product entity) throws DAOException {
        return catalogDAO.hasVersionChanged(entity);
    }

    private long countProductstWithSameDefinitionAndType(Product product) {
        QueryContainer<Long, Product> qc = QueryContainer.newQueryContainerCount(
                getEntityManager(), Product.class);
        qc.getCriteriaQuery().where(qc.getCriteriaBuilder().and(
                qc.getCriteriaBuilder()
                    .equal(qc.getRoot().get(Product_.organization), product.getOrganization()),
                qc.getCriteriaBuilder()
                    .equal(qc.getRoot().get(Product_.productDefinition),
                        product.getProductDefinition()),
                qc.getCriteriaBuilder()
                    .equal(qc.getRoot().get(Product_.productType), product.getProductType())));
        return qc.getSingleResult();
    }

    private long countProductstWithSameDefinitionAndTypeButDiffId(Product product) {
        QueryContainer<Long, Product> qc = QueryContainer.newQueryContainerCount(
                getEntityManager(), Product.class);
        qc.getCriteriaQuery().where(qc.getCriteriaBuilder().and(
                qc.getCriteriaBuilder()
                    .equal(qc.getRoot().get(Product_.organization), product.getOrganization()),
                qc.getCriteriaBuilder()
                    .equal(qc.getRoot().get(Product_.productType), product.getProductType()),
                qc.getCriteriaBuilder()
                    .notEqual(qc.getRoot().get(Product_.productDefinition),
                        product.getProductDefinition())));
        return qc.getSingleResult();
    }

    private long countProductPricesForProduct(final Product product) {
        QueryContainer<Long, ProductPrice> qc = QueryContainer.newQueryContainerCount(
                getEntityManager(), ProductPrice.class);
        qc.getRoot().join(ProductPrice_.product, JoinType.INNER);
        qc.getCriteriaQuery().where(qc.getCriteriaBuilder().and(
                qc.getCriteriaBuilder()
                .equal(qc.getRoot().get(ProductPrice_.product), product),
                qc.activeEntities(qc.getRoot())));
        return qc.getSingleResult();
    }

    private long countStocksWithProduct(final Product product) {
        QueryContainer<Long, Stock> qc = QueryContainer.newQueryContainerCount(getEntityManager(),
                Stock.class);
        qc.getRoot().join(Stock_.product, JoinType.INNER);
        qc.getCriteriaQuery().where(qc.getCriteriaBuilder().and(
                qc.getCriteriaBuilder()
                .equal(qc.getRoot().get(Stock_.product), product),
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
