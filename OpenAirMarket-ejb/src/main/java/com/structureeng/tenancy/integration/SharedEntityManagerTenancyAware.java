// Copyright 2013 Structure Eng Inc.

package com.structureeng.tenancy.integration;

import org.springframework.orm.jpa.support.SharedEntityManagerBean;

import javax.inject.Provider;
import javax.persistence.EntityManager;

/**
 * A {@code EntityManager} that is tenant aware.
 *
 * @author Edgar Rico (edgar.martinez.rico@gmail.com)
 */
public class SharedEntityManagerTenancyAware extends SharedEntityManagerBean {
        
    private final Provider<EntityManager> entityManagerProvider;
    
    public SharedEntityManagerTenancyAware(Provider<EntityManager> entityManagerProvider) {
        this.entityManagerProvider = entityManagerProvider;        
    }
    
    @Override
    protected EntityManager createEntityManager() throws IllegalStateException {
        return entityManagerProvider.get();
    }
}
