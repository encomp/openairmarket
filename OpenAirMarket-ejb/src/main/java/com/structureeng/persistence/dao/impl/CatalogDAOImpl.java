// Copyright 2013 Structure Eng Inc.
package com.structureeng.persistence.dao.impl;

import com.structureeng.persistence.dao.CatalogDAO;
import com.structureeng.persistence.dao.DAOErrorCode;
import com.structureeng.persistence.dao.DAOException;
import com.structureeng.persistence.model.AbstractActiveModel_;
import com.structureeng.persistence.model.AbstractCatalogModel;
import com.structureeng.persistence.model.AbstractCatalogModel_;

import java.io.Serializable;

import javax.persistence.NoResultException;
import javax.persistence.PersistenceException;
import javax.persistence.TypedQuery;
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

    @Override
    public void persist(T entity) throws DAOException {
        try {
            long uniqueId = countEntitiesWithReferenceId(entity.getReferenceId());
            long uniqueName = countEntitiesWithName(entity.getName());
            if (uniqueId > 0 || uniqueName > 0) {
                if (uniqueId > 0) {
                    throw DAOException.Builder.build(getErrorCodeUniqueReferenceId());
                }
                if (uniqueName > 0) {
                    throw DAOException.Builder.build(getErrorCodeUniqueName());
                }
            } else {
                super.persist(entity);
            }
        } catch (PersistenceException persistenceException) {
            throw DAOException.Builder.build(DAOErrorCode.PERSISTENCE, persistenceException);
        }
    }

    @Override
    public T merge(T entity) throws DAOException {
        try {
            isUnique(entity);
            return super.merge(entity);
        } catch (PersistenceException persistenceException) {
            throw DAOException.Builder.build(DAOErrorCode.PERSISTENCE, persistenceException);
        }
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

    private Long countEntitiesWithReferenceId(Integer referenceId) {
        return countEntities(0, referenceId);
    }

    private Long countEntitiesWithName(String name) {
        return countEntities(1, name);
    }

    private Long countEntities(int option, Object value) {
        CriteriaBuilder qBuilder = getEntityManager().getCriteriaBuilder();
        CriteriaQuery<Long> criteriaQuery = qBuilder.createQuery(Long.class);
        Root<T> root = criteriaQuery.from(getEntityClass());
        criteriaQuery.select(qBuilder.count(root));
        switch (option) {
            case 0:
                criteriaQuery.where(
                        qBuilder.equal(root.get(AbstractCatalogModel_.referenceId), value));
                break;

            case 1:
                criteriaQuery.where(qBuilder.equal(root.get(AbstractCatalogModel_.name), value));
                break;

            default:
                break;
        }
        TypedQuery<Long> typedQuery = getEntityManager().createQuery(criteriaQuery);
        return typedQuery.getSingleResult();
    }

    private Long countEntitiesWithSameNameButDiffReferenceId(Integer referenceId, String name) {
        CriteriaBuilder qBuilder = getEntityManager().getCriteriaBuilder();
        CriteriaQuery<Long> criteriaQuery = qBuilder.createQuery(Long.class);
        Root<T> root = criteriaQuery.from(getEntityClass());
        criteriaQuery.select(qBuilder.count(root));
        criteriaQuery.where(qBuilder.and(
                qBuilder.equal(root.get(AbstractCatalogModel_.name), name),
                qBuilder.notEqual(root.get(AbstractCatalogModel_.referenceId), referenceId)));
        TypedQuery<Long> typedQuery = getEntityManager().createQuery(criteriaQuery);
        return typedQuery.getSingleResult();
    }

    private void isUnique(final T entity) throws DAOException {
        long almacenes = countEntitiesWithSameNameButDiffReferenceId(entity.getReferenceId(),
                entity.getName());
        if (almacenes > 0) {
            throw DAOException.Builder.build(getErrorCodeUniqueName());
        }
    }

    public DAOErrorCode getErrorCodeUniqueReferenceId() {
        return DAOErrorCode.CATALOG_REFERENCE_ID_UK;
    }

    public DAOErrorCode getErrorCodeUniqueName() {
        return DAOErrorCode.CATALOG_NAME_UK;
    }
}
