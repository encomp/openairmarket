// Copyright 2013 Structure Eng Inc.

package com.structureeng.persistence.dao.impl;

import com.structureeng.persistence.dao.ActiveDAO;
import com.structureeng.persistence.dao.DAOException;
import com.structureeng.persistence.model.AbstractActiveModel;
import com.structureeng.persistence.model.AbstractActiveModel_;

import java.io.Serializable;
import java.util.List;

import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

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
    public void remove(T entity) throws DAOException {
        entity.setActive(Boolean.FALSE);
        super.merge(entity);
    }

    @Override
    public T find(S id) {
        T entity = super.find(id);
        if (entity.getActive()) {
            return entity;
        }
        return null;
    }

    @Override
    public T find(S id, long version) throws DAOException {
        T entity = super.find(id, version);
        if (entity.getActive()) {
            return entity;
        }
        return null;
    }
    
    @Override
    public long count() {
        return count(Boolean.TRUE);
    }

    @Override
    public long countInactive() {
        return count(Boolean.FALSE);
    }
    
    private long count(Boolean active) {
        CriteriaBuilder cb = getEntityManager().getCriteriaBuilder();        
        CriteriaQuery<Long> cq = cb.createQuery(Long.class);
        Root<T> root = cq.from(getEntityClass());        
        cq.select(cb.count(root));
        cq.where(cb.equal(root.get(AbstractActiveModel_.active), active));
        TypedQuery<Long> q = getEntityManager().createQuery(cq);
        return q.getSingleResult().longValue();
    }
    
    @Override
    public List<T> findRange(int start, int end) {
        CriteriaBuilder cb = getEntityManager().getCriteriaBuilder();
        CriteriaQuery<T> cq = cb.createQuery(getEntityClass());        
        Root<T> root = cq.from(getEntityClass());
        cq.where(cb.equal(root.get(AbstractActiveModel_.active), Boolean.TRUE));
        TypedQuery<T> q = getEntityManager().createQuery(cq);
        q.setMaxResults(end - start);
        q.setFirstResult(start);
        return q.getResultList();
    }
}
