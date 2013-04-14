// Copyright 2013 Structure Eng Inc.

package com.structureeng.persistence.dao.impl;

import com.structureeng.persistence.dao.ActiveDAO;
import com.structureeng.persistence.dao.DAOErrorCode;
import com.structureeng.persistence.dao.DAOException;
import com.structureeng.persistence.model.AbstractActiveModel;
import com.structureeng.persistence.model.AbstractActiveModel_;

import java.io.Serializable;
import java.util.List;

import javax.persistence.PersistenceException;

/**
 * Provides the implementation for {@code ActiveDAO} interface.
 *
 * @author Edgar Rico (edgar.martinez.rico@gmail.com)
 * @param <T> specifies the {@code AbstractActiveModel} of the data access object
 * @param <S> specifies the {@code Serializable} identifier of the {@code AbstractActiveModel}
 */
public abstract class ActiveDAOImpl<T extends AbstractActiveModel, S extends Serializable> extends
        DAOImpl<T, S> implements ActiveDAO<T, S> {

    public ActiveDAOImpl(Class<T> entityClass, Class<S> entityIdClass) {
        super(entityClass, entityIdClass);
    }

    @Override
    public final void persist(T entity) throws DAOException {
        try {
            validatePersistUniqueKeys(entity);
            super.persist(entity);
        } catch (PersistenceException persistenceException) {
            throw DAOException.Builder.build(DAOErrorCode.PERSISTENCE, persistenceException);
        }
    }

    @Override
    public final T merge(T entity) throws DAOException {
        try {
            validateMergeUniqueKeys(entity);
            return super.merge(entity);
        } catch (PersistenceException persistenceException) {
            throw DAOException.Builder.build(DAOErrorCode.PERSISTENCE, persistenceException);
        }
    }

    @Override
    public final void remove(T entity) throws DAOException {
        validateForeignKeys(entity);
        try {
            if (!hasVersionChanged(entity)) {
                entity.setActive(Boolean.FALSE);
                merge(entity);
            } else {
                throw DAOException.Builder.build(DAOErrorCode.OPRIMISTIC_LOCKING);
            }
        } catch (PersistenceException persistenceException) {
            throw DAOException.Builder.build(DAOErrorCode.PERSISTENCE, persistenceException);
        }
    }

    @Override
    public final T find(S id) {
        T entity = super.find(id);
        if (entity.getActive()) {
            return entity;
        }
        return null;
    }

    @Override
    public final T find(S id, long version) throws DAOException {
        T entity = super.find(id, version);
        if (entity.getActive()) {
            return entity;
        }
        return null;
    }

    /**
     * Count the number of instances in the persistent storage that are active.
     * 
     * @return the number of active entities.
     */
    @Override
    public final long count() {
        return countEntities(Boolean.TRUE);
    }
    
    @Override
    public final long countInactive() {
        return countEntities(Boolean.FALSE);
    }
    
    private long countEntities(Boolean value) {
        QueryContainer<Long, T> qc = newQueryContainerCount();
        qc.getCriteriaQuery().where(qc.getCriteriaBuilder()
                .equal(qc.getRoot().get(AbstractActiveModel_.active), value));
        return qc.getSingleResult();
    }

    @Override
    public final List<T> findRange(int start, int end) {        
        QueryContainer<T, T> qc = newQueryContainer(getEntityClass());
        qc.getCriteriaQuery().where(qc.getCriteriaBuilder()
                        .equal(qc.getRoot().get(AbstractActiveModel_.active), Boolean.TRUE));        
        return qc.getResultList(start, end - start);
    }

    /**
     * Validates the unique keys before an entity will be inserted in the persistence storage.
     *
     * @param entity the entity that should be validated.
     * @throws DAOException this exception will be thrown if the validation process failed.
     */
    protected abstract void validatePersistUniqueKeys(final T entity) throws DAOException;

    /**
     * Validates the unique keys before an entity will be updated from the persistence storage.
     *
     * @param entity the entity that should be validated.
     * @throws DAOException this exception will be thrown if the validation process failed.
     */
    protected abstract void validateMergeUniqueKeys(final T entity) throws DAOException;

    /**
     * Validates the foreign keys before an entity will be deleted from the persistence storage.
     *
     * @param entity the entity that should be validated.
     * @throws DAOException this exception will be thrown if the validation process failed.
     */
    protected abstract void validateForeignKeys(T entity) throws DAOException;
}
