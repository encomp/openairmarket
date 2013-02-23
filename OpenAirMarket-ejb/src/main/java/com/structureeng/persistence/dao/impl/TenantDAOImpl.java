// Copyright 2013 Structure Eng Inc.

package com.structureeng.persistence.dao.impl;

import com.structureeng.persistence.dao.TenantDAO;
import com.structureeng.persistence.model.tenant.Tenant;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 * Data Access Object for {@code Tenant}.
 * 
 * @author Edgar Rico (edgar.martinez.rico@gmail.com)
 */
public class TenantDAOImpl extends DAOImpl<Tenant, Long> implements TenantDAO {

    private final Logger logger = LoggerFactory.getLogger(getClass());
    
    @PersistenceContext
    private EntityManager entityManager;

    @Inject
    public TenantDAOImpl() {
        super(Tenant.class, Long.class);
    }

    @Override
    protected EntityManager getEntityManager() {
        return entityManager;
    }

    @Override
    public Logger getLogger() {
        return logger;
    }
}
