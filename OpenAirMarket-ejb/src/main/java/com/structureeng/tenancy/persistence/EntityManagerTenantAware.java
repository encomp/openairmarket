// Copyright 2013 Structure Eng Inc.

package com.structureeng.tenancy.persistence;

import com.structureeng.tenancy.context.TenancyContext;
import com.structureeng.tenancy.context.TenancyContextHolder;

import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableMap;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

/**
 * A {@code EntityManager} that is tenant aware.
 *
 * @author Edgar Rico (edgar.martinez.rico@gmail.com)
 */
public class EntityManagerTenantAware {

    private static final ThreadLocal<EntityManager> ENTITY_MANAGER_HOLDER = 
            new ThreadLocal<EntityManager>();
    private static String tenantContext = "eclipselink.tenant-id";
    private static EntityManagerFactory entityManagerFactory;

    @Inject
    public static void setEntityManagerFactory(EntityManagerFactory entityManagerFactory) {
        EntityManagerTenantAware.entityManagerFactory =
                Preconditions.checkNotNull(entityManagerFactory);
    }

    @Inject
    public static void setTenantContext(String tenantContext) {
        EntityManagerTenantAware.tenantContext = Preconditions.checkNotNull(tenantContext);
    }

    public static EntityManager get() {
        EntityManager entityManager = ENTITY_MANAGER_HOLDER.get();
        if (entityManager == null) {
            TenancyContext tenancyContext = TenancyContextHolder.getCurrentTenancyContext();
            if (tenancyContext != null) {
                final ImmutableMap.Builder<String, Object> immBuilder = ImmutableMap.builder();
                immBuilder.put(tenantContext, tenancyContext.getTenant().getId());
                entityManager = entityManagerFactory.createEntityManager(immBuilder.build());
            } else {
                entityManager = entityManagerFactory.createEntityManager();
            }
            ENTITY_MANAGER_HOLDER.set(entityManager);
        }
        return entityManager;
    }

    public static void clear() {
        ENTITY_MANAGER_HOLDER.set(null);
    }
}
