// Copyright 2013 Structure Eng Inc.

package com.structureeng.persistence.dao.impl;

import static com.google.common.base.Preconditions.checkNotNull;

import com.structureeng.persistence.dao.CatalogDAO;
import com.structureeng.persistence.dao.DAOErrorCode;
import com.structureeng.persistence.dao.DAOException;
import com.structureeng.persistence.dao.QueryContainer;
import com.structureeng.persistence.model.AbstractActiveModel_;
import com.structureeng.persistence.model.AbstractCatalogModel;
import com.structureeng.persistence.model.AbstractCatalogModel_;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.Serializable;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.LockModeType;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;

/**
 * Provides the implementation for {@code CatalogDAO} interface.
 *
 * @author Edgar Rico (edgar.martinez.rico@gmail.com)
 * @param <T> specifies the {@code AbstractActiveModel} of the data access object
 * @param <S> specifies the {@code Serializable} identifier of the {@code AbstractActiveModel}
 * @param <RID> specifies the {@code Number} identifier of the {@code AbstractCatalogModel}
 */
public final class CatalogDAOImpl<T extends AbstractCatalogModel, S extends Serializable, 
        RID extends Number> implements CatalogDAO<T, S, RID> {

    private EntityManager entityManager;
    private final Class<T> entityClass;
    private final Class<S> entityIdClass;
    private final Class<RID> referenceIdClass;
    private final ActiveDAOImpl<T, S> activeDAO;
    private final Logger logger = LoggerFactory.getLogger(getClass());

    public CatalogDAOImpl(Class<T> entityClass, Class<S> entityIdClass,
            Class<RID> referenceIdClass) {        
        this.entityClass = checkNotNull(entityClass);
        this.entityIdClass = checkNotNull(entityIdClass);
        this.referenceIdClass = checkNotNull(referenceIdClass);
        this.activeDAO = new ActiveDAOImpl<T, S>(entityClass, entityIdClass);
    }

    @Override
    public T findByReferenceId(RID referenceId) {
        return findByReferenceId(referenceId, Boolean.TRUE);
    }

    @Override
    public T findInactiveByReferenceId(RID referenceId) {
        return findByReferenceId(referenceId, Boolean.FALSE);
    }

    private T findByReferenceId(RID referenceId, Boolean active) {
        try {
            QueryContainer<T, T> qc = QueryContainer.newQueryContainer(getEntityManager(),
                    getEntityClass());
            qc.getCriteriaQuery().where(qc.getCriteriaBuilder()
                    .and(
                    qc.getCriteriaBuilder()
                    .equal(qc.getRoot().get(AbstractActiveModel_.active), active),
                    qc.getCriteriaBuilder()
                    .equal(qc.getRoot().get(AbstractCatalogModel_.referenceId), referenceId)));
            return qc.getSingleResult();
        } catch (NoResultException exc) {
            if (getLogger().isDebugEnabled()) {
                getLogger().debug(String.format(exc.getMessage().concat(" referenceId [%s]."),
                        referenceId), exc);
            }
            return null;
        }
    }

    @Override
    public void persist(T entity) throws DAOException {
        long uniqueId = countEntitiesWithReferenceId(
                referenceIdClass.cast(entity.getReferenceId()));
        long uniqueName = countEntitiesWithName(entity.getName());
        if (uniqueId > 0 || uniqueName > 0) {
            DAOException daoException = null;
            if (uniqueId > 0) {
                daoException = DAOException.Builder.build(getErrorCodeUniqueReferenceId());
            }
            if (uniqueName > 0) {
                daoException = DAOException.Builder.build(getErrorCodeUniqueName(),
                        daoException);
            }
            throw daoException;
        }
        activeDAO.persist(entity);
    }

    @Override
    public T merge(T entity) throws DAOException {
        long count = countEntitiesWithSameNameButDiffReferenceId(entity);
        if (count > 0) {
            throw DAOException.Builder.build(getErrorCodeUniqueName());
        }
        return activeDAO.merge(entity);
    }

    @Override
    public void remove(T entity) throws DAOException {
        activeDAO.remove(entity);
    }

    @Override
    public void refresh(T entity) {
        activeDAO.refresh(entity);
    }

    @Override
    public void refresh(T entity, LockModeType modeType) {
        activeDAO.refresh(entity, modeType);
    }

    @Override
    public T find(S id) {
        return activeDAO.find(id);
    }

    @Override
    public T find(S id, long version) throws DAOException {
        return activeDAO.find(id, version);
    }

    @Override
    public List<T> findRange(int start, int count) {
        return activeDAO.findRange(start, count);
    }

    @Override
    public long count() {
        return activeDAO.count();
    }

    @Override
    public long countInactive() {
        return activeDAO.countInactive();
    }

    @Override
    public void flush() {
        activeDAO.flush();
    }

    @Override
    public boolean hasVersionChanged(T entity) throws DAOException {
        return activeDAO.hasVersionChanged(entity);
    }

    private Long countEntitiesWithReferenceId(RID referenceId) {
        QueryContainer<Long, T> qc = QueryContainer.newQueryContainerCount(getEntityManager(),
                getEntityClass());
        qc.getCriteriaQuery().where(qc.getCriteriaBuilder().equal(
                qc.getRoot().get(AbstractCatalogModel_.referenceId), referenceId));
        return qc.getSingleResult();
    }

    private Long countEntitiesWithName(String name) {
        QueryContainer<Long, T> qc = QueryContainer.newQueryContainerCount(getEntityManager(),
                getEntityClass());
        qc.getCriteriaQuery().where(qc.getCriteriaBuilder().equal(
                qc.getRoot().get(AbstractCatalogModel_.name), name));
        return qc.getSingleResult();
    }

    private Long countEntitiesWithSameNameButDiffReferenceId(T entity) {
        QueryContainer<Long, T> qc = QueryContainer.newQueryContainerCount(getEntityManager(),
                getEntityClass());
        qc.getCriteriaQuery().where(qc.getCriteriaBuilder().and(
                qc.getCriteriaBuilder().equal(qc.getRoot()
                .get(AbstractCatalogModel_.name), entity.getName()),
                qc.getCriteriaBuilder().notEqual(qc.getRoot()
                .get(AbstractCatalogModel_.referenceId), entity.getReferenceId())));
        return qc.getSingleResult();
    }

    private DAOErrorCode getErrorCodeUniqueReferenceId() {
        return DAOErrorCode.CATALOG_REFERENCE_ID_UK;
    }

    private DAOErrorCode getErrorCodeUniqueName() {
        return DAOErrorCode.CATALOG_NAME_UK;
    }

    /**
     * Provides the class of this dao.
     *
     * @return - the class of the dao
     */
    public Class<T> getEntityClass() {
        return entityClass;
    }

    /**
     * Provides the class of the Id.
     *
     * @return - the class of the Id of an entity.
     */
    public Class<S> getEntityIdClass() {
        return entityIdClass;
    }
    
    @PersistenceContext
    public void setEntityManager(EntityManager entityManager) {
        this.entityManager = entityManager; 
        activeDAO.setEntityManager(entityManager);
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
