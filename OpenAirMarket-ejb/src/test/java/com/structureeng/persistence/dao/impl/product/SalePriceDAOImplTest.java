// Copyright 2013 Structure Eng Inc.

package com.structureeng.persistence.dao.impl.product;

import com.structureeng.persistence.dao.DAOException;
import com.structureeng.persistence.dao.SalePriceDAO;
import com.structureeng.persistence.dao.impl.AbstractTenantModelDAOImplTest;
import com.structureeng.persistence.model.Model;
import com.structureeng.persistence.model.business.Organization;
import com.structureeng.persistence.model.history.product.SalePriceHistory;
import com.structureeng.persistence.model.history.product.SalePriceHistory_;
import com.structureeng.persistence.model.product.Product;
import com.structureeng.persistence.model.product.SalePrice;

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
public class SalePriceDAOImplTest extends
        AbstractTenantModelDAOImplTest<SalePrice, SalePriceHistory> {

    private SalePriceDAO salePriceDAO;
    private static SalePrice salePrice;

    @Test
    public void testPersistA() throws DAOException {
        Product product = createProduct(1L, createOrganization(1L));
        SalePrice salePrice = build(product, BigDecimal.ONE);
        getSalePriceDAO().merge(salePrice);
    }

    @Test
    public void testPersistB() throws DAOException {
        Product product = createProduct(2L, createOrganization(1L));
        salePrice = build(product, BigDecimal.TEN);
        getSalePriceDAO().persist(salePrice);
    }

    @Test
    public void testPersistCount() throws DAOException {
        Long count = getSalePriceDAO().count();
        Assert.assertTrue(count >= 2);
    }

    @Test(expected = DAOException.class)
    public void testPersistMerge() throws DAOException {
        salePrice.setProduct(createProduct(1L, createOrganization(1L)));
        salePrice.setQuantity(BigDecimal.ONE);
        try {
            getSalePriceDAO().merge(salePrice);
            getSalePriceDAO().flush();
            Assert.fail("Should have thrown a DAOException.");
        } catch (DAOException daoException) {
            deleteHistory(salePrice);
            throw daoException;
        }
    }

    @Test(expected = PersistenceException.class)
    public void testPersistMergeDirty() throws DAOException {
        Product product = createProduct(3L, createOrganization(1L));
        SalePrice salePrice = build(product, BigDecimal.ONE);
        getSalePriceDAO().persist(salePrice);
        getSalePriceDAO().flush();
        salePrice.setProduct(createProduct(1L, createOrganization(1L)));
        try {
            getSalePriceDAO().merge(salePrice);
            Assert.fail("Should have thrown a DAOException.");
        } catch (PersistenceException daoException) {
            commit = false;
            throw daoException;
        }
    }

    @Test(expected = DAOException.class)
    public void testPersistUK() throws DAOException {
        Product product = createProduct(1L, createOrganization(1L));
        SalePrice salePrice = build(product, BigDecimal.ONE);
        try {
            getSalePriceDAO().persist(salePrice);
            getSalePriceDAO().flush();
            Assert.fail("Should have thrown a DAOException.");
        } catch (DAOException daoException) {
            commit = false;
            throw daoException;
        }
    }

    @Test
    public void testRange() {
        List<SalePrice> catalogModels = getSalePriceDAO().findRange(0, 2);
        Assert.assertNotNull(catalogModels);
        Assert.assertTrue(catalogModels.size() >= 1);
    }

    @Test
    public void testUpdate() throws DAOException {
        Product product = createProduct(1L, createOrganization(1L));
        SalePrice salePriceTmp = getSalePriceDAO().find(product, BigDecimal.ONE);
        salePriceTmp.setProduct(createProduct(3L, createOrganization(1L)));
        salePrice = getSalePriceDAO().merge(salePriceTmp);
    }

    @Test
    public void testUpdateFind() throws DAOException {
        Product product = createProduct(3L, createOrganization(1L));
        SalePrice salePrice = getSalePriceDAO().find(product, BigDecimal.ONE);
        Model tmp = getSalePriceDAO().find(salePrice.getId());
        Assert.assertEquals(salePrice, tmp);
        tmp = getSalePriceDAO().find(salePrice.getId(), salePrice.getVersion());
        Assert.assertEquals(salePrice, tmp);
    }

    @Test
    public void testUpdateMerge() throws DAOException {
        salePrice.setQuantity(BigDecimal.TEN);
        getSalePriceDAO().merge(salePrice);
    }

    @Test
    public void testUpdateRemove() throws DAOException {
        Product product = createProduct(3L, createOrganization(1L));
        SalePrice salePrice = getSalePriceDAO().find(product, BigDecimal.TEN);
        getSalePriceDAO().remove(salePrice);
    }

    @Test
    public void testUpdateRemoveCount() {
        Long count = getSalePriceDAO().countInactive();
        Assert.assertTrue(count >= 1);
    }

    @Test
    public void testUpdateRemoveFind() throws DAOException {
        Product product = createProduct(3L, createOrganization(1L));
        SalePrice catalogModel = getSalePriceDAO().findInactive(product, BigDecimal.TEN);
        SalePrice tmp = getSalePriceDAO().find(catalogModel.getId());
        Assert.assertNull(tmp);
        tmp = getSalePriceDAO().find(catalogModel.getId(), catalogModel.getVersion());
        Assert.assertNull(tmp);
    }

    @Test
    public void testValidateRemove() {
        Product product = createProduct(3L, createOrganization(1L));
        SalePrice salePrice = getSalePriceDAO().findInactive(product, BigDecimal.TEN);
        deleteHistory(salePrice);
    }

    public void deleteHistory(SalePrice salePrice) {
        CriteriaBuilder cb = getEntityManager().getCriteriaBuilder();
        CriteriaQuery<SalePriceHistory> cq = cb.createQuery(SalePriceHistory.class);
        Root<SalePriceHistory> root = cq.from(SalePriceHistory.class);
        root.fetch(SalePriceHistory_.audit, JoinType.INNER);
        root.fetch(SalePriceHistory_.productPrice, JoinType.INNER);
        cq.where(cb.equal(root.get(SalePriceHistory_.productPrice), salePrice));
        List<SalePriceHistory> histories = getEntityManager().createQuery(cq).getResultList();
        deleteTenantHistories(salePrice, histories);
    }

    public SalePrice build(Product product, BigDecimal quantity) {
        SalePrice.Builder builder = SalePrice.newBuilder();
        builder.setProduct(product);
        builder.setQuantity(quantity);
        builder.setPrice(BigDecimal.ONE);
        builder.setProfit(BigDecimal.TEN);
        builder.setProduct(product);
        return builder.build();
    }

    public Product createProduct(Long id, Organization store) {
        Product product = new Product();
        product.setId(id);
        product.setOrganization(store);
        return product;
    }

    public Organization createOrganization(Long id) {
        Organization store = new Organization();
        store.setId(id);
        return store;
    }

    public SalePriceDAO getSalePriceDAO() {
        if (salePriceDAO == null) {
            salePriceDAO = getApplicationContext().getBean(SalePriceDAO.class);
        }
        return salePriceDAO;
    }    
}
