//Structure Eng 2013 Copyright

package com.structureeng.persistence.history;

import com.structureeng.persistence.model.AbstractPersistenceTest;
import com.structureeng.persistence.model.history.HistoryTenant;
import com.structureeng.persistence.model.history.product.CompanyHistory;
import com.structureeng.persistence.model.history.product.CompanyHistory_;
import com.structureeng.persistence.model.product.Company;
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
 * Tests for {@code HistoryListener}.
 *
 * @author Edgar Rico (edgar.martinez.rico@gmail.com)
 */
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
@TransactionConfiguration(transactionManager = "transactionManager", defaultRollback = false)
public class TenantHistoryListenerTest extends AbstractPersistenceTest {

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
        entityManager.persist(company);
        tx.commit(transactionStatus);
    }

    @Test
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void testPostPersistValidation() {
        CompanyHistory companyHistory = retrieveCompanyHistory(99, JoinType.INNER);
        Company company = companyHistory.getCompany();
        HistoryTenant historyTenant = companyHistory.getHistory();
        assertHistory(HistoryType.CREATE, companyHistory, historyTenant, company);
        deleteTenantHistory(company, new Long[]{companyHistory.getId()},
                new Long[]{historyTenant.getId()});
    }

    @Test
    public void testPostUpdate() {
        registerTenancyContext(createTenant(1));
        tx = applicationContext.getBean(PlatformTransactionManager.class);
        TransactionStatus transactionStatus = tx.getTransaction(null);
        Company company = createCompany(100, "testCompany 100");
        entityManager.persist(company);
        entityManager.flush();
        company.setReferenceId(101);
        company.setName("testCompany 101");
        tx.commit(transactionStatus);
    }

    @Test
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void testPostUpdateValidation() {
        CompanyHistory[] companyHistorys = new CompanyHistory[2];
        companyHistorys[0] = retrieveCompanyHistory(100, JoinType.LEFT);
        CompanyHistory companyHistory = companyHistorys[0];
        HistoryTenant historyTenant = companyHistory.getHistory();
        assertHistory(HistoryType.CREATE, companyHistory, historyTenant);
        companyHistorys[1] = retrieveCompanyHistory(101, JoinType.INNER);
        companyHistory = companyHistorys[1];
        Company tenant = companyHistory.getCompany();
        historyTenant = companyHistory.getHistory();
        assertHistory(HistoryType.UPDATE, companyHistory, historyTenant, tenant);
        deleteTenantHistory(tenant, new Long[]{companyHistorys[0].getId(),
                companyHistorys[1].getId()}, new Long[]{companyHistorys[0].getHistory().getId(),
                companyHistorys[1].getHistory().getId()});
    }

    private void assertHistory(HistoryType historyType, CompanyHistory companyHistory,
            HistoryTenant historyTenant, Company company) {
        Assert.assertEquals(historyType, companyHistory.getHistoryType());
        Assert.assertEquals(company.getReferenceId(), companyHistory.getReferenceId());
        Assert.assertEquals(company.getName(), companyHistory.getName());
        Assert.assertEquals(company.getVersion(), companyHistory.getRevision());
        Assert.assertNotNull(historyTenant);
        Assert.assertNotNull(historyTenant.getId());
        Assert.assertNotNull(historyTenant.getCreatedDate());
    }

    private void assertHistory(HistoryType historyType, CompanyHistory companyHistory,
            HistoryTenant historyNonTenant) {
        Assert.assertEquals(historyType, companyHistory.getHistoryType());
        Assert.assertNotNull(companyHistory.getReferenceId());
        Assert.assertNotNull(companyHistory.getName());
        Assert.assertNotNull(companyHistory.getRevision());
        Assert.assertNotNull(historyNonTenant);
        Assert.assertNotNull(historyNonTenant.getId());
        Assert.assertNotNull(historyNonTenant.getCreatedDate());
    }

    private void deleteTenantHistory(Company company, Long[] companyHistory,
            Long[] historyTenant) {
        Query q = null;
        for (Long id : companyHistory) {
            q = entityManager.createQuery("DELETE FROM CompanyHistory c WHERE c.id = ?1");
            q.setParameter(1, id);
            Assert.assertEquals(1, q.executeUpdate());
        }
        for (Long id : historyTenant) {
            q = entityManager.createQuery("DELETE FROM HistoryTenant h WHERE h.id = ?1");
            q.setParameter(1, id);
            Assert.assertEquals(1, q.executeUpdate());
        }
        q = entityManager.createQuery("DELETE FROM Company c WHERE c.id = ?1");
        q.setParameter(1, company.getId());
        Assert.assertEquals(1, q.executeUpdate());
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

    private Company createCompany(int referenceId, String name) {
        return Company.newBuilder().setReferenceId(referenceId).setName(name).build();
    }

    private Tenant createTenant(long id) {
        Tenant tenant = new Tenant();
        tenant.setId(id);
        return tenant;
    }

    private void registerTenancyContext(Tenant tenant) {
        TenancyContextHolder.registerTenancyContext(new TenancyContext(tenant));
    }
}
