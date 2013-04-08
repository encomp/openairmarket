// Copyright 2013 Structure Eng Inc.

package com.structureeng.persistence.dao.impl.business;

import com.structureeng.persistence.dao.DAOException;
import com.structureeng.persistence.dao.ProductTypeDAO;
import com.structureeng.persistence.dao.impl.CatalogDAOImpl;
import com.structureeng.persistence.dao.impl.product.ProductErrorCode;
import com.structureeng.persistence.model.business.ProductType;
import com.structureeng.persistence.model.product.Product;
import com.structureeng.persistence.model.product.ProductDefinition_;
import com.structureeng.persistence.model.product.Product_;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.JoinType;

/**
 * Data Access Object for {@code ProductType}.
 *
 * @author Edgar Rico (edgar.martinez.rico@gmail.com)
 */
public class ProductTypeDAOImpl extends CatalogDAOImpl<ProductType, Long, Integer> implements
        ProductTypeDAO {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    @PersistenceContext
    private EntityManager entityManager;

    @Inject
    public ProductTypeDAOImpl() {
        super(ProductType.class, Long.class, Integer.class);
    }

    @Override
    protected void validateForeignKeys(ProductType entity) throws DAOException {
        if (entity.getActive()) {
            long count = countProductWithProductType(entity);
            if (count > 0) {
                throw DAOException.Builder.build(ProductErrorCode.PRODUCT_TYPE_FK);
            }
        }
    }

    private long countProductWithProductType(ProductType productType) {
        QueryContainer<Long, Product> qc =
                new QueryContainer<Long, Product>(Long.class, Product.class);
        qc.getCriteriaQuery().select(qc.getCriteriaBuilder().countDistinct(qc.getRoot()));
        qc.getRoot().join(Product_.productType, JoinType.INNER);
        qc.getCriteriaQuery().where(qc.getCriteriaBuilder().and(
                qc.getCriteriaBuilder().equal(qc.getRoot().get(Product_.productType), productType),
                qc.getCriteriaBuilder().equal(
                        qc.getRoot().get(ProductDefinition_.active), Boolean.TRUE)));
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
