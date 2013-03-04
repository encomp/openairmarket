// Copyright 2013 Structure Eng Inc.

package com.structureeng.persistence.dao.impl;

import com.structureeng.common.ErrorCode;
import com.structureeng.persistence.dao.CatalogDAO;
import com.structureeng.persistence.dao.DAOException;
import com.structureeng.persistence.model.AbstractCatalogModel;
import com.structureeng.persistence.model.Model;

import org.junit.Assert;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;
import org.springframework.test.context.transaction.TransactionConfiguration;

import java.io.Serializable;
import java.util.List;

/**
 * Test for {@code CatalogDAO}.
 *
 * @author Edgar Rico (edgar.martinez.rico@gmail.com)
 */
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
@TransactionConfiguration(transactionManager = "transactionManager", defaultRollback = false)
public abstract class AbstractCatalogDAOImplTest<S extends Serializable, 
        T extends AbstractCatalogModel<S>> extends AbstractTenantModelDAOImplTest {
    
    private static AbstractCatalogModel tempCompany;
    private final Class<T> clase;
    
    public AbstractCatalogDAOImplTest(Class<T> clase) {
        this.clase = clase;
    }
    
    @Test
    public void testPersistA() throws DAOException {
        T catalogModel = build(50, "test 50");
        getCatalogDAO().persist(clase.cast(catalogModel));
    }

    @Test
    public void testPersistB() throws DAOException {
        tempCompany = build(55, "test 55");
        getCatalogDAO().persist(clase.cast(tempCompany));
    }

    @Test
    public void testPersistCount() throws DAOException {
        Long count = getCatalogDAO().count();
        Assert.assertTrue(count >= 2);
    }
    
    @Test(expected = DAOException.class)
    public void testPersistMerge() throws DAOException {
        tempCompany.setReferenceId(50);
        try {
            getCatalogDAO().merge(clase.cast(tempCompany));
            Assert.fail("Should have thrown a DAOException.");
        } catch (DAOException daoException) {
            Assert.assertNotNull(daoException.getErrorCode());
        }
        tempCompany.setReferenceId(55);
        tempCompany.setName("test 50");
        try {
            getCatalogDAO().merge(clase.cast(tempCompany));
            Assert.fail("Should have thrown a DAOException.");
        } catch (DAOException daoException) {
            Assert.assertNotNull(daoException.getErrorCode());
            deleteHistory(tempCompany);
            throw daoException;
        }
    }

    @Test(expected = DAOException.class)
    public void testPersistMergeDirty() throws DAOException {
        T catalogModel = build(51, "test 51");
        getCatalogDAO().persist(catalogModel);
        getCatalogDAO().flush();
        catalogModel.setReferenceId(50);
        try {
            getCatalogDAO().merge(catalogModel);
            Assert.fail("Should have thrown a DAOException.");
        } catch (DAOException daoException) {
            Assert.assertNotNull(daoException.getErrorCode());
            commit = false;
            throw daoException;
        }
    }
    
    @Test(expected = DAOException.class)
    public void testPersistUK() throws DAOException {
        T catalogModel = build(50, "test 50");
        try {
            getCatalogDAO().persist(catalogModel);
        } catch (DAOException daoException) {
            Assert.assertNotNull(daoException.getErrorCode());
            throw daoException;
        }
    }

    @Test(expected = DAOException.class)
    public void testPersistUKName() throws DAOException {
        T catalogModel = build(99, "test 50");
        try {
            getCatalogDAO().persist(catalogModel);
        } catch (DAOException daoException) {
            Assert.assertNotNull(daoException.getErrorCode());
            throw daoException;
        }
    }

    @Test(expected = DAOException.class)
    public void testPersistUKReferenceId() throws DAOException {
        T catalogModel = build(50, "test 99");
        try {
            getCatalogDAO().persist(catalogModel);
        } catch (DAOException daoException) {
            ErrorCode errorCode = daoException.getErrorCode();
            Assert.assertNotNull(errorCode);
            throw daoException;
        }
    }

    @Test
    public void testRange() {
        List<T> companies = getCatalogDAO().findRange(0, 2);
        Assert.assertNotNull(companies);
        Assert.assertTrue(companies.size() >= 1);
    }

    @Test
    public void testUpdate() throws DAOException {
        T company = getCatalogDAO().findByReferenceId(50);
        company.setReferenceId(51);
        company.setName("test 51");
        tempCompany = getCatalogDAO().merge(company);
    }

    @Test
    public void testUpdateFind() throws DAOException {
        T company = getCatalogDAO().findByReferenceId(51);
        Model tmp = getCatalogDAO().find(company.getId());
        Assert.assertEquals(company, tmp);
        tmp = getCatalogDAO().find(company.getId(), company.getVersion());
        Assert.assertEquals(company, tmp);
    }

    @Test
    public void testUpdateMerge() throws DAOException {
        tempCompany.setReferenceId(52);
        tempCompany.setName("test 52");
        getCatalogDAO().merge(clase.cast(tempCompany));
    }

    @Test
    public void testUpdateRemove() throws DAOException {
        T company = getCatalogDAO().findByReferenceId(52);
        getCatalogDAO().remove(company);
    }

    @Test
    public void testUpdateRemoveCount() {
        Long count = getCatalogDAO().countInactive();
        Assert.assertTrue(count >= 1);
    }

    @Test
    public void testUpdateRemoveFind() throws DAOException {
        T company = getCatalogDAO().findInactiveByReferenceId(52);
        T tmp = getCatalogDAO().find(company.getId());
        Assert.assertNull(tmp);
        tmp = getCatalogDAO().findByReferenceId(company.getReferenceId());
        Assert.assertNull(tmp);
        tmp = getCatalogDAO().find(company.getId(), company.getVersion());
        Assert.assertNull(tmp);
    }

    @Test
    public void testValidateRemove() {
        AbstractCatalogModel company = getCatalogDAO().findInactiveByReferenceId(52);
        deleteHistory(company);
    }
    
    public abstract void deleteHistory(Model company);
    
    public abstract T build(Integer referenceId, String name);
    
    public abstract CatalogDAO<T,S> getCatalogDAO();
}
