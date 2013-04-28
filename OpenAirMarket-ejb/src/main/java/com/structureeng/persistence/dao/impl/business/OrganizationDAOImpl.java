// Copyright 2013 Structure Eng Inc.

package com.structureeng.persistence.dao.impl.business;

import static com.google.common.base.Preconditions.checkNotNull;

import com.structureeng.persistence.dao.DAOException;
import com.structureeng.persistence.dao.OrganizationDAO;
import com.structureeng.persistence.dao.impl.CatalogDAOImpl;
import com.structureeng.persistence.model.business.Organization;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.LockModeType;
import javax.persistence.PersistenceContext;

/**
 * Data Access Object for {@code Organization}.
 *
 * @author Edgar Rico (edgar.martinez.rico@gmail.com)
 */
public final class OrganizationDAOImpl implements OrganizationDAO {

    private EntityManager entityManager;
    private final CatalogDAOImpl<Organization, Long, Integer> catalogDAO;
    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Inject
    public OrganizationDAOImpl() {
        catalogDAO =
                new CatalogDAOImpl<Organization, Long, Integer>(Organization.class, Long.class,
                    Integer.class);
    }

    @Override
    public void persist(Organization entity) throws DAOException {
        catalogDAO.persist(entity);
    }

    @Override
    public Organization merge(Organization entity) throws DAOException {
        return catalogDAO.merge(entity);
    }

    @Override
    public void remove(Organization entity) throws DAOException {
        catalogDAO.remove(entity);
    }

    @Override
    public void refresh(Organization entity) {
        catalogDAO.refresh(entity);
    }

    @Override
    public void refresh(Organization entity, LockModeType modeType) {
        catalogDAO.refresh(entity, modeType);
    }

    @Override
    public Organization find(Long id) {
        return catalogDAO.find(id);
    }

    @Override
    public Organization find(Long id, long version) throws DAOException {
        return catalogDAO.find(id, version);
    }

    @Override
    public Organization findByReferenceId(Integer referenceId) {
        return catalogDAO.findByReferenceId(referenceId);
    }

    @Override
    public Organization findInactiveByReferenceId(Integer referenceId) {
        return catalogDAO.findInactiveByReferenceId(referenceId);
    }

    @Override
    public List<Organization> findRange(int start, int count) {
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
    public boolean hasVersionChanged(Organization entity) throws DAOException {
        return catalogDAO.hasVersionChanged(entity);
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
