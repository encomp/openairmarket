// Copyright 2013 Structure Eng Inc.

package com.structureeng.persistence.dao.impl.product;

import com.structureeng.persistence.dao.DAOException;
import com.structureeng.persistence.dao.MeasureUnitDAO;
import com.structureeng.persistence.dao.impl.CatalogDAOImpl;
import com.structureeng.persistence.model.product.MeasureUnit;
import com.structureeng.persistence.model.product.ProductDefinition_;
import com.structureeng.persistence.model.product.RetailProduct;
import com.structureeng.persistence.model.product.RetailProduct_;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.JoinType;

/**
 * Data Access Object for {@code MeasureUnit}.
 *
 * @author Edgar Rico (edgar.martinez.rico@gmail.com)
 */
public class MeasureUnitDAOImpl extends CatalogDAOImpl<MeasureUnit, Long, Integer> implements
        MeasureUnitDAO {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    @PersistenceContext
    private EntityManager entityManager;

    @Inject
    public MeasureUnitDAOImpl() {
        super(MeasureUnit.class, Long.class, Integer.class);
    }

    @Override
    protected void validateForeignKeys(MeasureUnit measureUnit) throws DAOException {
        if (measureUnit.getActive()) {
            long count = countRetailProductWithMeasureUnit(measureUnit);
            if (count > 0) {
                throw DAOException.Builder.build(ProductErrorCode.MEASURE_UNIT_FK);
            }
        }
    }

    private long countRetailProductWithMeasureUnit(MeasureUnit measureUnit) {
        QueryContainer<Long, RetailProduct> qc = newQueryContainerCount(RetailProduct.class);
        qc.getCriteriaQuery().select(qc.getCriteriaBuilder().countDistinct(qc.getRoot()));
        qc.getRoot().join(RetailProduct_.measureUnit, JoinType.INNER);
        qc.getCriteriaQuery().where(qc.getCriteriaBuilder().and(
                qc.getCriteriaBuilder().equal(
                    qc.getRoot().get(RetailProduct_.measureUnit), measureUnit),
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
