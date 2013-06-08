// Copyright 2013 Structure Eng Inc.

package com.structureeng.persistence.dao.impl.product;

import static com.google.common.base.Preconditions.checkNotNull;

import com.structureeng.persistence.dao.DAOException;
import com.structureeng.persistence.dao.ProductDAO;
import com.structureeng.persistence.dao.QueryContainer;
import com.structureeng.persistence.dao.impl.CatalogDAOImpl;
import com.structureeng.persistence.model.product.ProductOrganization;
import com.structureeng.persistence.model.product.Product;
import com.structureeng.persistence.model.product.ProductOrganization_;
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
public final class ProductDAOImpl implements ProductDAO {

    private EntityManager entityManager;
    private final CatalogDAOImpl<Product, Long, String> catalogDAO;
    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Inject
    public ProductDAOImpl() {
        catalogDAO = new CatalogDAOImpl<Product, Long, String>(Product.class, Long.class, 
                String.class);
    }

    @Override
    public void persist(Product entity) throws DAOException {
        long count = countEntitiesWithKey(entity);
        if (count > 0) {
            throw DAOException.Builder.build(ProductErrorCode.PRODUCT_DEFFINITION_KEY_UK);
        }
        catalogDAO.persist(entity);
    }

    @Override
    public Product merge(Product entity) throws DAOException {
        long count = countEntitiesWithSameKeyButDiffReferenceId(entity);
        if (count > 0) {
            throw DAOException.Builder.build(ProductErrorCode.PRODUCT_DEFFINITION_KEY_UK);
        }
        return catalogDAO.merge(entity);
    }

    @Override
    public void remove(Product entity) throws DAOException {
        if (entity.getActive()) {
            long count = countProductOrganizationsWithProduct(entity);
            if (count > 0) {
                throw DAOException.Builder.build(ProductErrorCode.PRODUCT_DEFFINITION_FK);
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
    public Product findByReferenceId(String referenceId) {
        return catalogDAO.findByReferenceId(referenceId);
    }

    @Override
    public Product findInactiveByReferenceId(String referenceId) {
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

    private long countEntitiesWithKey(Product productDefinition) {
        QueryContainer<Long, Product> qc =
                QueryContainer.newQueryContainerCount(getEntityManager(), Product.class);
        qc.getCriteriaQuery().where(qc.getCriteriaBuilder().equal(
                        qc.getRoot().get(Product_.referenceId), 
                        productDefinition.getReferenceId()));
        return qc.getSingleResult();
    }

    private long countEntitiesWithSameKeyButDiffReferenceId(Product entity) {
        QueryContainer<Long, Product> qc =
                QueryContainer.newQueryContainerCount(getEntityManager(), Product.class);
        qc.getCriteriaQuery().where(qc.getCriteriaBuilder().and(
                qc.getCriteriaBuilder().equal(qc.getRoot().get(Product_.referenceId),
                    entity.getName()),
                qc.getCriteriaBuilder().notEqual(qc.getRoot().get(Product_.referenceId),
                    entity.getReferenceId())));
        return qc.getSingleResult();
    }

    private long countProductOrganizationsWithProduct(final Product entity) {
        QueryContainer<Long, ProductOrganization> qc =
                QueryContainer.newQueryContainerCount(getEntityManager(), ProductOrganization.class);
        qc.getCriteriaQuery().select(qc.getCriteriaBuilder().countDistinct(qc.getRoot()));
        qc.getRoot().join(ProductOrganization_.product, JoinType.INNER);
        qc.getCriteriaQuery().where(qc.getCriteriaBuilder().and(
                qc.getCriteriaBuilder()
                    .equal(qc.getRoot().get(ProductOrganization_.product), entity),
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
