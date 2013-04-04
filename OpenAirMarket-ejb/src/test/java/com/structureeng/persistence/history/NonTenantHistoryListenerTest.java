//Structure Eng 2013 Copyright

package com.structureeng.persistence.history;

import com.structureeng.persistence.model.AbstractPersistenceTest;
import com.structureeng.persistence.model.history.Audit;
import com.structureeng.persistence.model.history.tenant.TenantHistory;
import com.structureeng.persistence.model.history.tenant.TenantHistory_;
import com.structureeng.persistence.model.tenant.Tenant;

import org.junit.Assert;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;
import org.springframework.test.context.transaction.TransactionConfiguration;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

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
@Transactional(propagation = Propagation.REQUIRES_NEW)
public class NonTenantHistoryListenerTest extends AbstractPersistenceTest {

    @PersistenceContext
    private EntityManager entityManager;

    @Test
    public void testSetApplicationContext() {
        Assert.assertNotNull(HistoryListener.getApplicationContext());
    }
    
    @Test
    public void testPostPersist() {
        Tenant tenant = createTenant(99, "tenat99");
        entityManager.persist(tenant);
    }

    @Test
    public void testPostPersistValidation() {
        TenantHistory tenantHistory = retrieveTenantHistory(99, JoinType.INNER);
        Tenant tenant = tenantHistory.getTenant();
        Audit historyNonTenant = tenantHistory.getHistory();
        assertHistory(HistoryType.CREATE, tenantHistory, historyNonTenant, tenant);
        deleteTenantHistory(tenant, new Long[]{tenantHistory.getId()},
                new Long[]{historyNonTenant.getId()});
    }

    @Test
    public void testPostUpdate() {
        Tenant tenant = createTenant(100, "tenat100");
        entityManager.persist(tenant);
        entityManager.flush();
        tenant.setReferenceId(101);
        tenant.setName("tenant101");
    }

    @Test
    public void testPostUpdateValidation() {
        TenantHistory[] tenantHistorys = new TenantHistory[2];
        tenantHistorys[0] = retrieveTenantHistory(100, JoinType.LEFT);
        TenantHistory tenantHistory = tenantHistorys[0];
        Audit historyNonTenant = tenantHistory.getHistory();
        assertHistory(HistoryType.CREATE, tenantHistory, historyNonTenant);
        tenantHistorys[1] = retrieveTenantHistory(101, JoinType.INNER);
        tenantHistory = tenantHistorys[1];
        Tenant tenant = tenantHistory.getTenant();
        historyNonTenant = tenantHistory.getHistory();
        assertHistory(HistoryType.UPDATE, tenantHistory, historyNonTenant, tenant);
        deleteTenantHistory(tenant, new Long[]{tenantHistorys[0].getId(),
                    tenantHistorys[1].getId()}, new Long[]{tenantHistorys[0].getHistory().getId(),
                    tenantHistorys[1].getHistory().getId()});
    }

    private void assertHistory(HistoryType historyType, TenantHistory tenantHistory,
            Audit historyNonTenant, Tenant tenant) {
        Assert.assertEquals(historyType, tenantHistory.getHistoryType());
        Assert.assertEquals(tenant.getReferenceId(), tenantHistory.getReferenceId());
        Assert.assertEquals(tenant.getName(), tenantHistory.getName());
        Assert.assertNotNull(tenantHistory.getVersion());
        Assert.assertNotNull(historyNonTenant);
        Assert.assertNotNull(historyNonTenant.getId());
        Assert.assertNotNull(historyNonTenant.getCreatedDate());
    }

    private void assertHistory(HistoryType historyType, TenantHistory tenantHistory,
            Audit historyNonTenant) {
        Assert.assertEquals(historyType, tenantHistory.getHistoryType());
        Assert.assertNotNull(tenantHistory.getReferenceId());
        Assert.assertNotNull(tenantHistory.getName());
        Assert.assertNotNull(tenantHistory.getVersion());
        Assert.assertNotNull(historyNonTenant);
        Assert.assertNotNull(historyNonTenant.getId());
        Assert.assertNotNull(historyNonTenant.getCreatedDate());
    }

    private TenantHistory retrieveTenantHistory(Integer referenceId, JoinType tenatJoinType) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<TenantHistory> cq = entityManager.getCriteriaBuilder()
                .createQuery(TenantHistory.class);
        Root<TenantHistory> root = cq.from(TenantHistory.class);
        root.fetch(TenantHistory_.tenant, tenatJoinType);
        cq.where(cb.equal(root.get(TenantHistory_.referenceId), referenceId));
        return entityManager.createQuery(cq).getSingleResult();
    }

    private void deleteTenantHistory(Tenant tenant, Long[] tenantHistory,
            Long[] historyNonTenant) {
        Query q = null;
        for (Long id : tenantHistory) {
            q = entityManager.createQuery("DELETE FROM TenantHistory t WHERE t.id = ?1");
            q.setParameter(1, id);
            Assert.assertEquals(1, q.executeUpdate());
        }
        for (Long id : historyNonTenant) {
            q = entityManager.createQuery("DELETE FROM Audit h WHERE h.id = ?1");
            q.setParameter(1, id);
            Assert.assertEquals(1, q.executeUpdate());
        }
        q = entityManager.createQuery("DELETE FROM Tenant t WHERE t.id = ?1");
        q.setParameter(1, tenant.getId());
        Assert.assertEquals(1, q.executeUpdate());
    }

    private Tenant createTenant(Integer refId, String name) {
        return Tenant.newBuilder().setReferenceId(refId).setName(name).build();
    }
}
