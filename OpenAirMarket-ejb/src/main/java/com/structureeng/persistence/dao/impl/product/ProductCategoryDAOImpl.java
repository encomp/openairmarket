// Copyright 2013 Structure Eng Inc.

package com.structureeng.persistence.dao.impl.product;

import com.structureeng.persistence.dao.DAOException;
import com.structureeng.persistence.dao.ProductCategoryDAO;
import com.structureeng.persistence.dao.QueryContainer;
import com.structureeng.persistence.dao.impl.CatalogDAOImpl;
import com.structureeng.persistence.model.product.ProductCategory;
import com.structureeng.persistence.model.product.ProductDefinition;
import com.structureeng.persistence.model.product.ProductDefinition_;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.LockModeType;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.JoinType;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Data Access Object for {@code ProductCategory}.
 *
 * @author Edgar Rico (edgar.martinez.rico@gmail.com)
 */
public final class ProductCategoryDAOImpl implements ProductCategoryDAO {

    private EntityManager entityManager;
    private final CatalogDAOImpl<ProductCategory, Long, Integer> catalogDAO;
    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Inject
    public ProductCategoryDAOImpl() {
        catalogDAO = new
                CatalogDAOImpl<ProductCategory, Long, Integer>(ProductCategory.class, Long.class,
                    Integer.class);
    }

    @Override
    public void persist(ProductCategory entity) throws DAOException {
        catalogDAO.persist(entity);
    }

    @Override
    public ProductCategory merge(ProductCategory entity) throws DAOException {
        return catalogDAO.merge(entity);
    }

    @Override
    public void remove(ProductCategory entity) throws DAOException {
        if (entity.getActive()) {
            long count = countProductDefinitionWithCategory(entity);
            if (count > 0) {
                throw DAOException.Builder.build(ProductErrorCode.PRODUCT_CATEGORY_FK);
            }
        }
        catalogDAO.remove(entity);
    }

    @Override
    public void refresh(ProductCategory entity) {
        catalogDAO.refresh(entity);
    }

    @Override
    public void refresh(ProductCategory entity, LockModeType modeType) {
        catalogDAO.refresh(entity, modeType);
    }

    @Override
    public ProductCategory find(Long id) {
        return catalogDAO.find(id);
    }

    @Override
    public ProductCategory find(Long id, long version) throws DAOException {
        return catalogDAO.find(id, version);
    }

    @Override
    public ProductCategory findByReferenceId(Integer referenceId) {
        return catalogDAO.findByReferenceId(referenceId);
    }

    @Override
    public ProductCategory findInactiveByReferenceId(Integer referenceId) {
        return catalogDAO.findInactiveByReferenceId(referenceId);
    }

    @Override
    public List<ProductCategory> findRange(int start, int count) {
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
    public boolean hasVersionChanged(ProductCategory entity) throws DAOException {
        return catalogDAO.hasVersionChanged(entity);
    }

    private long countProductDefinitionWithCategory(ProductCategory division) {
        QueryContainer<Long, ProductDefinition> qc =
                QueryContainer.newQueryContainerCount(getEntityManager(), ProductDefinition.class);
        qc.getCriteriaQuery().select(qc.getCriteriaBuilder().countDistinct(qc.getRoot()));
        qc.getRoot().join(ProductDefinition_.productCategory, JoinType.INNER);
        qc.getCriteriaQuery().where(qc.getCriteriaBuilder().and(
                qc.getCriteriaBuilder().equal(
                    qc.getRoot().get(ProductDefinition_.productCategory), division),
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
