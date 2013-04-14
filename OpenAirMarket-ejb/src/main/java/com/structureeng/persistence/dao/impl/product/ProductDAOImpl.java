// Copyright 2013 Structure Eng Inc.

package com.structureeng.persistence.dao.impl.product;

import com.structureeng.persistence.dao.DAOException;
import com.structureeng.persistence.dao.ProductDAO;
import com.structureeng.persistence.dao.impl.CatalogDAOImpl;
import com.structureeng.persistence.model.product.Product;
import com.structureeng.persistence.model.product.Product_;
import com.structureeng.persistence.model.product.ProductDefinition_;
import com.structureeng.persistence.model.product.RetailProduct;
import com.structureeng.persistence.model.product.RetailProduct_;
import com.structureeng.persistence.model.stock.Stock;
import com.structureeng.persistence.model.stock.Stock_;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigInteger;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.JoinType;

/**
 * Data Access Object for {@code Product}.
 *
 * @author Edgar Rico (edgar.martinez.rico@gmail.com)
 */
public class ProductDAOImpl extends CatalogDAOImpl<Product, Long, BigInteger> implements
        ProductDAO {

    private final Logger logger = LoggerFactory.getLogger(getClass());
    @PersistenceContext
    private EntityManager entityManager;

    @Inject
    public ProductDAOImpl() {
        super(Product.class, Long.class, BigInteger.class);
    }
    
    @Override
    protected DAOException validateUniqueKeysForPersistEvent(final Product entity) {        
        long count = countProductstWithSameDefinitionAndType(entity);
        if (count > 0) {
            return DAOException.Builder.build(ProductErrorCode.PRODUCT_TYPE_UK);
        }
        return null;
    }
    
    @Override
    protected DAOException validateUniqueKeysForMergeEvent(final Product entity) {
        long count = countProductstWithSameDefinitionAndTypeButDiffId(entity);
        if (count > 0) {
            return DAOException.Builder.build(ProductErrorCode.PRODUCT_TYPE_UK);
        }
        return null;
    }

    @Override
    protected void validateForeignKeys(final Product entity) throws DAOException {
        if (entity.getActive()) {
            DAOException daoException = null;
            if (countRetailProductsWithProduct(entity) > 0) {
                daoException = DAOException.Builder.build(ProductErrorCode.PRODUCT_FK_RETAIL);
            }
            if (countStocksWithProduct(entity) > 0) {
                daoException = DAOException.Builder.build(ProductErrorCode.PRODUCT_FK_STOCK, 
                        daoException);
            }
            if (daoException != null) {
                throw daoException;
            }
        }
    }
    
    private long countProductstWithSameDefinitionAndType(Product product) {
        QueryContainer<Long, Product> qc = newQueryContainerCount();
        qc.getCriteriaQuery().where(qc.getCriteriaBuilder().and(                
                qc.getCriteriaBuilder()
                    .equal(qc.getRoot().get(Product_.productDefinition), 
                        product.getProductDefinition()),
                qc.getCriteriaBuilder()
                    .equal(qc.getRoot().get(Product_.productType), product.getProductType())));
        return qc.getSingleResult();
    }
    
    private long countProductstWithSameDefinitionAndTypeButDiffId(Product product) {
        QueryContainer<Long, Product> qc = newQueryContainerCount();
        qc.getCriteriaQuery().where(qc.getCriteriaBuilder().and(                
                qc.getCriteriaBuilder()
                    .equal(qc.getRoot().get(Product_.productType), product.getProductType()),
                qc.getCriteriaBuilder()
                    .notEqual(qc.getRoot().get(Product_.productDefinition), 
                        product.getProductDefinition())));
        return qc.getSingleResult();
    }

    private long countRetailProductsWithProduct(final Product product) {
        QueryContainer<Long, RetailProduct> qc = newQueryContainerCount(RetailProduct.class);
        qc.getRoot().join(RetailProduct_.product, JoinType.INNER);
        qc.getCriteriaQuery().where(qc.getCriteriaBuilder().and(
                qc.getCriteriaBuilder()
                .equal(qc.getRoot().get(RetailProduct_.product), product),
                qc.getCriteriaBuilder()
                .equal(qc.getRoot().get(ProductDefinition_.active), Boolean.TRUE)));
        return qc.getSingleResult();
    }

    private long countStocksWithProduct(final Product product) {
        QueryContainer<Long, Stock> qc = newQueryContainerCount(Stock.class);
        qc.getRoot().join(Stock_.product, JoinType.INNER);
        qc.getCriteriaQuery().where(qc.getCriteriaBuilder().and(
                qc.getCriteriaBuilder()
                .equal(qc.getRoot().get(Stock_.product), product),
                qc.getCriteriaBuilder()
                .equal(qc.getRoot().get(ProductDefinition_.active), Boolean.TRUE)));
        return qc.getSingleResult();
    }

    @Override
    protected EntityManager getEntityManager() {
        return entityManager;
    }

    @Override
    public Logger getLogger() {
        return logger;
    }
}
