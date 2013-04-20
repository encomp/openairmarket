// Copyright 2013 Structure Eng Inc.

package com.structureeng.persistence.dao.impl.tenant;

import static com.google.common.base.Preconditions.checkNotNull;

import com.structureeng.persistence.dao.DAOException;
import com.structureeng.persistence.dao.TenantDAO;
import com.structureeng.persistence.dao.impl.CatalogDAOImpl;
import com.structureeng.persistence.model.tenant.Tenant;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.LockModeType;
import javax.persistence.PersistenceContext;

/**
 * Data Access Object for {@code Tenant}.
 *
 * @author Edgar Rico (edgar.martinez.rico@gmail.com)
 */
public final class TenantDAOImpl implements TenantDAO {

    private EntityManager entityManager;
    private final CatalogDAOImpl<Tenant, Integer, Integer> catalogDAO;
    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Inject
    public TenantDAOImpl() {
        catalogDAO = new CatalogDAOImpl<Tenant, Integer, Integer>(Tenant.class, Integer.class,
                Integer.class);
    }

    @Override
    public void persist(Tenant entity) throws DAOException {
        catalogDAO.persist(entity);
    }

    @Override
    public Tenant merge(Tenant entity) throws DAOException {
        return catalogDAO.merge(entity);
    }

    //TODO (edgarrico: needs to throw a DAOException)
    @Override
    public void remove(Tenant entity) throws DAOException {
        catalogDAO.remove(entity);
    }

    @Override
    public void refresh(Tenant entity) {
        catalogDAO.refresh(entity);
    }

    @Override
    public void refresh(Tenant entity, LockModeType modeType) {
        catalogDAO.refresh(entity, modeType);
    }

    @Override
    public Tenant find(Integer id) {
        return catalogDAO.find(id);
    }

    @Override
    public Tenant find(Integer id, long version) throws DAOException {
        return catalogDAO.find(id, version);
    }

    @Override
    public Tenant findByReferenceId(Integer referenceId) {
        return catalogDAO.findByReferenceId(referenceId);
    }

    @Override
    public Tenant findInactiveByReferenceId(Integer referenceId) {
        return catalogDAO.findInactiveByReferenceId(referenceId);
    }

    @Override
    public List<Tenant> findRange(int start, int count) {
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
    public boolean hasVersionChanged(Tenant entity) throws DAOException {
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
