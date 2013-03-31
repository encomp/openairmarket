// Copyright 2013 Structure Eng Inc.

package com.structureeng.persistence.dao.impl.product;

import com.structureeng.persistence.dao.DAOException;
import com.structureeng.persistence.dao.PackageDAO;
import com.structureeng.persistence.dao.impl.CatalogDAOImpl;
import com.structureeng.persistence.model.product.Package;
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
 * Data Access Object for {@code Package}.
 *
 * @author Edgar Rico (edgar.martinez.rico@gmail.com)
 */
public class PackageDAOImpl extends CatalogDAOImpl<Package, Long, Integer> implements PackageDAO {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    @PersistenceContext
    private EntityManager entityManager;

    @Inject
    public PackageDAOImpl() {
        super(Package.class, Long.class, Integer.class);
    }

    @Override
    protected void validateForeignKeys(Package aPackage) throws DAOException {
        if (aPackage.getActive()) {
            long count = countRetailProductWithPackage(aPackage);
            if (count > 0) {
                throw DAOException.Builder.build(ProductErrorCode.DIVISION_FK);
            }
        }
    }

    private long countRetailProductWithPackage(Package aPackage) {
        QueryContainer<Long, RetailProduct> qc =
                new QueryContainer<Long, RetailProduct>(Long.class, RetailProduct.class);
        qc.getCriteriaQuery().select(qc.getCriteriaBuilder().countDistinct(qc.getRoot()));
        qc.getRoot().join(RetailProduct_.aPackage, JoinType.INNER);
        qc.getCriteriaQuery().where(qc.getCriteriaBuilder().and(
                qc.getCriteriaBuilder().equal(
                    qc.getRoot().get(RetailProduct_.aPackage), aPackage),
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
