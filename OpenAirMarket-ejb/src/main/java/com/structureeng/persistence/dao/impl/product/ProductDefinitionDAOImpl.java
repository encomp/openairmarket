// Copyright 2013 Structure Eng Inc.

package com.structureeng.persistence.dao.impl.product;

import static com.google.common.base.Preconditions.checkNotNull;

import com.structureeng.persistence.dao.DAOException;
import com.structureeng.persistence.dao.ProductDefinitionDAO;
import com.structureeng.persistence.dao.QueryContainer;
import com.structureeng.persistence.dao.impl.CatalogDAOImpl;
import com.structureeng.persistence.model.product.Product;
import com.structureeng.persistence.model.product.ProductDefinition;
import com.structureeng.persistence.model.product.ProductDefinition_;
import com.structureeng.persistence.model.product.Product_;

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
 * Data Access Object for {@code ProductDefinition}.
 *
 * @author Edgar Rico (edgar.martinez.rico@gmail.com)
 */
public final class ProductDefinitionDAOImpl implements ProductDefinitionDAO {

    private EntityManager entityManager;
    private final CatalogDAOImpl<ProductDefinition, Long, BigInteger> catalogDAO;
    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Inject
    public ProductDefinitionDAOImpl() {
        catalogDAO =
                new CatalogDAOImpl<ProductDefinition, Long, BigInteger>(ProductDefinition.class,
                    Long.class, BigInteger.class);
    }

    @Override
    public void persist(ProductDefinition entity) throws DAOException {
        long count = countEntitiesWithKey(entity);
        if (count > 0) {
            throw DAOException.Builder.build(ProductErrorCode.PRODUCT_DEFFINITION_KEY_UK);
        }
        catalogDAO.persist(entity);
    }

    @Override
    public ProductDefinition merge(ProductDefinition entity) throws DAOException {
        long count = countEntitiesWithSameKeyButDiffReferenceId(entity);
        if (count > 0) {
            throw DAOException.Builder.build(ProductErrorCode.PRODUCT_DEFFINITION_KEY_UK);
        }
        return catalogDAO.merge(entity);
    }

    @Override
    public void remove(ProductDefinition entity) throws DAOException {
        if (entity.getActive()) {
            long count = countProductWithProductDefinition(entity);
            if (count > 0) {
                throw DAOException.Builder.build(ProductErrorCode.PRODUCT_DEFFINITION_FK);
            }
        }
        catalogDAO.remove(entity);
    }

    @Override
    public void refresh(ProductDefinition entity) {
        catalogDAO.refresh(entity);
    }

    @Override
    public void refresh(ProductDefinition entity, LockModeType modeType) {
        catalogDAO.refresh(entity, modeType);
    }

    @Override
    public ProductDefinition find(Long id) {
        return catalogDAO.find(id);
    }

    @Override
    public ProductDefinition find(Long id, long version) throws DAOException {
        return catalogDAO.find(id, version);
    }

    @Override
    public ProductDefinition findByReferenceId(BigInteger referenceId) {
        return catalogDAO.findByReferenceId(referenceId);
    }

    @Override
    public ProductDefinition findInactiveByReferenceId(BigInteger referenceId) {
        return catalogDAO.findInactiveByReferenceId(referenceId);
    }

    @Override
    public List<ProductDefinition> findRange(int start, int count) {
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
    public boolean hasVersionChanged(ProductDefinition entity) throws DAOException {
        return catalogDAO.hasVersionChanged(entity);
    }

    private long countEntitiesWithKey(ProductDefinition productDefinition) {
        QueryContainer<Long, ProductDefinition> qc =
                QueryContainer.newQueryContainerCount(getEntityManager(), ProductDefinition.class);
        qc.getCriteriaQuery().where(qc.getCriteriaBuilder().equal(
                        qc.getRoot().get(ProductDefinition_.key), productDefinition.getKey()));
        return qc.getSingleResult();
    }

    private long countEntitiesWithSameKeyButDiffReferenceId(ProductDefinition entity) {
        QueryContainer<Long, ProductDefinition> qc =
                QueryContainer.newQueryContainerCount(getEntityManager(), ProductDefinition.class);
        qc.getCriteriaQuery().where(qc.getCriteriaBuilder().and(
                qc.getCriteriaBuilder().equal(qc.getRoot().get(ProductDefinition_.key),
                    entity.getName()),
                qc.getCriteriaBuilder().notEqual(qc.getRoot().get(ProductDefinition_.referenceId),
                    entity.getReferenceId())));
        return qc.getSingleResult();
    }

    private long countProductWithProductDefinition(final ProductDefinition entity) {
        QueryContainer<Long, Product> qc =
                QueryContainer.newQueryContainerCount(getEntityManager(), Product.class);
        qc.getCriteriaQuery().select(qc.getCriteriaBuilder().countDistinct(qc.getRoot()));
        qc.getRoot().join(Product_.productDefinition, JoinType.INNER);
        qc.getCriteriaQuery().where(qc.getCriteriaBuilder().and(
                qc.getCriteriaBuilder()
                    .equal(qc.getRoot().get(Product_.productDefinition), entity),
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
