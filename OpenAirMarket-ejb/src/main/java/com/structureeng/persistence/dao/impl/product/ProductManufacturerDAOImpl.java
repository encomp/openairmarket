// Copyright 2013 Structure Eng Inc.

package com.structureeng.persistence.dao.impl.product;

import static com.google.common.base.Preconditions.checkNotNull;

import com.structureeng.persistence.dao.ProductManufacturerDAO;
import com.structureeng.persistence.dao.DAOException;
import com.structureeng.persistence.dao.QueryContainer;
import com.structureeng.persistence.dao.impl.CatalogDAOImpl;
import com.structureeng.persistence.model.product.ProductManufacturer;
import com.structureeng.persistence.model.product.Product;
import com.structureeng.persistence.model.product.Product_;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.LockModeType;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.JoinType;

/**
 * Data Access Object for {@code ProductManufacturer}.
 *
 * @author Edgar Rico (edgar.martinez.rico@gmail.com)
 */
public final class ProductManufacturerDAOImpl implements ProductManufacturerDAO {

    private EntityManager entityManager;
    private final CatalogDAOImpl<ProductManufacturer, Long, Integer> catalogDAO;
    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Inject
    public ProductManufacturerDAOImpl() {
        catalogDAO =
                new CatalogDAOImpl<ProductManufacturer, Long, Integer> (ProductManufacturer.class,
                Long.class, Integer.class);
    }

    @Override
    public void persist(ProductManufacturer entity) throws DAOException {
        catalogDAO.persist(entity);
    }

    @Override
    public ProductManufacturer merge(ProductManufacturer entity) throws DAOException {
        return catalogDAO.merge(entity);
    }

    @Override
    public void remove(ProductManufacturer entity) throws DAOException {
        if (entity.getActive()) {
            long count = countProductsWithManufacturer(entity);
            if (count > 0) {
                throw DAOException.Builder.build(ProductErrorCode.PRODUCT_MANUFACTURER_FK);
            }
        }
        catalogDAO.remove(entity);
    }

    @Override
    public void refresh(ProductManufacturer entity) {
        catalogDAO.refresh(entity);
    }

    @Override
    public void refresh(ProductManufacturer entity, LockModeType modeType) {
        catalogDAO.refresh(entity, modeType);
    }

    @Override
    public ProductManufacturer find(Long id) {
        return catalogDAO.find(id);
    }

    @Override
    public ProductManufacturer find(Long id, long version) throws DAOException {
        return catalogDAO.find(id, version);
    }

    @Override
    public ProductManufacturer findByReferenceId(Integer referenceId) {
        return catalogDAO.findByReferenceId(referenceId);
    }

    @Override
    public ProductManufacturer findInactiveByReferenceId(Integer referenceId) {
        return catalogDAO.findInactiveByReferenceId(referenceId);
    }

    @Override
    public List<ProductManufacturer> findRange(int start, int count) {
        return catalogDAO.findRange(start, count);
    }

    @Override
    public long count() {
        return catalogDAO.count();
    }

    @Override
    public long countInactive() {
        return catalogDAO.countInactive();
    }

    @Override
    public void flush() {
        catalogDAO.flush();
    }

    @Override
    public boolean hasVersionChanged(ProductManufacturer entity) throws DAOException {
        return catalogDAO.hasVersionChanged(entity);
    }

    private long countProductsWithManufacturer(final ProductManufacturer pm) {
        QueryContainer<Long, Product> qc =
                QueryContainer.newQueryContainerCount(getEntityManager(), Product.class);
        qc.getRoot().join(Product_.productManufacturer, JoinType.INNER);
        qc.getCriteriaQuery().where(qc.getCriteriaBuilder().and(
                qc.getCriteriaBuilder()
                    .equal(qc.getRoot().get(Product_.productManufacturer), pm),
                qc.activeEntities(qc.getRoot())));
        return qc.getSingleResult();
    }

    @PersistenceContext
    public void setEntityManager(EntityManager entityManager) {
        this.entityManager = checkNotNull(entityManager);
        catalogDAO.setEntityManager(entityManager);
    }

    /**
     * Provides the {@code EntityManager} that is being use by the dao.
     *
     * @return - the instance
     */
    public EntityManager getEntityManager() {
        return entityManager;
    }

    /**
     * Provides the {@code Logger} of the concrete class.
     *
     * @return - the logger instance of the class.
     */
    public Logger getLogger() {
        return logger;
    }
}
