// Copyright 2013 Structure Eng Inc.

package com.structureeng.persistence.dao.impl.product;

import com.structureeng.persistence.dao.DAOException;
import com.structureeng.persistence.dao.ProductDefinitionDAO;
import com.structureeng.persistence.dao.impl.CatalogDAOImpl;
import com.structureeng.persistence.model.product.Product;
import com.structureeng.persistence.model.product.ProductDefinition;
import com.structureeng.persistence.model.product.ProductDefinition_;
import com.structureeng.persistence.model.product.Product_;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.math.BigInteger;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.JoinType;

/**
 * Data Access Object for {@code ProductDefinition}.
 *
 * @author Edgar Rico (edgar.martinez.rico@gmail.com)
 */
public class ProductDefinitionDAOImpl extends CatalogDAOImpl<ProductDefinition, Long, BigInteger>
    implements ProductDefinitionDAO {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    @PersistenceContext
    private EntityManager entityManager;

    @Inject
    public ProductDefinitionDAOImpl() {
        super(ProductDefinition.class, Long.class, BigInteger.class);
    }

    @Override
    protected DAOException validateUniqueKeysForPersistEvent(final ProductDefinition entity) {        
        long count = countEntitiesWithKey(entity);
        if (count > 0) {
            return DAOException.Builder.build(ProductErrorCode.PRODUCT_DEFFINITION_KEY_UK);
        }
        return null;
    }

    @Override
    protected DAOException validateUniqueKeysForMergeEvent(final ProductDefinition entity) {
        long count = countEntitiesWithSameKeyButDiffReferenceId(entity);
        if (count > 0) {
            return DAOException.Builder.build(ProductErrorCode.PRODUCT_DEFFINITION_KEY_UK);
        }
        return null;
    }

    @Override
    protected void validateForeignKeys(ProductDefinition productDefinition) throws DAOException {
        if (productDefinition.getActive()) {
            long count = countProductWithProductDefinition(productDefinition);
            if (count > 0) {
                throw DAOException.Builder.build(ProductErrorCode.PRODUCT_DEFFINITION_FK);
            }
        }
    }

    private long countEntitiesWithKey(ProductDefinition productDefinition) {
        QueryContainer<Long, ProductDefinition> qc = newQueryContainerCount();
        qc.getCriteriaQuery().where(qc.getCriteriaBuilder().equal(
                        qc.getRoot().get(ProductDefinition_.key), productDefinition.getKey()));
        return qc.getSingleResult();
    }

    private long countEntitiesWithSameKeyButDiffReferenceId(ProductDefinition entity) {
        QueryContainer<Long, ProductDefinition> qc = newQueryContainerCount();
        qc.getCriteriaQuery().where(qc.getCriteriaBuilder().and(
                qc.getCriteriaBuilder().equal(qc.getRoot().get(ProductDefinition_.key), 
                    entity.getName()),
                qc.getCriteriaBuilder().notEqual(qc.getRoot().get(ProductDefinition_.referenceId), 
                    entity.getReferenceId())));
        return qc.getSingleResult();
    }

    private long countProductWithProductDefinition(final ProductDefinition entity) {
        QueryContainer<Long, Product> qc = 
                new QueryContainer<Long, Product>(Long.class, Product.class);
        qc.getCriteriaQuery().select(qc.getCriteriaBuilder().countDistinct(qc.getRoot()));
        qc.getRoot().join(Product_.productDefinition, JoinType.INNER);
        qc.getCriteriaQuery().where(qc.getCriteriaBuilder().and(
                qc.getCriteriaBuilder()
                    .equal(qc.getRoot().get(Product_.productDefinition), entity),
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
