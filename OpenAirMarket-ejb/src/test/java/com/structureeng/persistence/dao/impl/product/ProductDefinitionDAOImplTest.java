// Copyright 2013 Structure Eng Inc.

package com.structureeng.persistence.dao.impl.product;

import com.structureeng.persistence.dao.CatalogDAO;
import com.structureeng.persistence.dao.ProductDefinitionDAO;
import com.structureeng.persistence.dao.impl.AbstractCatalogDAOImplTest;
import com.structureeng.persistence.model.Model;
import com.structureeng.persistence.model.history.product.ProductDefinitionHistory;
import com.structureeng.persistence.model.history.product.ProductDefinitionHistory_;
import com.structureeng.persistence.model.product.ProductManufacturer;
import com.structureeng.persistence.model.product.Division;
import com.structureeng.persistence.model.product.ProductDefinition;
import java.math.BigInteger;
import java.util.List;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.JoinType;
import javax.persistence.criteria.Root;

/**
 * Test for {@code ProductDefinitionDAOImpl}.
 *
 * @author Edgar Rico (edgar.martinez.rico@gmail.com)
 */
public class ProductDefinitionDAOImplTest extends AbstractCatalogDAOImplTest<Long, BigInteger, 
        ProductDefinition, ProductDefinitionHistory> {
    
    private ProductDefinitionDAO productDefinitionDAO;
    
    public ProductDefinitionDAOImplTest() {
        super(ProductDefinition.class);
    }

    @Override
    public void deleteHistory(Model model) {
        ProductDefinition productDefinition = ProductDefinition.class.cast(model);
        CriteriaBuilder cb = getEntityManager().getCriteriaBuilder();
        CriteriaQuery<ProductDefinitionHistory> cq = 
                cb.createQuery(ProductDefinitionHistory.class);
        Root<ProductDefinitionHistory> root = cq.from(ProductDefinitionHistory.class);
        root.fetch(ProductDefinitionHistory_.audit, JoinType.INNER);
        root.fetch(ProductDefinitionHistory_.productDefinition, JoinType.INNER);
        cq.where(cb.equal(
                root.get(ProductDefinitionHistory_.productDefinition), productDefinition));
        List<ProductDefinitionHistory> histories = getEntityManager()
                .createQuery(cq).getResultList();
        deleteTenantHistories(productDefinition, histories);
    }

    @Override
    public ProductDefinition build(BigInteger referenceId, String name) {
        ProductManufacturer company = new ProductManufacturer();
        Division division = new Division();
        company.setId(1L);        
        division.setId(1L);
        ProductDefinition.Buider builder = ProductDefinition.newBuilder();
        builder.setReferenceId(referenceId).setName(name).setKey(name).setProductManufacturer(company)
                .setDivision(division);
        return builder.build();
    }

    @Override
    public BigInteger toReferenceId(String referenceId) {
        return new BigInteger(referenceId);
    }

    @Override
    public CatalogDAO<ProductDefinition, Long, BigInteger> getCatalogDAO() {
        if (productDefinitionDAO == null) {
            productDefinitionDAO = getApplicationContext().getBean(ProductDefinitionDAO.class);
        }
        return productDefinitionDAO;
    }
}
