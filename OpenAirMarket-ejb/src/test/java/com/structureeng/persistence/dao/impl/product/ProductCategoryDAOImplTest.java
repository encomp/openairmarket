// Copyright 2013 Structure Eng Inc.

package com.structureeng.persistence.dao.impl.product;

import com.structureeng.persistence.dao.CatalogDAO;
import com.structureeng.persistence.dao.ProductCategoryDAO;
import com.structureeng.persistence.dao.impl.AbstractCatalogDAOImplTest;
import com.structureeng.persistence.model.Model;
import com.structureeng.persistence.model.history.product.ProductCategoryHistory;
import com.structureeng.persistence.model.history.product.ProductCategoryHistory_;
import com.structureeng.persistence.model.product.ProductCategory;

import java.util.List;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.JoinType;
import javax.persistence.criteria.Root;

/**
 * Test for {@code ProductCategoryDAOImpl}.
 *
 * @author Edgar Rico (edgar.martinez.rico@gmail.com)
 */
public class ProductCategoryDAOImplTest extends AbstractCatalogDAOImplTest<Long, Integer,
        ProductCategory, ProductCategoryHistory> {

    private ProductCategoryDAO productCategoryDAO;

    public ProductCategoryDAOImplTest() {
        super(ProductCategory.class);
    }

    @Override
    public void deleteHistory(Model model) {
        ProductCategory division = ProductCategory.class.cast(model);
        CriteriaBuilder cb = getEntityManager().getCriteriaBuilder();
        CriteriaQuery<ProductCategoryHistory> cq = cb.createQuery(ProductCategoryHistory.class);
        Root<ProductCategoryHistory> root = cq.from(ProductCategoryHistory.class);
        root.fetch(ProductCategoryHistory_.audit, JoinType.INNER);
        root.fetch(ProductCategoryHistory_.productCategory, JoinType.INNER);
        cq.where(cb.equal(root.get(ProductCategoryHistory_.productCategory), division));
        List<ProductCategoryHistory> histories =
                getEntityManager().createQuery(cq).getResultList();
        deleteTenantHistories(division, histories);
    }

    @Override
    public ProductCategory build(Integer referenceId, String name) {
        return ProductCategory.newBuilder().setReferenceId(referenceId).setName(name).build();
    }

    @Override
    public Integer toReferenceId(String referenceId) {
        return new Integer(referenceId);
    }

    @Override
    public CatalogDAO<ProductCategory, Long, Integer> getCatalogDAO() {
        if (productCategoryDAO == null) {
            productCategoryDAO = getApplicationContext().getBean(ProductCategoryDAO.class);
        }
        return productCategoryDAO;
    }
}
