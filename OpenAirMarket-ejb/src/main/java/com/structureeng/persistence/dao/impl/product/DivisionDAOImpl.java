// Copyright 2013 Structure Eng Inc.

package com.structureeng.persistence.dao.impl.product;

import com.structureeng.persistence.dao.DAOException;
import com.structureeng.persistence.dao.DivisionDAO;
import com.structureeng.persistence.dao.impl.CatalogDAOImpl;
import com.structureeng.persistence.model.product.Division;
import com.structureeng.persistence.model.product.ProductDefinition;
import com.structureeng.persistence.model.product.ProductDefinition_;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.JoinType;

/**
 * Data Access Object for {@code Division}.
 *
 * @author Edgar Rico (edgar.martinez.rico@gmail.com)
 */
public class DivisionDAOImpl extends CatalogDAOImpl<Division, Long, Integer>
    implements DivisionDAO {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    @PersistenceContext
    private EntityManager entityManager;

    @Inject
    public DivisionDAOImpl() {
        super(Division.class, Long.class, Integer.class);
    }

    @Override
    protected void validateForeignKeys(Division division) throws DAOException {
        if (division.getActive()) {
            long count = countProductDefinitionWithDivision(division);
            if (count > 0) {
                throw DAOException.Builder.build(ProductErrorCode.DIVISION_FK);
            }
        }
    }

    private long countProductDefinitionWithDivision(Division division) {
        QueryContainer<Long, ProductDefinition> qc = 
                newQueryContainerCount(ProductDefinition.class);
        qc.getCriteriaQuery().select(qc.getCriteriaBuilder().countDistinct(qc.getRoot()));
        qc.getRoot().join(ProductDefinition_.division, JoinType.INNER);
        qc.getCriteriaQuery().where(qc.getCriteriaBuilder().and(
                qc.getCriteriaBuilder().equal(
                    qc.getRoot().get(ProductDefinition_.division), division),
                qc.getCriteriaBuilder().equal(
                    qc.getRoot().get(ProductDefinition_.active), Boolean.TRUE)));
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
