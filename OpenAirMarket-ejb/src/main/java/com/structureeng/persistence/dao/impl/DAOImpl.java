// Copyright 2013 Structure Eng Inc.

package com.structureeng.persistence.dao.impl;

import static com.google.common.base.Preconditions.checkNotNull;

import com.structureeng.persistence.dao.DAO;
import com.structureeng.persistence.dao.DAOErrorCode;
import com.structureeng.persistence.dao.DAOException;
import com.structureeng.persistence.model.AbstractModel;

import org.slf4j.Logger;

import java.io.Serializable;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.LockModeType;
import javax.persistence.NoResultException;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

/**
 * Provides the implementation for {@code DAO} interface.
 *
 * @author Edgar Rico (edgar.martinez.rico@gmail.com)
 * @param <T> specifies the {@code Model} of the data access object
 * @param <S> specifies the {@code Serializable} identifier of the {@code Model}
 */
public abstract class DAOImpl<T extends AbstractModel, S extends Serializable> implements
        DAO<T, S> {

    private final Class<T> entityClass;
    private final Class<S> entityIdClass;

    public DAOImpl(Class<T> entityClass, Class<S> entityIdClass) {
        this.entityClass = checkNotNull(entityClass);
        this.entityIdClass = checkNotNull(entityIdClass);
    }

    @Override
    public void persist(T entity) throws DAOException {
        getEntityManager().persist(entity);
    }

    @Override
    public T merge(T entity) throws DAOException {
        if (!hasVersionChanged(entity)) {
            if (!getEntityManager().contains(entity)) {
                return getEntityManager().merge(entity);
            } else {
                return entity;
            }
        } else {
            throw DAOException.Builder.build(DAOErrorCode.OPRIMISTIC_LOCKING);
        }
    }

    @Override
    public final void refresh(T entity) {
        getEntityManager().refresh(entity);
    }

    @Override
    public final void refresh(T entity, LockModeType modeType) {
        getEntityManager().refresh(entity, modeType);
    }

    @Override
    public T find(S id) {
        try {
            return getEntityManager().find(getEntityClass(), id);
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
            T current = getEntityManager().find(getEntityClass(), id);
            if (current.getVersion() == version) {
                return current;
            } else {
                throw DAOException.Builder.build(DAOErrorCode.OPRIMISTIC_LOCKING);
            }
        } catch (NoResultException exc) {
            throw DAOException.Builder.build(DAOErrorCode.NO_RESULT);
        }
    }

    @Override
    public final void flush() {
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
            return false;
        } catch (DAOException ex) {
            getLogger().debug(ex.getMessage());
            return true;
        }
    }

    /**
     * Provides the class of this dao.
     *
     * @return - the class of the dao
     */
    public final Class<T> getEntityClass() {
        return entityClass;
    }

    /**
     * Provides the class of the Id.
     *
     * @return - the class of the Id of an entity.
     */
    public final Class<S> getEntityIdClass() {
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
    
    /**
     * Create a new count {@link QueryContainer}.
     * 
     * @return new instance
     */
    public final QueryContainer<Long, T> newQueryContainerCount() {
        QueryContainer<Long, T> qc = new QueryContainer<Long, T>(getEntityManager(), Long.class, 
                getEntityClass());
        qc.getCriteriaQuery().select(qc.getCriteriaBuilder().countDistinct(qc.getRoot()));
        return qc;
    }
    
    /**
     * Create a new count {@link QueryContainer}.
     * @param <TT>  the type that will be use to create the {@code QueryContainer}.
     * @param clase the class that will be use to create the {@code CriteriaQuery} and the 
     *              {@code Root}.
     * @return new instance
     */
    public final <TT> QueryContainer<Long, TT> newQueryContainerCount(Class<TT> clase) {
        QueryContainer<Long, TT> qc = new QueryContainer<Long, TT>(getEntityManager(), Long.class, 
                clase);
        qc.getCriteriaQuery().select(qc.getCriteriaBuilder().countDistinct(qc.getRoot()));
        return qc;
    }
    
    /**
     * Creates a new instance of {@link QueryContainer}.
     * 
     * @param <R>   the type that will be use to create the {@code QueryContainer}.
     * @param clase the class that will be use to create the {@code CriteriaQuery} and the 
     *              {@code Root}.
     * @return new instance
     */
    public final <R> QueryContainer<R, R> newQueryContainer(Class<R> clase) {
        return new QueryContainer<R, R>(getEntityManager(), clase, clase);
    }

    /**
     * Stores the minimum required objects to perform a @{code Query}.
     *
     * @author Edgar Rico (edgar.martinez.rico@gmail.com)
     */
    public static class QueryContainer<R, F> {

        private final EntityManager entityManager;
        private final CriteriaBuilder criteriaBuilder;
        private final CriteriaQuery<R> criteriaQuery;
        private final Root<F> root;        
                        
        public QueryContainer(EntityManager entityManager, Class<R> result, Class<F> from) {
            this.entityManager = checkNotNull(entityManager);
            this.criteriaBuilder = checkNotNull(getEntityManager().getCriteriaBuilder());
            this.criteriaQuery = checkNotNull(criteriaBuilder.createQuery(result));
            this.root = checkNotNull(criteriaQuery.from(from));
        }

        public EntityManager getEntityManager() {
            return entityManager;
        }
                
        public CriteriaBuilder getCriteriaBuilder() {
            return criteriaBuilder;
        }

        public CriteriaQuery<R> getCriteriaQuery() {
            return criteriaQuery;
        }

        public Root<F> getRoot() {
            return root;
        }       
        
        public R getSingleResult() {
            return createTypedQuery().getSingleResult();
        }
        
        public List<R> getResultList() {
            return createTypedQuery().getResultList();
        }
        
        public List<R> getResultList(int firstResult, int maxResults) {
            TypedQuery<R> typedQuery = createTypedQuery();
            typedQuery.setFirstResult(firstResult);
            typedQuery.setMaxResults(maxResults);            
            return typedQuery.getResultList();
        }

        private TypedQuery<R> createTypedQuery() {
            return getEntityManager().createQuery(criteriaQuery);
        }
    }
}
