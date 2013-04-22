// Copyright 2013 Structure Eng Inc.

package com.structureeng.persistence.dao.impl.product;

import com.structureeng.persistence.dao.DAOException;
import com.structureeng.persistence.dao.PurchasePriceDAO;
import com.structureeng.persistence.dao.impl.AbstractTenantModelDAOImplTest;
import com.structureeng.persistence.model.Model;
import com.structureeng.persistence.model.business.Store;
import com.structureeng.persistence.model.history.product.PurchasePriceHistory;
import com.structureeng.persistence.model.history.product.PurchasePriceHistory_;
import com.structureeng.persistence.model.history.product.SalePriceHistory;
import com.structureeng.persistence.model.history.product.SalePriceHistory_;
import com.structureeng.persistence.model.product.Product;
import com.structureeng.persistence.model.product.PurchasePrice;

import org.junit.Assert;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;
import org.springframework.test.context.transaction.TransactionConfiguration;

import java.math.BigDecimal;
import java.util.List;

import javax.persistence.PersistenceException;
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
public class PurchasePriceDAOImplTest extends
        AbstractTenantModelDAOImplTest<PurchasePrice, PurchasePriceHistory> {

    private PurchasePriceDAO purchasePriceDAO;
    private static PurchasePrice purchasePrice;

    @Test
    public void testPersistA() throws DAOException {
        Product product = createProduct(1L, createStore(1L));
        PurchasePrice purchasePrice = build(product, BigDecimal.ONE);
        getPurchasePriceDAO().merge(purchasePrice);
    }

    @Test
    public void testPersistB() throws DAOException {
        Product product = createProduct(2L, createStore(1L));
        purchasePrice = build(product, BigDecimal.TEN);
        getPurchasePriceDAO().persist(purchasePrice);
    }

    @Test
    public void testPersistCount() throws DAOException {
        Long count = getPurchasePriceDAO().count();
        Assert.assertTrue(count >= 2);
    }

    @Test(expected = DAOException.class)
    public void testPersistMerge() throws DAOException {
        purchasePrice.setProduct(createProduct(1L, createStore(1L)));
        purchasePrice.setQuantity(BigDecimal.ONE);
        try {
            getPurchasePriceDAO().merge(purchasePrice);
            getPurchasePriceDAO().flush();
            Assert.fail("Should have thrown a DAOException.");
        } catch (DAOException daoException) {
            deleteHistory(purchasePrice);
            throw daoException;
        }
    }

    @Test(expected = PersistenceException.class)
    public void testPersistMergeDirty() throws DAOException {
        Product product = createProduct(3L, createStore(1L));
        PurchasePrice purchasePrice = build(product, BigDecimal.ONE);
        getPurchasePriceDAO().persist(purchasePrice);
        getPurchasePriceDAO().flush();
        purchasePrice.setProduct(createProduct(1L, createStore(1L)));
        try {
            getPurchasePriceDAO().merge(purchasePrice);
            Assert.fail("Should have thrown a DAOException.");
        } catch (PersistenceException daoException) {
            commit = false;
            throw daoException;
        }
    }

    @Test(expected = DAOException.class)
    public void testPersistUK() throws DAOException {
        Product product = createProduct(1L, createStore(1L));
        PurchasePrice purchasePrice = build(product, BigDecimal.ONE);
        try {
            getPurchasePriceDAO().persist(purchasePrice);
            getPurchasePriceDAO().flush();
            Assert.fail("Should have thrown a DAOException.");
        } catch (DAOException daoException) {
            commit = false;
            throw daoException;
        }
    }

    @Test
    public void testRange() {
        List<PurchasePrice> catalogModels = getPurchasePriceDAO().findRange(0, 2);
        Assert.assertNotNull(catalogModels);
        Assert.assertTrue(catalogModels.size() >= 1);
    }

    @Test
    public void testUpdate() throws DAOException {
        Product product = createProduct(1L, createStore(1L));
        PurchasePrice purchasePriceTmp = getPurchasePriceDAO().find(product, BigDecimal.ONE);
        purchasePriceTmp.setProduct(createProduct(3L, createStore(1L)));
        purchasePrice = getPurchasePriceDAO().merge(purchasePriceTmp);
    }

    @Test
    public void testUpdateFind() throws DAOException {
        Product product = createProduct(3L, createStore(1L));
        PurchasePrice purchasePrice = getPurchasePriceDAO().find(product, BigDecimal.ONE);
        Model tmp = getPurchasePriceDAO().find(purchasePrice.getId());
        Assert.assertEquals(purchasePrice, tmp);
        tmp = getPurchasePriceDAO().find(purchasePrice.getId(), purchasePrice.getVersion());
        Assert.assertEquals(purchasePrice, tmp);
    }

    @Test
    public void testUpdateMerge() throws DAOException {
        purchasePrice.setQuantity(BigDecimal.TEN);
        getPurchasePriceDAO().merge(purchasePrice);
    }

    @Test
    public void testUpdateRemove() throws DAOException {
        Product product = createProduct(3L, createStore(1L));
        PurchasePrice purchasePrice = getPurchasePriceDAO().find(product, BigDecimal.TEN);
        getPurchasePriceDAO().remove(purchasePrice);
    }

    @Test
    public void testUpdateRemoveCount() {
        Long count = getPurchasePriceDAO().countInactive();
        Assert.assertTrue(count >= 1);
    }

    @Test
    public void testUpdateRemoveFind() throws DAOException {
        Product product = createProduct(3L, createStore(1L));
        PurchasePrice price = getPurchasePriceDAO().findInactive(product, BigDecimal.TEN);
        PurchasePrice tmp = getPurchasePriceDAO().find(price.getId());
        Assert.assertNull(tmp);
        tmp = getPurchasePriceDAO().find(price.getId(), price.getVersion());
        Assert.assertNull(tmp);
    }

    @Test
    public void testValidateRemove() {
        Product product = createProduct(3L, createStore(1L));
        PurchasePrice price = getPurchasePriceDAO().findInactive(product, BigDecimal.TEN);
        deleteHistory(price);
    }

    public void deleteHistory(PurchasePrice purchasePrice) {
        CriteriaBuilder cb = getEntityManager().getCriteriaBuilder();
        CriteriaQuery<PurchasePriceHistory> cq = cb.createQuery(PurchasePriceHistory.class);
        Root<SalePriceHistory> root = cq.from(SalePriceHistory.class);
        root.fetch(PurchasePriceHistory_.audit, JoinType.INNER);
        root.fetch(PurchasePriceHistory_.productPrice, JoinType.INNER);
        cq.where(cb.equal(root.get(SalePriceHistory_.productPrice), purchasePrice));
        List<PurchasePriceHistory> histories = getEntityManager().createQuery(cq).getResultList();
        deleteTenantHistories(purchasePrice, histories);
    }

    public PurchasePrice build(Product product, BigDecimal quantity) {
        PurchasePrice.Builder builder = PurchasePrice.newBuilder();
        builder.setProduct(product);
        builder.setQuantity(quantity);
        builder.setPrice(BigDecimal.ONE);
        builder.setProduct(product);
        return builder.build();
    }

    public Product createProduct(Long id, Store store) {
        Product product = new Product();
        product.setId(id);
        product.setStore(store);
        return product;
    }

    public Store createStore(Long id) {
        Store store = new Store();
        store.setId(id);
        return store;
    }

    public PurchasePriceDAO getPurchasePriceDAO() {
        if (purchasePriceDAO == null) {
            purchasePriceDAO = getApplicationContext().getBean(PurchasePriceDAO.class);
        }
        return purchasePriceDAO;
    }    
}
