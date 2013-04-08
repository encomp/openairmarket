// Copyright 2013 Structure Eng Inc.

package com.structureeng.persistence.dao.impl.business;

import com.structureeng.persistence.dao.CatalogDAO;
import com.structureeng.persistence.dao.ProductTypeDAO;
import com.structureeng.persistence.dao.impl.AbstractCatalogDAOImplTest;
import com.structureeng.persistence.model.Model;
import com.structureeng.persistence.model.business.ProductType;
import com.structureeng.persistence.model.history.business.ProductTypeHistory;
import com.structureeng.persistence.model.history.business.ProductTypeHistory_;

import java.util.List;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.JoinType;
import javax.persistence.criteria.Root;

/**
 * Test for {@code ProductTypeDAOImpl}.
 *
 * @author Edgar Rico (edgar.martinez.rico@gmail.com)
 */
public class ProductTypeDAOImplTest extends AbstractCatalogDAOImplTest<Long, Integer, ProductType,
        ProductTypeHistory> {

    private ProductTypeDAO productTypeDAO;

    public ProductTypeDAOImplTest() {
        super(ProductType.class);
    }

    @Override
    public void deleteHistory(Model model) {
        ProductType productType = ProductType.class.cast(model);
        CriteriaBuilder cb = getEntityManager().getCriteriaBuilder();
        CriteriaQuery<ProductTypeHistory> cq = cb.createQuery(ProductTypeHistory.class);
        Root<ProductTypeHistory> root = cq.from(ProductTypeHistory.class);
        root.fetch(ProductTypeHistory_.audit, JoinType.INNER);
        root.fetch(ProductTypeHistory_.rule, JoinType.INNER);
        cq.where(cb.equal(root.get(ProductTypeHistory_.rule), productType));
        List<ProductTypeHistory> histories = getEntityManager().createQuery(cq).getResultList();
        deleteTenantHistories(productType, histories);
    }

    @Override
    public ProductType build(Integer referenceId, String name) {
        return ProductType.newBuilder().setReferenceId(referenceId).setName(name)
                .setDescription("test").build();
    }

    @Override
    public Integer toReferenceId(String referenceId) {
        return new Integer(referenceId);
    }

    @Override
    public CatalogDAO<ProductType, Long, Integer> getCatalogDAO() {
        if (productTypeDAO == null) {
            productTypeDAO = getApplicationContext().getBean(ProductTypeDAO.class);
        }
        return productTypeDAO;
    }
}
