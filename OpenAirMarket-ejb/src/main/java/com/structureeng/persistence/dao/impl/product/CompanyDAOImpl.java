// Copyright 2013 Structure Eng Inc.

package com.structureeng.persistence.dao.impl.product;

import static com.google.common.base.Preconditions.checkNotNull;

import com.structureeng.persistence.dao.CompanyDAO;
import com.structureeng.persistence.dao.DAOException;
import com.structureeng.persistence.dao.QueryContainer;
import com.structureeng.persistence.dao.impl.CatalogDAOImpl;
import com.structureeng.persistence.model.product.Company;
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

/**
 * Data Access Object for {@code Company}.
 *
 * @author Edgar Rico (edgar.martinez.rico@gmail.com)
 */
public final class CompanyDAOImpl implements CompanyDAO {

    private EntityManager entityManager;
    private final CatalogDAOImpl<Company, Long, Integer> catalogDAO;
    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Inject
    public CompanyDAOImpl() {
        catalogDAO = new CatalogDAOImpl<Company, Long, Integer>(Company.class, Long.class,
                Integer.class);
    }

    @Override
    public void persist(Company entity) throws DAOException {
        catalogDAO.persist(entity);
    }

    @Override
    public Company merge(Company entity) throws DAOException {
        return catalogDAO.merge(entity);
    }

    @Override
    public void remove(Company entity) throws DAOException {
        if (entity.getActive()) {
            long count = countProductDefinitionWithCompany(entity);
            if (count > 0) {
                throw DAOException.Builder.build(ProductErrorCode.COMPANY_FK);
            }
        }
        catalogDAO.remove(entity);
    }

    @Override
    public void refresh(Company entity) {
        catalogDAO.refresh(entity);
    }

    @Override
    public void refresh(Company entity, LockModeType modeType) {
        catalogDAO.refresh(entity, modeType);
    }

    @Override
    public Company find(Long id) {
        return catalogDAO.find(id);
    }

    @Override
    public Company find(Long id, long version) throws DAOException {
        return catalogDAO.find(id, version);
    }

    @Override
    public Company findByReferenceId(Integer referenceId) {
        return catalogDAO.findByReferenceId(referenceId);
    }

    @Override
    public Company findInactiveByReferenceId(Integer referenceId) {
        return catalogDAO.findInactiveByReferenceId(referenceId);
    }

    @Override
    public List<Company> findRange(int start, int count) {
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
    public boolean hasVersionChanged(Company entity) throws DAOException {
        return catalogDAO.hasVersionChanged(entity);
    }

    private long countProductDefinitionWithCompany(final Company company) {
        QueryContainer<Long, ProductDefinition> qc =
                QueryContainer.newQueryContainerCount(getEntityManager(), ProductDefinition.class);
        qc.getRoot().join(ProductDefinition_.company, JoinType.INNER);
        qc.getCriteriaQuery().where(qc.getCriteriaBuilder().and(
                qc.getCriteriaBuilder()
                    .equal(qc.getRoot().get(ProductDefinition_.company), company),
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
