// Copyright 2013 Structure Eng Inc.

package com.structureeng.tenancy.context;

import com.structureeng.persistence.model.tenant.Tenant;

import com.google.common.cache.Cache;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

/**
 * Test for class {@code ThreadLocalTenancyContextHolder}.
 *
 * @author Edgar Rico (edgar.martinez.rico@gmail.com)
 */
public class ThreadLocalTenancyContextHolderTest {
    
    private Cache<Long, TenancyContext> cache;
    private ThreadLocalTenancyContextHolder contextHolder;

    @Before
    public void setUp() {
        contextHolder = new ThreadLocalTenancyContextHolder();
        TenancyContextHolder.setStrategy(contextHolder);                
    }
    
    @Test
    public void testRetrieveTenancyContext() {
        TenancyContextHolder.registerTenancyContext(createTenancyContext(2));
        TenancyContext tenancyContext = TenancyContextHolder.getCurrentTenancyContext();
        Assert.assertEquals(createTenancyContext(2), tenancyContext);
    }
    
    @Test
    public void testClearCurrentTenancyHolder() {
        TenancyContextHolder.registerTenancyContext(createTenancyContext(5));
        TenancyContextHolder.clearCurrentTenancyContext();
        TenancyContext tenancyContext = TenancyContextHolder.getCurrentTenancyContext();        
        Assert.assertNull(tenancyContext);
    }

    private TenancyContext createTenancyContext(Integer tenantId) {        
        Tenant tenant = new Tenant();
        tenant.setId(tenantId);        
        return new TenancyContext(tenant);
    }
}
