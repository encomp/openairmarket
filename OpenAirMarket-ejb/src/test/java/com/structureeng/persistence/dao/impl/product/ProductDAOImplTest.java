// Copyright 2013 Structure Eng Inc.

package com.structureeng.persistence.dao.impl.product;

import com.structureeng.persistence.dao.CatalogDAO;
import com.structureeng.persistence.dao.ProductDAO;
import com.structureeng.persistence.dao.impl.AbstractCatalogDAOImplTest;
import com.structureeng.persistence.model.Model;
import com.structureeng.persistence.model.history.product.ProductHistory;
import com.structureeng.persistence.model.history.product.ProductHistory_;
import com.structureeng.persistence.model.product.ProductManufacturer;
import com.structureeng.persistence.model.product.ProductCategory;
import com.structureeng.persistence.model.product.Product;
import java.math.BigInteger;
import java.util.List;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.JoinType;
import javax.persistence.criteria.Root;

/**
 * Test for {@code ProductDAOImpl}.
 *
 * @author Edgar Rico (edgar.martinez.rico@gmail.com)
 */
public class ProductDAOImplTest extends AbstractCatalogDAOImplTest<Long, String,
        Product, ProductHistory> {

    private ProductDAO productDAO;

    public ProductDAOImplTest() {
        super(Product.class);
    }

    @Override
    public void deleteHistory(Model model) {
        Product productDefinition = Product.class.cast(model);
        CriteriaBuilder cb = getEntityManager().getCriteriaBuilder();
        CriteriaQuery<ProductHistory> cq =
                cb.createQuery(ProductHistory.class);
        Root<ProductHistory> root = cq.from(ProductHistory.class);
        root.fetch(ProductHistory_.audit, JoinType.INNER);
        root.fetch(ProductHistory_.product, JoinType.INNER);
        cq.where(cb.equal(
                root.get(ProductHistory_.product), productDefinition));
        List<ProductHistory> histories = getEntityManager()
                .createQuery(cq).getResultList();
        deleteTenantHistories(productDefinition, histories);
    }

    @Override
    public Product build(String referenceId, String name) {
        ProductManufacturer productManufacturer = new ProductManufacturer();
        ProductCategory productCategory = new ProductCategory();
        productManufacturer.setId(1L);
        productCategory.setId(1L);
        Product.Buider builder = Product.newBuilder();
        builder.setReferenceId(referenceId).setName(name)
                .setProductManufacturer(productManufacturer);
        return builder.build();
    }

    @Override
    public String toReferenceId(String referenceId) {
        return referenceId;
    }

    @Override
    public CatalogDAO<Product, Long, String> getCatalogDAO() {
        if (productDAO == null) {
            productDAO = getApplicationContext().getBean(ProductDAO.class);
        }
        return productDAO;
    }
}
