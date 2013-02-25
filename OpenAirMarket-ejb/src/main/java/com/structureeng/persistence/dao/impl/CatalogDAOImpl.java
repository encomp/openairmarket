// Copyright 2013 Structure Eng Inc.

package com.structureeng.persistence.dao.impl;

import com.structureeng.persistence.dao.CatalogDAO;
import com.structureeng.persistence.model.AbstractActiveModel_;
import com.structureeng.persistence.model.AbstractCatalogModel;
import com.structureeng.persistence.model.AbstractCatalogModel_;

import java.io.Serializable;

import javax.persistence.NoResultException;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

/**
 * Provides the implementation for {@code CatalogDAO} interface.
 *
 * @author Edgar Rico (edgar.martinez.rico@gmail.com)
 * @param <T> specifies the {@code AbstractActiveModel} of the data access object
 * @param <S> specifies the {@code Serializable} identifier of the {@code AbstractActiveModel}
 */
public abstract class CatalogDAOImpl<T extends AbstractCatalogModel, S extends Serializable>
        extends ActiveDAOImpl<T, S> implements CatalogDAO<T, S> {

    public CatalogDAOImpl(Class<T> entityClass, Class<S> entityIdClass) {
        super(entityClass, entityIdClass);
    }

    @Override
    public T findByReferenceId(Integer referenceId) {
        return findByReferenceId(referenceId, Boolean.TRUE);
    }

    @Override
    public T findInactiveByReferenceId(Integer referenceId) {
        return findByReferenceId(referenceId, Boolean.FALSE);
    }

    private T findByReferenceId(Integer referenceId, Boolean active) {
        try {
            CriteriaBuilder cb = getEntityManager().getCriteriaBuilder();
            CriteriaQuery<T> cq = cb.createQuery(getEntityClass());
            Root<T> root = cq.from(getEntityClass());
            cq.where(cb.and(
                    cb.equal(root.get(AbstractActiveModel_.active), active),
                    cb.equal(root.get(AbstractCatalogModel_.referenceId), referenceId)));
            return getEntityManager().createQuery(cq).getSingleResult();
        } catch (NoResultException exc) {
            if (getLogger().isDebugEnabled()) {
                getLogger().debug(String.format(exc.getMessage().concat(" referenceId [%s]."),
                        referenceId), exc);
            }
            return null;
        }
    }
}
