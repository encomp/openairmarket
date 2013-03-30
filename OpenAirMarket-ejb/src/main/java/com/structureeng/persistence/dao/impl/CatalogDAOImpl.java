// Copyright 2013 Structure Eng Inc.

package com.structureeng.persistence.dao.impl;

import com.structureeng.persistence.dao.CatalogDAO;
import com.structureeng.persistence.dao.DAOErrorCode;
import com.structureeng.persistence.dao.DAOException;
import com.structureeng.persistence.model.AbstractActiveModel_;
import com.structureeng.persistence.model.AbstractCatalogModel;
import com.structureeng.persistence.model.AbstractCatalogModel_;

import com.google.common.base.Preconditions;
import java.io.Serializable;

import javax.persistence.NoResultException;
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
 * @param <RID> specifies the {@code Number} identifier of the {@code AbstractCatalogModel}
 */
public abstract class CatalogDAOImpl<T extends AbstractCatalogModel, S extends Serializable,
        RID extends Number> extends ActiveDAOImpl<T, S> implements CatalogDAO<T, S, RID> {

    private final Class<RID> referenceIdClass;

    public CatalogDAOImpl(Class<T> entityClass, Class<S> entityIdClass,
            Class<RID> referenceIdClass) {
        super(entityClass, entityIdClass);
        this.referenceIdClass = Preconditions.checkNotNull(referenceIdClass);
    }

    @Override
    public T findByReferenceId(RID referenceId) {
        return findByReferenceId(referenceId, Boolean.TRUE);
    }

    @Override
    public T findInactiveByReferenceId(RID referenceId) {
        return findByReferenceId(referenceId, Boolean.FALSE);
    }

    @Override
    protected void validatePersistUniqueKeys(final T entity) throws DAOException {
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
    }

    @Override
    protected void validateMergeUniqueKeys(final T entity) throws DAOException {
        long almacenes = countEntitiesWithSameNameButDiffReferenceId(
                referenceIdClass.cast(entity.getReferenceId()), entity.getName());
        if (almacenes > 0) {
            throw DAOException.Builder.build(getErrorCodeUniqueName());
        }
    }

    private T findByReferenceId(RID referenceId, Boolean active) {
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

    private Long countEntitiesWithReferenceId(RID referenceId) {
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

    private Long countEntitiesWithSameNameButDiffReferenceId(RID referenceId, String name) {
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

    public DAOErrorCode getErrorCodeUniqueReferenceId() {
        return DAOErrorCode.CATALOG_REFERENCE_ID_UK;
    }

    public DAOErrorCode getErrorCodeUniqueName() {
        return DAOErrorCode.CATALOG_NAME_UK;
    }
}
