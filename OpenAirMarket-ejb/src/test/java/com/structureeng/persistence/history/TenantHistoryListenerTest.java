//Structure Eng 2013 Copyright

package com.structureeng.persistence.history;

import com.structureeng.persistence.model.AbstractPersistenceTest;
import com.structureeng.persistence.model.history.Audit;
import com.structureeng.persistence.model.history.product.ProductManufacturerHistory;
import com.structureeng.persistence.model.history.product.ProductManufacturerHistory_;
import com.structureeng.persistence.model.product.ProductManufacturer;
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
        ProductManufacturer company = createProductManufacturer(99, "testCompany 99");
        entityManager.persist(company);
        tx.commit(transactionStatus);
    }

    @Test
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void testPostPersistValidation() {
        ProductManufacturerHistory companyHistory = 
                retrieveProductManufacturerHistory(99, JoinType.INNER);
        ProductManufacturer company = companyHistory.getProductManufacturer();
        Audit historyTenant = companyHistory.getHistory();
        assertHistory(HistoryType.CREATE, companyHistory, historyTenant, company);
        deleteCompanyHistory(company, new Long[]{companyHistory.getId()},
                new Long[]{historyTenant.getId()});
    }

    @Test
    public void testPostUpdate() {
        registerTenancyContext(createTenant(1));
        tx = applicationContext.getBean(PlatformTransactionManager.class);
        TransactionStatus transactionStatus = tx.getTransaction(null);
        ProductManufacturer company = createProductManufacturer(100, "testCompany 100");
        entityManager.persist(company);
        entityManager.flush();
        company.setReferenceId(101);
        company.setName("testCompany 101");
        tx.commit(transactionStatus);
    }

    @Test
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void testPostUpdateValidation() {
        ProductManufacturerHistory[] companyHistorys = new ProductManufacturerHistory[2];
        companyHistorys[0] = retrieveProductManufacturerHistory(100, JoinType.LEFT);
        ProductManufacturerHistory companyHistory = companyHistorys[0];
        Audit historyTenant = companyHistory.getHistory();
        assertHistory(HistoryType.CREATE, companyHistory, historyTenant);
        companyHistorys[1] = retrieveProductManufacturerHistory(101, JoinType.INNER);
        companyHistory = companyHistorys[1];
        ProductManufacturer tenant = companyHistory.getProductManufacturer();
        historyTenant = companyHistory.getHistory();
        assertHistory(HistoryType.UPDATE, companyHistory, historyTenant, tenant);
        deleteCompanyHistory(tenant, new Long[]{companyHistorys[0].getId(),
                companyHistorys[1].getId()}, new Long[]{companyHistorys[0].getHistory().getId(),
                companyHistorys[1].getHistory().getId()});
    }

    private void assertHistory(HistoryType historyType, ProductManufacturerHistory companyHistory,
            Audit historyTenant, ProductManufacturer company) {
        Assert.assertEquals(historyType, companyHistory.getHistoryType());
        Assert.assertEquals(company.getReferenceId(), companyHistory.getReferenceId());
        Assert.assertEquals(company.getName(), companyHistory.getName());
        Assert.assertNotNull(companyHistory.getVersion());
        Assert.assertNotNull(historyTenant);
        Assert.assertNotNull(historyTenant.getId());
        Assert.assertNotNull(historyTenant.getCreatedDate());
    }

    private void assertHistory(HistoryType historyType, ProductManufacturerHistory companyHistory,
            Audit historyNonTenant) {
        Assert.assertEquals(historyType, companyHistory.getHistoryType());
        Assert.assertNotNull(companyHistory.getReferenceId());
        Assert.assertNotNull(companyHistory.getName());
        Assert.assertNotNull(companyHistory.getVersion());
        Assert.assertNotNull(historyNonTenant);
        Assert.assertNotNull(historyNonTenant.getId());
        Assert.assertNotNull(historyNonTenant.getCreatedDate());
    }

    private void deleteCompanyHistory(ProductManufacturer company, Long[] companyHistory,
            Long[] historyTenant) {
        Query q = null;
        for (Long id : companyHistory) {
            q = entityManager
                    .createQuery("DELETE FROM ProductManufacturerHistory c WHERE c.id = ?1");
            q.setParameter(1, id);
            Assert.assertEquals(1, q.executeUpdate());
        }
        for (Long id : historyTenant) {
            q = entityManager.createQuery("DELETE FROM Audit h WHERE h.id = ?1");
            q.setParameter(1, id);
            Assert.assertEquals(1, q.executeUpdate());
        }
        q = entityManager.createQuery("DELETE FROM ProductManufacturer c WHERE c.id = ?1");
        q.setParameter(1, company.getId());
        Assert.assertEquals(1, q.executeUpdate());
    }

    private ProductManufacturerHistory retrieveProductManufacturerHistory(Integer referenceId, 
            JoinType tenatJoinType) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<ProductManufacturerHistory> cq = entityManager.getCriteriaBuilder()
                .createQuery(ProductManufacturerHistory.class);
        Root<ProductManufacturerHistory> root = cq.from(ProductManufacturerHistory.class);
        root.fetch(ProductManufacturerHistory_.audit, JoinType.INNER);
        root.fetch(ProductManufacturerHistory_.productManufacturer, tenatJoinType);
        cq.where(cb.equal(root.get(ProductManufacturerHistory_.referenceId), referenceId));
        return entityManager.createQuery(cq).getSingleResult();
    }

    private ProductManufacturer createProductManufacturer(int referenceId, String name) {
        return ProductManufacturer.newBuilder().setReferenceId(referenceId).setName(name).build();
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
