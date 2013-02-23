// Copyright 2013 Structure Eng Inc.

package com.structureeng.tenancy.integration;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.orm.jpa.JpaTransactionManager;

import javax.inject.Provider;
import javax.persistence.EntityManager;

/**
 * A {@code EntityManager} that is tenant aware.
 *
 * @author Edgar Rico (edgar.martinez.rico@gmail.com)
 */
public class JpaTransactionManagerTenantAware extends JpaTransactionManager {

    private final Logger logger = LoggerFactory.getLogger(getClass());
    private final Provider<EntityManager> entityManagerProvider;
    
    public JpaTransactionManagerTenantAware(Provider<EntityManager> entityManagerProvider) {
        this.entityManagerProvider = entityManagerProvider;
    }

    @Override
    protected EntityManager createEntityManagerForTransaction() {        
        return entityManagerProvider.get();
    }
}
