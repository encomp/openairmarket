// Copyright 2013 Structure Eng Inc.

package com.structureeng.persistence.dao.impl;

import com.structureeng.persistence.model.AbstractPersistenceTest;
import com.structureeng.persistence.model.TenantModel;
import com.structureeng.persistence.model.history.AbstractTenantHistoryModel;
import com.structureeng.persistence.model.tenant.Tenant;
import com.structureeng.tenancy.context.TenancyContext;
import com.structureeng.tenancy.context.TenancyContextHolder;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionStatus;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.Query;

/**
 * Specifies the behavior for the {@code TenantModel} data access layer.
 *
 * @author Edgar Rico (edgar.martinez.rico@gmail.com)
 */
public abstract class AbstractTenantModelDAOImplTest extends AbstractPersistenceTest {
    
    private EntityManager entityManager;    
    private PlatformTransactionManager tx;
    private TransactionStatus transactionStatus;
    protected boolean commit = true;
    
    @Before
    @Override
    public void setup() {
        super.setup();
        registerTenancyContext(createTenant(1));
        tx = getApplicationContext().getBean(PlatformTransactionManager.class);
        transactionStatus = tx.getTransaction(null);
    }
    
    @After
    public void shutdown() {
        if (commit) {
            tx.commit(transactionStatus);
        } else {
            tx.rollback(transactionStatus);
        }
    }
    
    private Tenant createTenant(long id) {
        Tenant tenant = new Tenant();
        tenant.setId(id);
        return tenant;
    }

    private void registerTenancyContext(Tenant tenant) {
        TenancyContextHolder.registerTenancyContext(new TenancyContext(tenant));
    }
    
    protected void deleteTenantHistories(TenantModel tenantModel, 
            List<AbstractTenantHistoryModel> tenantHistories) {
        Query q = null;
        for (AbstractTenantHistoryModel historyTenant:  tenantHistories) {
            q = getEntityManager().createQuery(String.format("DELETE FROM %s a WHERE a.id = ?1", 
                    getClassName(historyTenant)));
            q.setParameter(1, historyTenant.getId());
            Assert.assertEquals(1, q.executeUpdate());
            q = getEntityManager().createQuery(String.format("DELETE FROM %s ht WHERE ht.id = ?1", 
                    getClassName(historyTenant.getHistoryTenant())));
            q.setParameter(1, historyTenant.getHistoryTenant().getId());
            Assert.assertEquals(1, q.executeUpdate());            
        }
        q = getEntityManager().createQuery(String.format("DELETE FROM %s a WHERE a.id = ?1", 
                getClassName(tenantModel)));
        q.setParameter(1, tenantModel.getId());
        Assert.assertEquals(1, q.executeUpdate());
    }
    
    private String getClassName(Object object) {
        String[] clase = object.getClass().getName().split("\\.");        
        return clase[clase.length - 1];
    }
        
    public EntityManager getEntityManager() {
        if (entityManager == null) {
            entityManager = getApplicationContext().getBean(EntityManager.class);
        }
        return entityManager;
    }
}
