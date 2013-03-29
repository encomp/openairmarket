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

import java.util.List;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.JoinType;
import javax.persistence.criteria.Root;

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
            CriteriaBuilder cb = getEntityManager().getCriteriaBuilder();
            CriteriaQuery<ProductDefinition> cq = cb.createQuery(ProductDefinition.class);
            Root<ProductDefinition> root = cq.from(ProductDefinition.class);
            root.join(ProductDefinition_.company, JoinType.INNER);
            cq.where(cb.and(
                    cb.equal(root.get(ProductDefinition_.company), company),
                    cb.equal(root.get(ProductDefinition_.active), Boolean.TRUE)));
            List<ProductDefinition> entities = getEntityManager().createQuery(cq).getResultList();
            if (entities != null && entities.size() > 0) {
                throw DAOException.Builder.build(ProductErrorCode.COMPANY_FK);
            }
        }
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
