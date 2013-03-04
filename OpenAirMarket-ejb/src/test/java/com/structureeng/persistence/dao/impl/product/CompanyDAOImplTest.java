// Copyright 2013 Structure Eng Inc.
package com.structureeng.persistence.dao.impl.product;

import com.structureeng.common.ErrorCode;
import com.structureeng.persistence.dao.CompanyDAO;
import com.structureeng.persistence.dao.DAOException;
import com.structureeng.persistence.dao.impl.AbstractTenantModelDAOImplTest;
import com.structureeng.persistence.model.history.AbstractTenantHistoryModel;
import com.structureeng.persistence.model.history.product.CompanyHistory;
import com.structureeng.persistence.model.history.product.CompanyHistory_;
import com.structureeng.persistence.model.product.Company;

import com.google.common.collect.Lists;

import org.junit.Assert;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;
import org.springframework.test.context.transaction.TransactionConfiguration;

import java.util.List;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.JoinType;
import javax.persistence.criteria.Root;

/**
 * Test for {@code TenantDAOImpl}.
 *
 * @author Edgar Rico (edgar.martinez.rico@gmail.com)
 */
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
@TransactionConfiguration(transactionManager = "transactionManager", defaultRollback = false)
public class CompanyDAOImplTest extends AbstractTenantModelDAOImplTest {

    private static Company tempCompany;
    private CompanyDAO companyDAO;

    @Test
    public void testPersistA() throws DAOException {
        Company company = Company.newBuilder().setReferenceId(50).setName("test 50").build();
        getCompanyDAO().persist(company);
    }

    @Test
    public void testPersistB() throws DAOException {
        tempCompany = Company.newBuilder().setReferenceId(55).setName("test 55").build();
        getCompanyDAO().persist(tempCompany);
    }

    @Test
    public void testPersistCount() throws DAOException {
        Long count = getCompanyDAO().count();
        Assert.assertTrue(count >= 2);
    }

    @Test(expected = DAOException.class)
    public void testPersistMerge() throws DAOException {
        tempCompany.setReferenceId(50);
        try {
            getCompanyDAO().merge(tempCompany);
            Assert.fail("Should have thrown a DAOException.");
        } catch (DAOException daoException) {
            Assert.assertNotNull(daoException.getErrorCode());
        }
        tempCompany.setReferenceId(55);
        tempCompany.setName("test 50");
        try {
            getCompanyDAO().merge(tempCompany);
            Assert.fail("Should have thrown a DAOException.");
        } catch (DAOException daoException) {
            Assert.assertNotNull(daoException.getErrorCode());
            deleteHistory(tempCompany);
            throw daoException;
        }
    }

    @Test(expected = DAOException.class)
    public void testPersistMergeDirty() throws DAOException {
        Company company = Company.newBuilder().setReferenceId(51).setName("test 51").build();
        getCompanyDAO().persist(company);
        getCompanyDAO().flush();
        company.setReferenceId(50);
        try {
            getCompanyDAO().merge(company);
            Assert.fail("Should have thrown a DAOException.");
        } catch (DAOException daoException) {
            Assert.assertNotNull(daoException.getErrorCode());
            commit = false;
            throw daoException;
        }
    }

    @Test(expected = DAOException.class)
    public void testPersistUK() throws DAOException {
        Company company = Company.newBuilder().setReferenceId(50).setName("test 50").build();
        try {
            getCompanyDAO().persist(company);
        } catch (DAOException daoException) {
            Assert.assertNotNull(daoException.getErrorCode());
            throw daoException;
        }
    }

    @Test(expected = DAOException.class)
    public void testPersistUKName() throws DAOException {
        Company company = Company.newBuilder().setReferenceId(99).setName("test 50").build();
        try {
            getCompanyDAO().persist(company);
        } catch (DAOException daoException) {
            Assert.assertNotNull(daoException.getErrorCode());
            throw daoException;
        }
    }

    @Test(expected = DAOException.class)
    public void testPersistUKReferenceId() throws DAOException {
        Company company = Company.newBuilder().setReferenceId(50).setName("test 99").build();
        try {
            getCompanyDAO().persist(company);
        } catch (DAOException daoException) {
            ErrorCode errorCode = daoException.getErrorCode();
            Assert.assertNotNull(errorCode);
            throw daoException;
        }
    }

    @Test
    public void testRange() {
        List<Company> companies = getCompanyDAO().findRange(0, 2);
        Assert.assertNotNull(companies);
        Assert.assertTrue(companies.size() >= 1);
    }

    @Test
    public void testUpdate() throws DAOException {
        Company company = getCompanyDAO().findByReferenceId(50);
        company.setReferenceId(51);
        company.setName("test 51");
        tempCompany = getCompanyDAO().merge(company);
    }

    @Test
    public void testUpdateFind() throws DAOException {
        Company company = getCompanyDAO().findByReferenceId(51);
        Company tmp = getCompanyDAO().find(company.getId());
        Assert.assertEquals(company, tmp);
        tmp = getCompanyDAO().find(company.getId(), company.getVersion());
        Assert.assertEquals(company, tmp);
    }

    @Test
    public void testUpdateMerge() throws DAOException {
        tempCompany.setReferenceId(52);
        tempCompany.setName("test 52");
        getCompanyDAO().merge(tempCompany);
    }

    @Test
    public void testUpdateRemove() throws DAOException {
        Company company = getCompanyDAO().findByReferenceId(52);
        getCompanyDAO().remove(company);
    }

    @Test
    public void testUpdateRemoveCount() {
        Long count = getCompanyDAO().countInactive();
        Assert.assertTrue(count >= 1);
    }

    @Test
    public void testUpdateRemoveFind() throws DAOException {
        Company company = getCompanyDAO().findInactiveByReferenceId(52);
        Company tmp = getCompanyDAO().find(company.getId());
        Assert.assertNull(tmp);
        tmp = getCompanyDAO().findByReferenceId(company.getReferenceId());
        Assert.assertNull(tmp);
        tmp = getCompanyDAO().find(company.getId(), company.getVersion());
        Assert.assertNull(tmp);
    }

    @Test
    public void testValidateRemove() {
        Company company = getCompanyDAO().findInactiveByReferenceId(52);
        deleteHistory(company);
    }

    private void deleteHistory(Company company) {
        CriteriaBuilder cb = getEntityManager().getCriteriaBuilder();
        CriteriaQuery<CompanyHistory> cq = cb.createQuery(CompanyHistory.class);
        Root<CompanyHistory> root = cq.from(CompanyHistory.class);
        root.fetch(CompanyHistory_.historyTenant, JoinType.INNER);
        root.fetch(CompanyHistory_.company, JoinType.INNER);
        cq.where(cb.equal(root.get(CompanyHistory_.company), company));
        List<CompanyHistory> histories = getEntityManager().createQuery(cq).getResultList();
        List<AbstractTenantHistoryModel> test = Lists.newArrayList();
        test.addAll(histories);
        deleteTenantHistories(company, test);
    }

    public CompanyDAO getCompanyDAO() {
        if (companyDAO == null) {
            companyDAO = getApplicationContext().getBean(CompanyDAO.class);
        }
        return companyDAO;
    }
}
