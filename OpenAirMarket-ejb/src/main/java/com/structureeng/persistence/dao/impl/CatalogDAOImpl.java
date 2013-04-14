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
    protected final void validatePersistUniqueKeys(final T entity) throws DAOException {
        DAOException daoException = validateUniqueKeysForPersistEvent(entity);
        long uniqueId = countEntitiesWithReferenceId(
                referenceIdClass.cast(entity.getReferenceId()));
        long uniqueName = countEntitiesWithName(entity.getName());
        if (uniqueId > 0 || uniqueName > 0) {            
            if (uniqueId > 0) {
                daoException = DAOException.Builder.build(getErrorCodeUniqueReferenceId(), 
                        daoException);
            }
            if (uniqueName > 0) {
                daoException = DAOException.Builder.build(getErrorCodeUniqueName(),
                        daoException);
            }            
        }
        shouldThrowDAOException(daoException);
    }

    @Override
    protected final void validateMergeUniqueKeys(final T entity) throws DAOException {
        DAOException daoException = validateUniqueKeysForMergeEvent(entity);
        long count = countEntitiesWithSameNameButDiffReferenceId(entity);
        if (count > 0) {
            daoException = DAOException.Builder.build(getErrorCodeUniqueName(), daoException);
        }
        shouldThrowDAOException(daoException);
    }

    private T findByReferenceId(RID referenceId, Boolean active) {
        try {
            QueryContainer<T, T> qc = newQueryContainer(getEntityClass());
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

    private Long countEntitiesWithReferenceId(RID referenceId) {        
        QueryContainer<Long, T> qc = newQueryContainerCount();
        qc.getCriteriaQuery().where(qc.getCriteriaBuilder().equal(
                    qc.getRoot().get(AbstractCatalogModel_.referenceId), referenceId));
        return qc.getSingleResult();
    }

    private Long countEntitiesWithName(String name) {
        QueryContainer<Long, T> qc = newQueryContainerCount();
        qc.getCriteriaQuery().where(qc.getCriteriaBuilder().equal(
                        qc.getRoot().get(AbstractCatalogModel_.name), name));
        return qc.getSingleResult();
    }
    
    private Long countEntitiesWithSameNameButDiffReferenceId(T entity) {
        QueryContainer<Long, T> qc = newQueryContainerCount();
        qc.getCriteriaQuery().where(qc.getCriteriaBuilder().and(
                qc.getCriteriaBuilder().equal(qc.getRoot()
                    .get(AbstractCatalogModel_.name), entity.getName()),
                qc.getCriteriaBuilder().notEqual(qc.getRoot()
                    .get(AbstractCatalogModel_.referenceId), entity.getReferenceId())));
        return qc.getSingleResult();
    }

    public DAOErrorCode getErrorCodeUniqueReferenceId() {
        return DAOErrorCode.CATALOG_REFERENCE_ID_UK;
    }

    public DAOErrorCode getErrorCodeUniqueName() {
        return DAOErrorCode.CATALOG_NAME_UK;
    }
    
    private void shouldThrowDAOException(DAOException daoException) throws DAOException {
        if (daoException != null) {
            throw daoException;
        }
    }
    
    protected DAOException validateUniqueKeysForPersistEvent(final T entity) {
        return null;
    }
    
    protected DAOException validateUniqueKeysForMergeEvent(final T entity) {
        return null;
    }
}
