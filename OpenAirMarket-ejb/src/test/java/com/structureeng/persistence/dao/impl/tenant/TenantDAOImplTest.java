// Copyright 2013 Structure Eng Inc.

package com.structureeng.persistence.dao.impl.tenant;

import com.structureeng.persistence.dao.DAOException;
import com.structureeng.persistence.dao.TenantDAO;
import com.structureeng.persistence.model.AbstractPersistenceTest;
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

import java.util.List;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
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
@Transactional(propagation = Propagation.REQUIRES_NEW)
public class TenantDAOImplTest extends AbstractPersistenceTest {

    @Inject
    private TenantDAO tenantDAO;
    
    @PersistenceContext
    private EntityManager entityManager;
    
    private static Tenant tempTenant;

    @Test
    public void testPersist() throws DAOException {
        Tenant tenant = Tenant.newBuilder().setReferenceId(50).setName("tenant 50").build();
        tenantDAO.persist(tenant);
    }
    
    @Test
    public void testPersistCount() throws DAOException {
        Long count = tenantDAO.count();
        Assert.assertTrue(count >= 1);
    }
    
    @Test
    public void testRange() {
        List<Tenant> tenants = tenantDAO.findRange(0, 2);
        Assert.assertNotNull(tenants);
        Assert.assertTrue(tenants.size() >= 1);
    }

    @Test
    public void testUpdate() throws DAOException {
        Tenant tenant = tenantDAO.findByReferenceId(50);       
        tenant.setReferenceId(51);
        tenant.setName("test tenant 51");
        tempTenant = tenantDAO.merge(tenant);
    }
    
    @Test
    public void testUpdateFind() throws DAOException {
        Tenant tenant = tenantDAO.findByReferenceId(51);
        Tenant tmp = tenantDAO.find(tenant.getId());
        Assert.assertEquals(tenant, tmp);
        tmp = tenantDAO.find(tenant.getId(), tenant.getVersion());
        Assert.assertEquals(tenant, tmp);
    }

    @Test
    public void testUpdateMerge() throws DAOException {        
        tempTenant.setReferenceId(52);
        tempTenant.setName("test tenant 52");
        tenantDAO.merge(tempTenant);
    }

    @Test
    public void testUpdateRemove() throws DAOException {
        Tenant tenant = tenantDAO.findByReferenceId(52);
        tenantDAO.remove(tenant);
    }
    
    @Test
    public void testUpdateRemoveCount() {
        Long count = tenantDAO.countInactive();
        Assert.assertTrue(count >= 1);
    }
    
    @Test
    public void testUpdateRemoveFind() throws DAOException {
        Tenant tenant = tenantDAO.findInactiveByReferenceId(52);
        Tenant tmp = tenantDAO.find(tenant.getId());
        Assert.assertNull(tmp);
        tmp = tenantDAO.findByReferenceId(tenant.getReferenceId());
        Assert.assertNull(tmp);
        tmp = tenantDAO.find(tenant.getId(), tenant.getVersion());
        Assert.assertNull(tmp);
    }

    @Test    
    public void testValidateRemove() {
        Tenant tenant = tenantDAO.findInactiveByReferenceId(52);
        deleteHistory(tenant);
    }

    private void deleteHistory(Tenant tenant) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<TenantHistory> cq = cb.createQuery(TenantHistory.class);
        Root<TenantHistory> root = cq.from(TenantHistory.class);
        root.fetch(TenantHistory_.audit, JoinType.INNER);
        root.fetch(TenantHistory_.tenant, JoinType.INNER);
        cq.where(cb.equal(root.get(TenantHistory_.tenant), tenant));
        List<TenantHistory> tenantHistories = entityManager.createQuery(cq).getResultList();
        Query q = null;
        for (TenantHistory tenantHistory : tenantHistories) {
            q = entityManager.createQuery("DELETE FROM TenantHistory t WHERE t.id = ?1");
            q.setParameter(1, tenantHistory.getId());
            Assert.assertEquals(1, q.executeUpdate());
            q = entityManager.createQuery("DELETE FROM Audit h WHERE h.id = ?1");
            q.setParameter(1, tenantHistory.getAudit().getId());
            Assert.assertEquals(1, q.executeUpdate());            
        }
        q = entityManager.createQuery("DELETE FROM Tenant t WHERE t.id = ?1");
        q.setParameter(1, tenant.getId());
        Assert.assertEquals(1, q.executeUpdate());
    }
}
