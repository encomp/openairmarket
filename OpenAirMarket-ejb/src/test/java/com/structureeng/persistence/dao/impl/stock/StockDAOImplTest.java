// Copyright 2013 Structure Eng Inc.

package com.structureeng.persistence.dao.impl.stock;

import com.structureeng.persistence.dao.DAOException;
import com.structureeng.persistence.dao.StockDAO;
import com.structureeng.persistence.dao.impl.AbstractTenantModelDAOImplTest;
import com.structureeng.persistence.model.Model;
import com.structureeng.persistence.model.business.Organization;
import com.structureeng.persistence.model.history.stock.StockHistory;
import com.structureeng.persistence.model.history.stock.StockHistory_;
import com.structureeng.persistence.model.product.Product;
import com.structureeng.persistence.model.stock.Stock;
import com.structureeng.persistence.model.stock.Warehouse;

import org.junit.Assert;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;
import org.springframework.test.context.transaction.TransactionConfiguration;

import java.math.BigDecimal;
import java.util.List;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.JoinType;
import javax.persistence.criteria.Root;

/**
 * Test for {@code StockDAOImpl}.
 *
 * @author Edgar Rico (edgar.martinez.rico@gmail.com)
 */
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
@TransactionConfiguration(transactionManager = "transactionManager", defaultRollback = false)
public class StockDAOImplTest extends AbstractTenantModelDAOImplTest<Stock, StockHistory> {

    private StockDAO stockDAO;
    private static Stock stock;

    @Test
    public void testPersistA() throws DAOException {
        Organization store = createStore(1L);
        Product product = createProduct(1L, store);
        Warehouse warehouse = createWarehouse(1L, store);
        Stock stock = build(product, warehouse);
        getStockDAO().merge(stock);
    }

    @Test
    public void testPersistB() throws DAOException {
        Organization store = createStore(1L);
        Product product = createProduct(2L, store);
        Warehouse warehouse = createWarehouse(1L, store);
        stock = build(product, warehouse);
        getStockDAO().persist(stock);
    }

    @Test
    public void testPersistCount() throws DAOException {
        Long count = getStockDAO().count();
        Assert.assertTrue(count >= 2);
    }

    @Test(expected = DAOException.class)
    public void testPersistMerge() throws DAOException {
        Organization store = createStore(1L);
        stock.setProduct(createProduct(2L, store));
        stock.setWarehouse(createWarehouse(2L, createStore(2L)));
        try {
            getStockDAO().merge(stock);
            getStockDAO().flush();
            Assert.fail("Should have thrown a DAOException.");
        } catch (DAOException daoException) {
            deleteHistory(stock);
            throw daoException;
        }
    }

    @Test(expected = DAOException.class)
    public void testPersistMergeDirty() throws DAOException {
        Organization store = createStore(1L);
        Product product = createProduct(3L, store);
        Warehouse warehouse = createWarehouse(1L, store);
        Stock stock = build(product, warehouse);
        getStockDAO().persist(stock);
        getStockDAO().flush();
        stock.setWarehouse(createWarehouse(2L, createStore(2L)));
        try {
            getStockDAO().merge(stock);
            Assert.fail("Should have thrown a DAOException.");
        } catch (DAOException daoException) {
            commit = false;
            throw daoException;
        }
    }

    @Test(expected = DAOException.class)
    public void testPersistUK() throws DAOException {
        Organization store = createStore(1L);
        Product product = createProduct(1L, store);
        Warehouse warehouse = createWarehouse(1L, store);
        Stock stock = build(product, warehouse);
        try {
            getStockDAO().persist(stock);
            getStockDAO().flush();
            Assert.fail("Should have thrown a DAOException.");
        } catch (DAOException daoException) {
            commit = false;
            throw daoException;
        }
    }

    @Test
    public void testRange() {
        List<Stock> catalogModels = getStockDAO().findRange(0, 2);
        Assert.assertNotNull(catalogModels);
        Assert.assertTrue(catalogModels.size() >= 1);
    }

    @Test
    public void testUpdate() throws DAOException {
        Organization store = createStore(1L);
        Product product = createProduct(1L, store);
        Warehouse warehouse = createWarehouse(1L, store);
        Stock stockTemp = getStockDAO().find(product, warehouse);
        stockTemp.setProduct(createProduct(3L, store));
        stock = getStockDAO().merge(stockTemp);
    }

    @Test
    public void testUpdateFind() throws DAOException {
        Organization store = createStore(1L);
        Product product = createProduct(3L, store);
        Warehouse warehouse = createWarehouse(1L, store);
        Stock catalogModel = getStockDAO().find(product, warehouse);
        Model tmp = getStockDAO().find(catalogModel.getId());
        Assert.assertEquals(catalogModel, tmp);
        tmp = getStockDAO().find(catalogModel.getId(), catalogModel.getVersion());
        Assert.assertEquals(catalogModel, tmp);
    }

    @Test
    public void testUpdateMerge() throws DAOException {
        stock.setStockAmount(BigDecimal.TEN);
        stock.setMaximumStock(BigDecimal.TEN);
        stock.setMinimumStock(BigDecimal.TEN);
        stock.setWaste(BigDecimal.TEN);
        getStockDAO().merge(stock);
    }

    @Test
    public void testUpdateRemove() throws DAOException {
        Organization store = createStore(1L);
        Product product = createProduct(3L, store);
        Warehouse warehouse = createWarehouse(1L, store);
        Stock catalogModel = getStockDAO().find(product, warehouse);
        getStockDAO().remove(catalogModel);
    }

    @Test
    public void testUpdateRemoveCount() {
        Long count = getStockDAO().countInactive();
        Assert.assertTrue(count >= 1);
    }

    @Test
    public void testUpdateRemoveFind() throws DAOException {
        Organization store = createStore(1L);
        Product product = createProduct(3L, store);
        Warehouse warehouse = createWarehouse(1L, store);
        Stock catalogModel = getStockDAO().findInactive(product, warehouse);
        Stock tmp = getStockDAO().find(catalogModel.getId());
        Assert.assertNull(tmp);
        tmp = getStockDAO().find(catalogModel.getId(), catalogModel.getVersion());
        Assert.assertNull(tmp);
    }

    @Test
    public void testValidateRemove() {
        Organization store = createStore(1L);
        Product product = createProduct(3L, store);
        Warehouse warehouse = createWarehouse(1L, store);
        Stock stock = getStockDAO().findInactive(product, warehouse);
        deleteHistory(stock);
    }

    public void deleteHistory(Stock stock) {
        CriteriaBuilder cb = getEntityManager().getCriteriaBuilder();
        CriteriaQuery<StockHistory> cq = cb.createQuery(StockHistory.class);
        Root<StockHistory> root = cq.from(StockHistory.class);
        root.fetch(StockHistory_.audit, JoinType.INNER);
        root.fetch(StockHistory_.stock, JoinType.INNER);
        cq.where(cb.equal(root.get(StockHistory_.stock), stock));
        List<StockHistory> histories = getEntityManager().createQuery(cq).getResultList();
        deleteTenantHistories(stock, histories);
    }

    public Stock build(Product product, Warehouse warehouse) {
        Stock.Buider buider = Stock.newBuilder();
        buider.setStockAmount(BigDecimal.TEN);
        buider.setMaximumStock(BigDecimal.TEN);
        buider.setMinimumStock(BigDecimal.TEN);
        buider.setWaste(BigDecimal.ONE);
        buider.setProduct(product);
        buider.setWarehouse(warehouse);
        return buider.build();
    }

    public Warehouse createWarehouse(Long id, Organization store) {
        Warehouse warehouse = new Warehouse();
        warehouse.setId(id);
        warehouse.setOrganization(store);
        return warehouse;
    }

    public Product createProduct(Long id, Organization store) {
        Product product = new Product();
        product.setId(id);
        product.setOrganization(store);
        return product;
    }

    public Organization createStore(Long id) {
        Organization store = new Organization();
        store.setId(id);
        return store;
    }

    public StockDAO getStockDAO() {
        if (stockDAO == null) {
            stockDAO = getApplicationContext().getBean(StockDAO.class);
        }
        return stockDAO;
    }
}
