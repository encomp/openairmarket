// Copyright 2013 Structure Eng Inc.

package com.structureeng.persistence.dao.impl.product;

import com.structureeng.persistence.dao.CatalogDAO;
import com.structureeng.persistence.dao.ProductMeasureUnitDAO;
import com.structureeng.persistence.dao.impl.AbstractCatalogDAOImplTest;
import com.structureeng.persistence.model.Model;
import com.structureeng.persistence.model.history.product.ProductMeasureUnitHistory;
import com.structureeng.persistence.model.history.product.ProductMeasureUnitHistory_;
import com.structureeng.persistence.model.product.ProductMeasureUnit;

import java.util.List;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.JoinType;
import javax.persistence.criteria.Root;

/**
 * Test for {@code ProductMeasureUnitDAO}.
 *
 * @author Edgar Rico (edgar.martinez.rico@gmail.com)
 */
public class ProductMeasureUnitDAOImplTest extends AbstractCatalogDAOImplTest<Long, Integer,
        ProductMeasureUnit, ProductMeasureUnitHistory> {

    private ProductMeasureUnitDAO packageDAO;

    public ProductMeasureUnitDAOImplTest() {
        super(ProductMeasureUnit.class);
    }

    @Override
    public void deleteHistory(Model model) {
        ProductMeasureUnit measureUnit = ProductMeasureUnit.class.cast(model);
        CriteriaBuilder cb = getEntityManager().getCriteriaBuilder();
        CriteriaQuery<ProductMeasureUnitHistory> cq = 
                cb.createQuery(ProductMeasureUnitHistory.class);
        Root<ProductMeasureUnitHistory> root = cq.from(ProductMeasureUnitHistory.class);
        root.fetch(ProductMeasureUnitHistory_.audit, JoinType.INNER);
        root.fetch(ProductMeasureUnitHistory_.productMeasureUnit, JoinType.INNER);
        cq.where(cb.equal(root.get(ProductMeasureUnitHistory_.productMeasureUnit), measureUnit));
        List<ProductMeasureUnitHistory> histories = 
                getEntityManager().createQuery(cq).getResultList();
        deleteTenantHistories(measureUnit, histories);
    }

    @Override
    public ProductMeasureUnit build(Integer referenceId, String name) {
        return ProductMeasureUnit.newBuilder().setReferenceId(referenceId).setName(name)
                .setCountable(Boolean.TRUE).setExpire(Boolean.FALSE).build();
    }

    @Override
    public Integer toReferenceId(String referenceId) {
        return new Integer(referenceId);
    }

    @Override
    public CatalogDAO<ProductMeasureUnit, Long, Integer> getCatalogDAO() {
        if (packageDAO == null) {
            packageDAO = getApplicationContext().getBean(ProductMeasureUnitDAO.class);
        }
        return packageDAO;
    }
}
