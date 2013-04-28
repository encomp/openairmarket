// Copyright 2013 Structure Eng Inc.

package com.structureeng.persistence.dao.impl.stock;

import static com.google.common.base.Preconditions.checkNotNull;

import com.structureeng.persistence.dao.DAOException;
import com.structureeng.persistence.dao.QueryContainer;
import com.structureeng.persistence.dao.StockDAO;
import com.structureeng.persistence.dao.impl.ActiveDAOImpl;
import com.structureeng.persistence.dao.impl.product.ProductErrorCode;
import com.structureeng.persistence.model.product.Product;
import com.structureeng.persistence.model.product.Product_;
import com.structureeng.persistence.model.stock.Stock;
import com.structureeng.persistence.model.stock.Stock_;
import com.structureeng.persistence.model.stock.Warehouse;
import com.structureeng.persistence.model.stock.Warehouse_;

import com.google.common.collect.ImmutableList;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.LockModeType;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.JoinType;
import javax.persistence.criteria.Predicate;

/**
 * Data Access Object for {@code Stock}.
 *
 * @author Edgar Rico (edgar.martinez.rico@gmail.com)
 */
public class StockDAOImpl implements StockDAO {

    private EntityManager entityManager;
    private final ActiveDAOImpl<Stock, Long> activeDAO;
    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Inject
    public StockDAOImpl() {
        activeDAO = new ActiveDAOImpl<Stock, Long>(Stock.class, Long.class);
    }

    @Override
    public void persist(Stock entity) throws DAOException {
        validateUK(entity);
        activeDAO.persist(entity);
    }

    @Override
    public Stock merge(Stock entity) throws DAOException {
        validateUK(entity);
        return activeDAO.merge(entity);
    }

    @Override
    public void remove(Stock entity) throws DAOException {
        activeDAO.remove(entity);
    }

    @Override
    public void refresh(Stock entity) {
        activeDAO.refresh(entity);
    }

    @Override
    public void refresh(Stock entity, LockModeType modeType) {
        activeDAO.refresh(entity, modeType);
    }

    @Override
    public Stock find(Long id) {
        return activeDAO.find(id);
    }

    @Override
    public Stock find(Long id, long version) throws DAOException {
        return activeDAO.find(id, version);
    }

    @Override
    public List<Stock> findRange(int start, int count) {
        return activeDAO.findRange(start, count);
    }

    @Override
    public Stock find(Product product, Warehouse warehouse) {
        return find(Boolean.TRUE, product, warehouse);
    }

    @Override
    public Stock findInactive(Product product, Warehouse warehouse) {
        return find(Boolean.FALSE, product, warehouse);
    }

    private Stock find(Boolean stockActive, Product product, Warehouse warehouse) {
        try {
            QueryContainer<Stock, Stock> query =
                    QueryContainer.newQueryContainer(getEntityManager(), Stock.class);
            query.getRoot().fetch(Stock_.product, JoinType.INNER)
                    .fetch(Product_.organization, JoinType.INNER);
            query.getRoot().fetch(Stock_.warehouse, JoinType.INNER)
                    .fetch(Warehouse_.organization, JoinType.INNER);
            ImmutableList.Builder<Predicate> builder = ImmutableList.builder();
            builder.add(query.getCriteriaBuilder()
                            .equal(query.getRoot().get(Stock_.product), product));
            builder.add(query.getCriteriaBuilder()
                            .equal(query.getRoot().get(Stock_.warehouse), warehouse));
            builder.add(query.getCriteriaBuilder()
                            .equal(
                                query.getRoot().get(Stock_.product).get(Product_.organization),
                                query.getRoot().get(Stock_.warehouse).get(Warehouse_.organization)));
            builder.addAll(getActivePredicates(query, stockActive));
            query.getCriteriaQuery().where(
                    query.getCriteriaBuilder().and(builder.build().toArray(new Predicate[]{})));
            return query.getSingleResult();
        } catch (NoResultException exc) {
            logger.warn(String.format(
                    "The Stock [%s] for the Product [%d] in the Warehouse [%d] does not exist.",
                    stockActive ? "ACTIVE" : "INACTIVE", product.getId(), warehouse.getId()));
        }
        return null;
    }

    private List<Predicate> getActivePredicates(QueryContainer<Stock, Stock> qc, boolean active) {
        ImmutableList.Builder<Predicate> builder = ImmutableList.builder();
        if (active) {
            builder.add(qc.activeEntities(qc.getRoot().get(Stock_.product)));
            builder.add(qc.activeEntities(qc.getRoot().get(Stock_.warehouse)));
            builder.add(qc.activeEntities(qc.getRoot().get(Stock_.product)
                    .get(Product_.organization)));
            builder.add(qc.activeEntities(qc.getRoot().get(Stock_.warehouse)
                    .get(Warehouse_.organization)));
            builder.add(qc.activeEntities(qc.getRoot()));
        } else {
            builder.add(qc.inactiveEntities(qc.getRoot()));
        }
        return builder.build();
    }

    @Override
    public long count() {
        return activeDAO.count();
    }

    @Override
    public long countInactive() {
        return activeDAO.countInactive();
    }

    @Override
    public void flush() {
        activeDAO.flush();
    }

    @Override
    public boolean hasVersionChanged(Stock entity) throws DAOException {
        return activeDAO.hasVersionChanged(entity);
    }

    private void validateUK(Stock entity) throws DAOException {
        DAOException daoException = null;
        long count = countStocks(entity);
        if (count > 0) {
            daoException = DAOException.Builder.build(ProductErrorCode.STOCK_UK);
        }
        daoException = isSameOrganization(entity, daoException);
        if (daoException != null) {
            throw daoException;
        }
    }

    /**
     * Validates that the {@code Product} and {@code Warehosue} belongs to the same 
     * {@code Organization}.
     *
     * @param stock         the instance that will be validated.
     * @throws DAOException in case the {@code Store} of the {@code Product} and the
     *                      {@code Warehouse} are different.
     */
    private DAOException isSameOrganization(Stock stock, DAOException exc) {
        if (!stock.getProduct().getOrganization().equals(stock.getWarehouse().getOrganization())) {
            return DAOException.Builder.build(ProductErrorCode.STOCK_CONSTRAINT_ORGANIZATION, exc);
        }
        return exc;
    }

    private Long countStocks(Stock stock) {
        QueryContainer<Long, Stock> qc =
                QueryContainer.newQueryContainerCount(getEntityManager(), Stock.class);
        qc.getRoot().join(Stock_.product, JoinType.INNER);
        qc.getRoot().join(Stock_.warehouse, JoinType.INNER);
        ImmutableList.Builder<Predicate> builder = ImmutableList.builder();
        builder.add(qc.getCriteriaBuilder()
                .equal(qc.getRoot().get(Stock_.product), stock.getProduct()));
        builder.add(qc.getCriteriaBuilder()
                .equal(qc.getRoot().get(Stock_.warehouse), stock.getWarehouse()));
        if (stock.getId() != null) {
            builder.add(qc.getCriteriaBuilder()
                    .notEqual(qc.getRoot().get(Stock_.id), stock.getId()));
        }
        qc.getCriteriaQuery().where(
                qc.getCriteriaBuilder().and(builder.build().toArray(new Predicate[]{})));
        return qc.getSingleResult();
    }

    @PersistenceContext
    public void setEntityManager(EntityManager entityManager) {
        this.entityManager = checkNotNull(entityManager);
        activeDAO.setEntityManager(entityManager);
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
