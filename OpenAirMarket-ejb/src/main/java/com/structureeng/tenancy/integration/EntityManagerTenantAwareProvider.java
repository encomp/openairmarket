// Copyright 2013 Structure Eng Inc.

package com.structureeng.tenancy.integration;

import com.structureeng.tenancy.context.TenancyContext;
import com.structureeng.tenancy.context.TenancyContextHolder;

import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.orm.jpa.EntityManagerFactoryInfo;
import org.springframework.util.CollectionUtils;

import java.util.Map;

import javax.inject.Provider;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

/**
 * A {@code EntityManager} that is tenant aware.
 *
 * @author Edgar Rico (edgar.martinez.rico@gmail.com)
 */
public class EntityManagerTenantAwareProvider implements Provider<EntityManager> {
    
    private static final String MULTITENANT = "eclipselink.tenant-id";
    private final Logger logger = LoggerFactory.getLogger(Provider.class);    
    private final EntityManagerFactory entityManagerFactory;
    private final Map<String, Object> jpaPropertyMap;
    
    public EntityManagerTenantAwareProvider(EntityManagerFactory entityManagerFactory, 
            Map<String, Object> jpaPropertyMap) {
        this.entityManagerFactory = Preconditions.checkNotNull(entityManagerFactory);
        this.jpaPropertyMap = jpaPropertyMap;
    }

    @Override
    public EntityManager get() {
        logger.debug("About to create a new Entity Manager instance.");
        EntityManagerFactory emf = getEntityManagerFactory();
        if (emf instanceof EntityManagerFactoryInfo) {
            emf = ((EntityManagerFactoryInfo) emf).getNativeEntityManagerFactory();
        }
        Map<String, Object> properties = getJpaPropertyMap();
        TenancyContext tenancyContext = TenancyContextHolder.getCurrentTenancyContext();
        if (tenancyContext != null) {
            logger.debug("Found tenant [" + tenancyContext + ']');
            ImmutableMap.Builder<String, Object> builder = ImmutableMap.builder();
            if (!CollectionUtils.isEmpty(properties)) {
                builder.putAll(getJpaPropertyMap());
            }
            builder.put(MULTITENANT, tenancyContext.getTenant().getId());
            properties = builder.build();
        }
        logger.debug("Creating a new Entity Manager from properties [" + properties + ']');
        return (!CollectionUtils.isEmpty(properties)
                ? emf.createEntityManager(properties) : emf.createEntityManager());
    }
    
    public EntityManagerFactory getEntityManagerFactory() {
        return entityManagerFactory;
    }
    
    public Map<String, Object> getJpaPropertyMap() {
        return jpaPropertyMap;
    }
}
