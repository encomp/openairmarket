// Copyright 2013 Structure Eng Inc.

package com.structureeng.persistence.dao.impl.stock;

import static com.google.common.base.Preconditions.checkNotNull;

import com.structureeng.persistence.dao.DAOException;
import com.structureeng.persistence.dao.QueryContainer;
import com.structureeng.persistence.dao.WarehouseDAO;
import com.structureeng.persistence.dao.impl.CatalogDAOImpl;
import com.structureeng.persistence.dao.impl.product.ProductErrorCode;
import com.structureeng.persistence.model.stock.Stock;
import com.structureeng.persistence.model.stock.Stock_;
import com.structureeng.persistence.model.stock.Warehouse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.LockModeType;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.JoinType;

/**
 * Data Access Object for {@code Warehouse}.
 *
 * @author Edgar Rico (edgar.martinez.rico@gmail.com)
 */
public final class WarehouseDAOImpl implements WarehouseDAO {

    private EntityManager entityManager;
    private final CatalogDAOImpl<Warehouse, Long, Integer> catalogDAO;
    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Inject
    public WarehouseDAOImpl() {
        catalogDAO = new CatalogDAOImpl<Warehouse, Long, Integer>(Warehouse.class, Long.class,
                Integer.class);
    }

    @Override
    public void persist(Warehouse entity) throws DAOException {
        catalogDAO.persist(entity);
    }

    @Override
    public Warehouse merge(Warehouse entity) throws DAOException {
        return catalogDAO.merge(entity);
    }

    @Override
    public void remove(Warehouse entity) throws DAOException {
        long count = countStocksWithWarehouse(entity);
        if (count > 0) {
            throw DAOException.Builder.build(ProductErrorCode.WAREHOUSE_STOCK_FK);
        }
        catalogDAO.remove(entity);
    }

    @Override
    public void refresh(Warehouse entity) {
        catalogDAO.refresh(entity);
    }

    @Override
    public void refresh(Warehouse entity, LockModeType modeType) {
        catalogDAO.refresh(entity, modeType);
    }

    @Override
    public Warehouse find(Long id) {
        return catalogDAO.find(id);
    }

    @Override
    public Warehouse find(Long id, long version) throws DAOException {
        return catalogDAO.find(id, version);
    }

    @Override
    public Warehouse findByReferenceId(Integer referenceId) {
        return catalogDAO.findByReferenceId(referenceId);
    }

    @Override
    public Warehouse findInactiveByReferenceId(Integer referenceId) {
        return catalogDAO.findInactiveByReferenceId(referenceId);
    }

    @Override
    public List<Warehouse> findRange(int start, int count) {
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
    public boolean hasVersionChanged(Warehouse entity) throws DAOException {
        return catalogDAO.hasVersionChanged(entity);
    }

    private long countStocksWithWarehouse(final Warehouse warehouse) {
        QueryContainer<Long, Stock> qc =
                QueryContainer.newQueryContainerCount(getEntityManager(), Stock.class);
        qc.getCriteriaQuery().select(qc.getCriteriaBuilder().countDistinct(qc.getRoot()));
        qc.getRoot().join(Stock_.warehouse, JoinType.INNER);
        qc.getCriteriaQuery().where(qc.getCriteriaBuilder().and(
                qc.getCriteriaBuilder()
                    .equal(qc.getRoot().get(Stock_.warehouse), warehouse)));
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
