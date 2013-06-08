// Copyright 2013 Structure Eng Inc.

package com.structureeng.persistence.dao.impl.business;

import static com.google.common.base.Preconditions.checkNotNull;

import com.structureeng.persistence.dao.DAOException;
import com.structureeng.persistence.dao.QueryContainer;
import com.structureeng.persistence.dao.TaxCategoryDAO;
import com.structureeng.persistence.dao.impl.CatalogDAOImpl;
import com.structureeng.persistence.dao.impl.product.ProductErrorCode;
import com.structureeng.persistence.model.business.TaxCategory;
import com.structureeng.persistence.model.product.ProductOrganization;
import com.structureeng.persistence.model.product.ProductOrganization_;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.LockModeType;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.JoinType;

/**
 * Data Access Object for {@code TaxType}.
 *
 * @author Edgar Rico (edgar.martinez.rico@gmail.com)
 */
public final class TaxCategoryDAOImpl implements TaxCategoryDAO {

    private EntityManager entityManager;
    private final CatalogDAOImpl<TaxCategory, Long, String> catalogDAO;
    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Inject
    public TaxCategoryDAOImpl() {
        catalogDAO = new
                CatalogDAOImpl<TaxCategory, Long, String>(TaxCategory.class, Long.class, 
                    String.class);
    }

    @Override
    public void persist(TaxCategory entity) throws DAOException {
        catalogDAO.persist(entity);
    }

    @Override
    public TaxCategory merge(TaxCategory entity) throws DAOException {
        return catalogDAO.merge(entity);
    }

    @Override
    public void remove(TaxCategory entity) throws DAOException {
        if (entity.getActive()) {
            long count = countProductWithTaxCategory(entity);
            if (count > 0) {
                throw DAOException.Builder.build(ProductErrorCode.TAX_TYPE_FK);
            }
        }
        catalogDAO.remove(entity);
    }

    @Override
    public void refresh(TaxCategory entity) {
        catalogDAO.refresh(entity);
    }

    @Override
    public void refresh(TaxCategory entity, LockModeType modeType) {
        catalogDAO.refresh(entity, modeType);
    }

    @Override
    public TaxCategory find(Long id) {
        return catalogDAO.find(id);
    }

    @Override
    public TaxCategory find(Long id, long version) throws DAOException {
        return catalogDAO.find(id, version);
    }

    @Override
    public TaxCategory findByReferenceId(String referenceId) {
        return catalogDAO.findByReferenceId(referenceId);
    }

    @Override
    public TaxCategory findInactiveByReferenceId(String referenceId) {
        return catalogDAO.findInactiveByReferenceId(referenceId);
    }

    @Override
    public List<TaxCategory> findRange(int start, int count) {
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
    public boolean hasVersionChanged(TaxCategory entity) throws DAOException {
        return catalogDAO.hasVersionChanged(entity);
    }

    private long countProductWithTaxCategory(TaxCategory taxCategory) {
        QueryContainer<Long, ProductOrganization> qc =
                QueryContainer.newQueryContainerCount(getEntityManager(), 
                    ProductOrganization.class);
        qc.getCriteriaQuery().select(qc.getCriteriaBuilder().countDistinct(qc.getRoot()));
        qc.getRoot().join(ProductOrganization_.taxCategory, JoinType.INNER);
        qc.getCriteriaQuery().where(qc.getCriteriaBuilder().and(
                qc.getCriteriaBuilder().equal(qc.getRoot()
                    .get(ProductOrganization_.taxCategory), taxCategory),
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
