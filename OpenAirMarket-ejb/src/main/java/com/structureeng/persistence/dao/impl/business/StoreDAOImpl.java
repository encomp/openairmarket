// Copyright 2013 Structure Eng Inc.

package com.structureeng.persistence.dao.impl.business;

import com.structureeng.persistence.dao.DAOException;
import com.structureeng.persistence.dao.StoreDAO;
import com.structureeng.persistence.dao.impl.CatalogDAOImpl;
import com.structureeng.persistence.model.business.Store;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 * Data Access Object for {@code Store}.
 *
 * @author Edgar Rico (edgar.martinez.rico@gmail.com)
 */
public class StoreDAOImpl extends CatalogDAOImpl<Store, Long, Integer> implements StoreDAO {
    
    private final Logger logger = LoggerFactory.getLogger(getClass());

    @PersistenceContext
    private EntityManager entityManager;

    @Inject
    public StoreDAOImpl() {
        super(Store.class, Long.class, Integer.class);
    }

    @Override
    protected void validateForeignKeys(Store entity) throws DAOException {        
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
