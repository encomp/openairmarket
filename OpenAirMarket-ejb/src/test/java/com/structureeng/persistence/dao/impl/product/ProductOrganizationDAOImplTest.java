// Copyright 2013 Structure Eng Inc.

package com.structureeng.persistence.dao.impl.product;

import com.structureeng.persistence.dao.CatalogDAO;
import com.structureeng.persistence.dao.ProductOrganizationDAO;
import com.structureeng.persistence.dao.impl.AbstractCatalogDAOImplTest;
import com.structureeng.persistence.model.Model;
import com.structureeng.persistence.model.business.Organization;
import com.structureeng.persistence.model.business.TaxCategory;
import com.structureeng.persistence.model.history.product.ProductOrganizationHistory;
import com.structureeng.persistence.model.product.ProductMeasureUnit;
import com.structureeng.persistence.model.product.ProductOrganization;
import com.structureeng.persistence.model.product.Product;
import com.structureeng.persistence.model.product.ProductType;

import com.google.common.collect.ImmutableMap;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.List;
import java.util.Map;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.JoinType;
import javax.persistence.criteria.Root;

/**
 * Test for {@code ProductDAOImpl}.
 *
 * @author Edgar Rico (edgar.martinez.rico@gmail.com)
 */
public class ProductOrganizationDAOImplTest { 
/**
extends AbstractCatalogDAOImplTest<Long, BigInteger,
        ProductOrganization, ProductOrganizationHistory> {

    private final Map<BigInteger, Long> productTypes;
    private ProductOrganizationDAO productDAO;

    public ProductOrganizationDAOImplTest() {
        super(ProductOrganization.class);
        ImmutableMap.Builder<BigInteger, Long> builder = ImmutableMap.builder();
        builder.put(new BigInteger("50"), 1L);
        builder.put(new BigInteger("51"), 2L);
        builder.put(new BigInteger("55"), 3L);
        builder.put(new BigInteger("99"), 4L);
        productTypes = builder.build();
    }

    @Override
    public void deleteHistory(Model model) {
        ProductOrganization product = ProductOrganization.class.cast(model);
        CriteriaBuilder cb = getEntityManager().getCriteriaBuilder();
        CriteriaQuery<ProductOrganizationHistory> cq = cb.createQuery(ProductOrganizationHistory.class);
        Root<ProductOrganizationHistory> root = cq.from(ProductOrganizationHistory.class);
        root.fetch(ProductHistory_.audit, JoinType.INNER);
        root.fetch(ProductHistory_.product, JoinType.INNER);
        cq.where(cb.equal(root.get(ProductHistory_.product), product));
        List<ProductOrganizationHistory> histories = getEntityManager().createQuery(cq).getResultList();
        deleteTenantHistories(product, histories);
    }

    @Override
    public ProductOrganization build(BigInteger referenceId, String name) {
        Organization organization = new Organization();
        organization.setId(2L);
        ProductType productType = new ProductType();
        productType.setId(productTypes.get(referenceId));
        TaxCategory taxType = new TaxCategory();
        taxType.setId(5L);
        ProductMeasureUnit measureUnit = new ProductMeasureUnit();
        measureUnit.setId(1L);
        Product productDefinition = new Product();
        productDefinition.setId(1L);
        ProductOrganization.Builder buider = ProductOrganization.newBuilder();
        buider.setReferenceId(referenceId).setName(name).setAutoStock(Boolean.TRUE)
              .setWastable(Boolean.TRUE).setQuantity(BigDecimal.ONE)
              .setProductDefinition(productDefinition).setOrganization(organization)
              .setProductType(productType).setTaxType(taxType).setProductMeasureUnit(measureUnit);
        return buider.build();
    }

    @Override
    public BigInteger toReferenceId(String referenceId) {
        return new BigInteger(referenceId);
    }

    @Override
    public CatalogDAO<ProductOrganization, Long, BigInteger> getCatalogDAO() {
        if (productDAO == null) {
            productDAO = getApplicationContext().getBean(ProductOrganizationDAO.class);
        }
        return productDAO;
    }
    * **/
}
