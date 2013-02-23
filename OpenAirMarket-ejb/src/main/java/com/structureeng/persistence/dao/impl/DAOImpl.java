// Copyright 2013 Structure Eng Inc.

package com.structureeng.persistence.dao.impl;

import com.structureeng.persistence.dao.DAO;
import com.structureeng.persistence.dao.DAOException;
import com.structureeng.persistence.model.AbstractModel;

import com.google.common.base.Preconditions;

import org.slf4j.Logger;

import java.io.Serializable;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.LockModeType;
import javax.persistence.NoResultException;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

/**
 * Provides the implementation for {@code DAO} interface.
 *
 * @author Edgar Rico (edgar.martinez.rico@gmail.com)
 * @param <T> specifies the {@code Model} of the dao
 * @param <S> specifies the {@code Serializable} identifier of the {@Model}
 */
public abstract class DAOImpl<T extends AbstractModel, S extends Serializable> implements
        DAO<T, S> {

    /**
     * Specifies the property that will be used to retrieve the.
     */
    protected static final String PERSISTENCE_ERROR_LABEL = "dao.persistence.id";
    protected static final String PERSISTENCE_ERROR_MESSAGE = "dao.persistence.message";
    protected static final String NORESULT_LABEL = "dao.noResult.id";
    protected static final String NORESULT_MESSAGE = "dao.noResult.message";
    protected static final String OPTIMISTIC_LOCK_LABEL = "dao.optimisticLocking.id";
    protected static final String OPTIMISTIC_LOCK_MESSAGE = "dao.optimisticLocking.message";
    protected static final String UNEXPECTED_LABEL = "dao.unexpected.id";
    protected static final String UNEXPECTED_MESSAGE = "dao.unexpected.message";
    private final Class<T> entityClass;
    private final Class<S> entityIdClass;

    public DAOImpl(Class<T> entityClass, Class<S> entityIdClass) {
        this.entityClass = Preconditions.checkNotNull(entityClass);
        this.entityIdClass = Preconditions.checkNotNull(entityIdClass);
    }

    @Override
    public void persist(T entity) throws DAOException {
        getEntityManager().persist(entity);
    }

    @Override
    public void merge(T entity) throws DAOException {
        getEntityManager().merge(entity);
    }

    @Override
    public void remove(T entity) throws DAOException {
        getEntityManager().remove(getEntityManager().merge(entity));
    }

    @Override
    public void refresh(T entity) {
        getEntityManager().refresh(entity);
    }

    @Override
    public void refresh(T entity, LockModeType modeType) {
        getEntityManager().refresh(entity, modeType);
    }

    @Override
    public T find(S id) {
        try {
            return getEntityManager().find(entityClass, id);
        } catch (NoResultException exc) {
            if (getLogger().isDebugEnabled()) {
                getLogger().debug(String.format(exc.getMessage().concat(" id [%s]."), id), exc);
            }
            return null;
        }
    }

    @Override
    public T find(S id, long version) throws DAOException {
        try {
            T current = getEntityManager().find(entityClass, id);
            if (current.getVersion() == version) {
                return current;
            } else {
                throw DAOException.Builder.build(OPTIMISTIC_LOCK_LABEL, OPTIMISTIC_LOCK_MESSAGE);
            }
        } catch (NoResultException exc) {
            throw DAOException.Builder.build(NORESULT_LABEL, NORESULT_MESSAGE, exc);
        }
    }

    @Override
    public List<T> findRange(int start, int end) {        
        CriteriaQuery<T> cq = getEntityManager().getCriteriaBuilder().createQuery(getEntityClass());
        cq.select(cq.from(entityClass));
        TypedQuery<T> q = getEntityManager().createQuery(cq);
        q.setMaxResults(end - start);
        q.setFirstResult(start);        
        return q.getResultList();
    }

    @Override
    public long count() {
        CriteriaQuery<Long> cq = getEntityManager().getCriteriaBuilder().createQuery(Long.class);
        Root<T> rt = cq.from(entityClass);
        cq.select(getEntityManager().getCriteriaBuilder().count(rt));
        TypedQuery<Long> q = getEntityManager().createQuery(cq);
        return q.getSingleResult().longValue();
    }

    @Override
    public void flush() {
        getEntityManager().flush();
    }

    protected int update(String jpaQL, Object... listedParam) {
        Query query = getEntityManager().createQuery(jpaQL);
        for (int i = 0; i < listedParam.length; i++) {
            query.setParameter(i + 1, listedParam[i]);
        }
        return query.executeUpdate();
    }

    @Override
    public boolean hasVersionChanged(T entity) {        
        try {            
            find(getEntityIdClass().cast(entity.getId()), entity.getVersion());
            return true;
        } catch (DAOException ex) {
            getLogger().debug(ex.getMessage());
        }
        return false;
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

    /**
     * Provides the {@code EntityManager} that is being use by the dao.
     *
     * @return - the instance
     */
    protected abstract EntityManager getEntityManager();

    /**
     * Provides the {@code Logger} of the concrete class.
     *
     * @return - the logger instance of the class.
     */
    public abstract Logger getLogger();
}
