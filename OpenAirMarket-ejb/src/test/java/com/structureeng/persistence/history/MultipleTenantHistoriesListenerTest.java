//Structure Eng 2013 Copyright

package com.structureeng.persistence.history;

import com.structureeng.persistence.model.AbstractPersistenceTest;
import com.structureeng.persistence.model.history.HistoryTenant;
import com.structureeng.persistence.model.history.product.CompanyHistory;
import com.structureeng.persistence.model.history.product.CompanyHistory_;
import com.structureeng.persistence.model.history.product.DivisionHistory;
import com.structureeng.persistence.model.history.product.DivisionHistory_;
import com.structureeng.persistence.model.product.Company;
import com.structureeng.persistence.model.product.Division;
import com.structureeng.persistence.model.tenant.Tenant;
import com.structureeng.tenancy.context.TenancyContext;
import com.structureeng.tenancy.context.TenancyContextHolder;

import org.junit.Assert;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;
import org.springframework.context.ApplicationContext;
import org.springframework.test.context.transaction.TransactionConfiguration;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.JoinType;
import javax.persistence.criteria.Root;

/**
 * Tests multiple tenant entities being changed within the same transaction for 
 * {@code HistoryListener}.
 *
 * @author Edgar Rico (edgar.martinez.rico@gmail.com)
 */
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
@TransactionConfiguration(transactionManager = "transactionManager", defaultRollback = false)
public class MultipleTenantHistoriesListenerTest extends AbstractPersistenceTest {
    
    @PersistenceContext
    private EntityManager entityManager;

    @Inject
    private ApplicationContext applicationContext;

    private PlatformTransactionManager tx;
    
    @Test
    public void testPostPersist() {
        registerTenancyContext(createTenant(1));
        tx = applicationContext.getBean(PlatformTransactionManager.class);
        TransactionStatus transactionStatus = tx.getTransaction(null);
        Company company = createCompany(99, "testCompany 99");
        Division division = createDivision(99, "testDivision 99");
        entityManager.persist(company);
        entityManager.persist(division);
        tx.commit(transactionStatus);
    }

    @Test
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void testPostPersistValidation() {
        CompanyHistory companyHistory = retrieveCompanyHistory(99, JoinType.INNER);
        DivisionHistory divisionHistory = retrieveDivisionHistory(99, JoinType.INNER);
        Company company = companyHistory.getCompany();
        Division division = divisionHistory.getDivision();
        HistoryTenant historyTenant = companyHistory.getHistory();
        assertCompanyHistory(HistoryType.CREATE, companyHistory, historyTenant, company);
        assertDivisionHistory(HistoryType.CREATE, divisionHistory, historyTenant, division);
        deleteCompanyHistory(company, companyHistory.getId());
        deleteDivisionHistory(division, divisionHistory.getId());
        deleteTenantHistory(historyTenant.getId());
    }
            
    private void assertCompanyHistory(HistoryType historyType, CompanyHistory companyHistory,
            HistoryTenant historyTenant, Company company) {
        Assert.assertEquals(historyType, companyHistory.getHistoryType());
        Assert.assertEquals(company.getReferenceId(), companyHistory.getReferenceId());
        Assert.assertEquals(company.getName(), companyHistory.getName());
        Assert.assertEquals(company.getVersion(), companyHistory.getRevision());
        Assert.assertNotNull(historyTenant);
        Assert.assertNotNull(historyTenant.getId());
        Assert.assertNotNull(historyTenant.getCreatedDate());
    }
    
    private void assertDivisionHistory(HistoryType historyType, DivisionHistory divisionHistory,
            HistoryTenant historyTenant, Division division) {
        Assert.assertEquals(historyType, divisionHistory.getHistoryType());
        Assert.assertEquals(division.getReferenceId(), divisionHistory.getReferenceId());
        Assert.assertEquals(division.getName(), divisionHistory.getName());
        Assert.assertEquals(division.getVersion(), divisionHistory.getRevision());
        Assert.assertNotNull(historyTenant);
        Assert.assertNotNull(historyTenant.getId());
        Assert.assertNotNull(historyTenant.getCreatedDate());
    }
            
    private CompanyHistory retrieveCompanyHistory(Integer referenceId, JoinType tenatJoinType) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<CompanyHistory> cq = entityManager.getCriteriaBuilder()
                .createQuery(CompanyHistory.class);
        Root<CompanyHistory> root = cq.from(CompanyHistory.class);
        root.fetch(CompanyHistory_.historyTenant, JoinType.INNER);
        root.fetch(CompanyHistory_.company, tenatJoinType);
        cq.where(cb.equal(root.get(CompanyHistory_.referenceId), referenceId));
        return entityManager.createQuery(cq).getSingleResult();
    }
    
    private DivisionHistory retrieveDivisionHistory(Integer referenceId, JoinType tenatJoinType) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<DivisionHistory> cq = entityManager.getCriteriaBuilder()
                .createQuery(DivisionHistory.class);
        Root<DivisionHistory> root = cq.from(DivisionHistory.class);
        root.fetch(DivisionHistory_.historyTenant, JoinType.INNER);
        root.fetch(DivisionHistory_.division, tenatJoinType);
        cq.where(cb.equal(root.get(DivisionHistory_.referenceId), referenceId));
        return entityManager.createQuery(cq).getSingleResult();
    }
    
    private void deleteDivisionHistory(Division division, Long... divisionHistory) {
        Query q = null;
        for (Long id : divisionHistory) {
            q = entityManager.createQuery("DELETE FROM DivisionHistory c WHERE c.id = ?1");
            q.setParameter(1, id);
            Assert.assertEquals(1, q.executeUpdate());
        }        
        q = entityManager.createQuery("DELETE FROM Division c WHERE c.id = ?1");
        q.setParameter(1, division.getId());
        Assert.assertEquals(1, q.executeUpdate());
    }
    
    private void deleteCompanyHistory(Company company, Long... companyHistory) {
        Query q = null;
        for (Long id : companyHistory) {
            q = entityManager.createQuery("DELETE FROM CompanyHistory c WHERE c.id = ?1");
            q.setParameter(1, id);
            Assert.assertEquals(1, q.executeUpdate());
        }        
        q = entityManager.createQuery("DELETE FROM Company c WHERE c.id = ?1");
        q.setParameter(1, company.getId());
        Assert.assertEquals(1, q.executeUpdate());
    }
    
    private void deleteTenantHistory(Long... historyTenant) {         
        for (Long id : historyTenant) {
            Query q = entityManager.createQuery("DELETE FROM HistoryTenant h WHERE h.id = ?1");
            q.setParameter(1, id);
            Assert.assertEquals(1, q.executeUpdate());
        }
    }
    
    private Division createDivision(int referenceId, String name) {
        return Division.newBuilder().setReferenceId(referenceId).setName(name).build();
    }
    
    private Company createCompany(int referenceId, String name) {
        return Company.newBuilder().setReferenceId(referenceId).setName(name).build();
    }

    private Tenant createTenant(int id) {
        Tenant tenant = new Tenant();
        tenant.setId(id);
        return tenant;
    }

    private void registerTenancyContext(Tenant tenant) {
        TenancyContextHolder.registerTenancyContext(new TenancyContext(tenant));
    }
}
