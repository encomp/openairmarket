// Copyright 2013 Structure Eng Inc.

package com.structureeng.persistence.dao.impl.product;

import com.structureeng.persistence.dao.CatalogDAO;
import com.structureeng.persistence.dao.ProductDAO;
import com.structureeng.persistence.dao.impl.AbstractCatalogDAOImplTest;
import com.structureeng.persistence.model.Model;
import com.structureeng.persistence.model.business.ProductType;
import com.structureeng.persistence.model.business.Store;
import com.structureeng.persistence.model.business.TaxType;
import com.structureeng.persistence.model.history.product.ProductHistory;
import com.structureeng.persistence.model.history.product.ProductHistory_;
import com.structureeng.persistence.model.product.Product;
import com.structureeng.persistence.model.product.ProductDefinition;

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
public class ProductDAOImplTest extends AbstractCatalogDAOImplTest<Long, BigInteger,
        Product, ProductHistory> {

    private final Map<BigInteger, Long> productTypes;
    private ProductDAO productDAO;

    public ProductDAOImplTest() {
        super(Product.class);
        ImmutableMap.Builder<BigInteger, Long> builder = ImmutableMap.builder();
        builder.put(new BigInteger("50"), 1L);
        builder.put(new BigInteger("51"), 2L);
        builder.put(new BigInteger("55"), 3L);
        builder.put(new BigInteger("99"), 4L);
        productTypes = builder.build();
    }

    @Override
    public void deleteHistory(Model model) {
        Product product = Product.class.cast(model);
        CriteriaBuilder cb = getEntityManager().getCriteriaBuilder();
        CriteriaQuery<ProductHistory> cq = cb.createQuery(ProductHistory.class);
        Root<ProductHistory> root = cq.from(ProductHistory.class);
        root.fetch(ProductHistory_.audit, JoinType.INNER);
        root.fetch(ProductHistory_.product, JoinType.INNER);
        cq.where(cb.equal(root.get(ProductHistory_.product), product));
        List<ProductHistory> histories = getEntityManager().createQuery(cq).getResultList();
        deleteTenantHistories(product, histories);
    }

    @Override
    public Product build(BigInteger referenceId, String name) {
        Store store = new Store();
        store.setId(1L);
        ProductType productType = new ProductType();
        productType.setId(productTypes.get(referenceId));
        TaxType taxType = new TaxType();
        taxType.setId(5L);
        ProductDefinition productDefinition = new ProductDefinition();
        productDefinition.setId(1L);
        Product.Buider buider = Product.newBuilder();
        buider.setReferenceId(referenceId).setName(name).setAutoStock(Boolean.TRUE)
              .setWastable(Boolean.TRUE).setQuantity(BigDecimal.ONE).setCost(BigDecimal.TEN)
              .setLastCost(BigDecimal.TEN).setProductDefinition(productDefinition)
              .setStore(store).setProductType(productType).setTaxType(taxType);
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
