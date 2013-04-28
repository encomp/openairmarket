// Copyright 2013 Structure Eng Inc.

package com.structureeng.persistence.dao.impl.product;

import com.structureeng.persistence.dao.CatalogDAO;
import com.structureeng.persistence.dao.ProductManufacturerDAO;
import com.structureeng.persistence.dao.impl.AbstractCatalogDAOImplTest;
import com.structureeng.persistence.model.Model;
import com.structureeng.persistence.model.history.product.ProductManufacturerHistory;
import com.structureeng.persistence.model.history.product.ProductManufacturerHistory_;
import com.structureeng.persistence.model.product.ProductManufacturer;

import java.util.List;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.JoinType;
import javax.persistence.criteria.Root;

/**
 * Test for {@code ProductManufacturerDAOImpl}.
 *
 * @author Edgar Rico (edgar.martinez.rico@gmail.com)
 */
public class ProductManufacturerDAOImplTest extends
       AbstractCatalogDAOImplTest<Long, Integer, ProductManufacturer, ProductManufacturerHistory> {

    private ProductManufacturerDAO companyDAO;

    public ProductManufacturerDAOImplTest() {
        super(ProductManufacturer.class);
    }

    @Override
    public void deleteHistory(Model model) {
        ProductManufacturer company = ProductManufacturer.class.cast(model);
        CriteriaBuilder cb = getEntityManager().getCriteriaBuilder();
        CriteriaQuery<ProductManufacturerHistory> cq = cb.createQuery(ProductManufacturerHistory.class);
        Root<ProductManufacturerHistory> root = cq.from(ProductManufacturerHistory.class);
        root.fetch(ProductManufacturerHistory_.audit, JoinType.INNER);
        root.fetch(ProductManufacturerHistory_.productManufacturer, JoinType.INNER);
        cq.where(cb.equal(root.get(ProductManufacturerHistory_.productManufacturer), company));
        List<ProductManufacturerHistory> histories = getEntityManager().createQuery(cq).getResultList();
        deleteTenantHistories(company, histories);
    }

    @Override
    public ProductManufacturer build(Integer referenceId, String name) {
        return ProductManufacturer.newBuilder().setReferenceId(referenceId).setName(name).build();
    }

    @Override
    public Integer toReferenceId(String referenceId) {
        return new Integer(referenceId);
    }

    @Override
    public CatalogDAO<ProductManufacturer, Long, Integer> getCatalogDAO() {
        if (companyDAO == null) {
            companyDAO = getApplicationContext().getBean(ProductManufacturerDAO.class);
        }
        return companyDAO;
    }
}
