// Copyright 2013 Structure Eng Inc.

package com.structureeng.persistence.dao.impl.product;

import static com.google.common.base.Preconditions.checkNotNull;

import com.structureeng.persistence.dao.DAOException;
import com.structureeng.persistence.dao.MeasureUnitDAO;
import com.structureeng.persistence.dao.QueryContainer;
import com.structureeng.persistence.dao.impl.CatalogDAOImpl;
import com.structureeng.persistence.model.product.MeasureUnit;
import com.structureeng.persistence.model.product.Product;
import com.structureeng.persistence.model.product.ProductDefinition_;
import com.structureeng.persistence.model.product.Product_;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.LockModeType;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.JoinType;

/**
 * Data Access Object for {@code MeasureUnit}.
 *
 * @author Edgar Rico (edgar.martinez.rico@gmail.com)
 */
public final class MeasureUnitDAOImpl implements MeasureUnitDAO {

    private EntityManager entityManager;
    private final CatalogDAOImpl<MeasureUnit, Long, Integer> catalogDAO;
    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Inject
    public MeasureUnitDAOImpl() {
        catalogDAO = new CatalogDAOImpl<MeasureUnit, Long, Integer>(MeasureUnit.class, Long.class, 
                Integer.class);
    }
    
    @Override
    public void persist(MeasureUnit entity) throws DAOException {
        catalogDAO.persist(entity);
    }

    @Override
    public MeasureUnit merge(MeasureUnit entity) throws DAOException {
        return catalogDAO.merge(entity);
    }

    @Override
    public void remove(MeasureUnit entity) throws DAOException {
        if (entity.getActive()) {
            long count = countRetailProductWithMeasureUnit(entity);
            if (count > 0) {
                throw DAOException.Builder.build(ProductErrorCode.MEASURE_UNIT_FK);
            }
        }
        catalogDAO.remove(entity);
    }

    @Override
    public void refresh(MeasureUnit entity) {
        catalogDAO.refresh(entity);
    }

    @Override
    public void refresh(MeasureUnit entity, LockModeType modeType) {
        catalogDAO.refresh(entity, modeType);
    }

    @Override
    public MeasureUnit find(Long id) {
        return catalogDAO.find(id);
    }

    @Override
    public MeasureUnit find(Long id, long version) throws DAOException {
        return catalogDAO.find(id, version);
    }
    
    @Override
    public MeasureUnit findByReferenceId(Integer referenceId) {
        return catalogDAO.findByReferenceId(referenceId);
    }

    @Override
    public MeasureUnit findInactiveByReferenceId(Integer referenceId) {
        return catalogDAO.findInactiveByReferenceId(referenceId);
    }

    @Override
    public List<MeasureUnit> findRange(int start, int count) {
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
    public boolean hasVersionChanged(MeasureUnit entity) throws DAOException {
        return catalogDAO.hasVersionChanged(entity);
    }

    private long countRetailProductWithMeasureUnit(MeasureUnit measureUnit) {
        QueryContainer<Long, Product> qc = 
                QueryContainer.newQueryContainerCount(getEntityManager(), Product.class);
        qc.getCriteriaQuery().select(qc.getCriteriaBuilder().countDistinct(qc.getRoot()));
        qc.getRoot().join(Product_.measureUnit, JoinType.INNER);
        qc.getCriteriaQuery().where(qc.getCriteriaBuilder().and(
                qc.getCriteriaBuilder().equal(
                    qc.getRoot().get(Product_.measureUnit), measureUnit),
                qc.getCriteriaBuilder().equal(
                    qc.getRoot().get(ProductDefinition_.active), Boolean.TRUE)));
        return qc.getSingleResult();
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
