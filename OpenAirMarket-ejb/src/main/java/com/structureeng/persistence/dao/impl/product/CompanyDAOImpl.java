// Copyright 2013 Structure Eng Inc.

package com.structureeng.persistence.dao.impl.product;

import com.structureeng.persistence.dao.CompanyDAO;
import com.structureeng.persistence.dao.DAOException;
import com.structureeng.persistence.dao.impl.CatalogDAOImpl;
import com.structureeng.persistence.model.product.Company;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 * Data Access Object for {@code Company}.
 * 
 * @author Edgar Rico (edgar.martinez.rico@gmail.com)
 */
public class CompanyDAOImpl extends CatalogDAOImpl<Company, Long> implements CompanyDAO {
    
    private final Logger logger = LoggerFactory.getLogger(getClass());
    
    @PersistenceContext
    private EntityManager entityManager;

    @Inject
    public CompanyDAOImpl() {
        super(Company.class, Long.class);
    }
    
    @Override
    protected void validateForeignKeys() throws DAOException {        
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
