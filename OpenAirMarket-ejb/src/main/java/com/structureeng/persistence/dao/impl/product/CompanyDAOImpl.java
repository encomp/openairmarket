// Copyright 2013 Structure Eng Inc.

package com.structureeng.persistence.dao.impl.product;

import com.structureeng.persistence.dao.CompanyDAO;
import com.structureeng.persistence.dao.DAOException;
import com.structureeng.persistence.dao.impl.CatalogDAOImpl;
import com.structureeng.persistence.model.product.Company;
import com.structureeng.persistence.model.product.ProductDefinition;
import com.structureeng.persistence.model.product.ProductDefinition_;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.JoinType;

/**
 * Data Access Object for {@code Company}.
 *
 * @author Edgar Rico (edgar.martinez.rico@gmail.com)
 */
public class CompanyDAOImpl extends CatalogDAOImpl<Company, Long, Integer> implements CompanyDAO {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    @PersistenceContext
    private EntityManager entityManager;

    @Inject
    public CompanyDAOImpl() {
        super(Company.class, Long.class, Integer.class);
    }

    @Override
    protected void validateForeignKeys(Company company) throws DAOException {
        if (company.getActive()) {
            long count = countProductDefinitionWithCompany(company);
            if (count > 0) {
                throw DAOException.Builder.build(ProductErrorCode.COMPANY_FK);
            }
        }
    }

    private long countProductDefinitionWithCompany(final Company company) {
        QueryContainer<Long, ProductDefinition> qc =
                new QueryContainer<Long, ProductDefinition>(Long.class, ProductDefinition.class);
        qc.getCriteriaQuery().select(qc.getCriteriaBuilder().countDistinct(qc.getRoot()));
        qc.getRoot().join(ProductDefinition_.company, JoinType.INNER);
        qc.getCriteriaQuery().where(qc.getCriteriaBuilder().and(
                qc.getCriteriaBuilder()
                    .equal(qc.getRoot().get(ProductDefinition_.company), company),
                qc.getCriteriaBuilder()
                    .equal(qc.getRoot().get(ProductDefinition_.active), Boolean.TRUE)));
        return qc.getSingleResult();
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
