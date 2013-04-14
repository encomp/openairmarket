// Copyright 2013 Structure Eng Inc.

package com.structureeng.persistence.dao.impl.product;

import com.structureeng.persistence.dao.CatalogDAO;
import com.structureeng.persistence.dao.ProductDAO;
import com.structureeng.persistence.dao.impl.AbstractCatalogDAOImplTest;
import com.structureeng.persistence.model.Model;
import com.structureeng.persistence.model.business.ProductType;
import com.structureeng.persistence.model.business.TaxType;
import com.structureeng.persistence.model.history.product.ProductHistory;
import com.structureeng.persistence.model.product.Product;
import com.structureeng.persistence.model.product.ProductDefinition;

import java.math.BigDecimal;
import java.math.BigInteger;

/**
 * Test for {@code ProductDAOImpl}.
 *
 * @author Edgar Rico (edgar.martinez.rico@gmail.com)
 */
public class ProductDAOImplTest extends AbstractCatalogDAOImplTest<Long, BigInteger,
        Product, ProductHistory> {

    private ProductDAO productDAO;

    public ProductDAOImplTest() {
        super(Product.class);
    }

    @Override
    public void deleteHistory(Model model) {
    }

    @Override
    public Product build(BigInteger referenceId, String name) {
        ProductType productType = new ProductType();
        productType.setId(1L);
        TaxType taxType = new TaxType();
        taxType.setId(2L);
        ProductDefinition productDefinition = new ProductDefinition();
        productDefinition.setId(1L);
        Product.Buider buider = Product.newBuilder();
        buider.setReferenceId(referenceId).setName(name).setAutoStock(Boolean.TRUE)
              .setWastable(Boolean.TRUE).setQuantity(BigDecimal.ONE).setCost(BigDecimal.TEN)
              .setLastCost(BigDecimal.TEN).setProductDefinition(productDefinition)
              .setProductType(productType).setTaxType(taxType);
        return buider.build();
    }

    @Override
    public BigInteger toReferenceId(String referenceId) {
        return new BigInteger(referenceId);
    }

    @Override
    public CatalogDAO<Product, Long, BigInteger> getCatalogDAO() {
        if (productDAO == null) {
            productDAO = getApplicationContext().getBean(ProductDAO.class);
        }
        return productDAO;
    }
}
