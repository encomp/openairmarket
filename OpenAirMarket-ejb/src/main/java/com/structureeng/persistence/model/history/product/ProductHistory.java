// Copyright 2013 Structure Eng Inc.

package com.structureeng.persistence.model.history.product;

import static com.google.common.base.Preconditions.checkNotNull;
import static com.structureeng.persistence.model.AbstractModel.checkNotEmpty;

import com.structureeng.persistence.history.HistoryEntityBuilder;
import com.structureeng.persistence.model.history.AbstractHistoryCatalogTenantModel;
import com.structureeng.persistence.model.history.AbstractHistoryTenantModel;
import com.structureeng.persistence.model.product.ProductManufacturer;
import com.structureeng.persistence.model.product.Product;
import com.structureeng.persistence.model.product.ProductMeasureUnit;
import com.structureeng.persistence.model.product.ProductType;

import com.google.common.base.Preconditions;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

/**
 * Define the revision for the {@code Product} entities.
 *
 * @author Edgar Rico (edgar.martinez.rico@gmail.com)
 */
@Entity
@Table(name = "productHistory", uniqueConstraints = {
        @UniqueConstraint(name = "productHistoryUK",
                columnNames = {"idProduct", "idAudit"})})
public class ProductHistory extends AbstractHistoryCatalogTenantModel<String> {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "idProductHistory")
    private Long id;

    @JoinColumn(name = "idProduct", referencedColumnName = "idProduct", nullable = false)
    @ManyToOne(fetch = FetchType.LAZY)
    private Product product;

    @Column(name = "image", length = 500)
    private String image;

    @Enumerated(EnumType.STRING)
    @Column(name = "productType", length = 50, nullable = false)
    private ProductType productType;

    @JoinColumn(name = "idProductMeasureUnit", referencedColumnName = "idProductMeasureUnit",
            nullable = false)
    @ManyToOne(cascade = CascadeType.REFRESH)
    private ProductMeasureUnit productMeasureUnit;

    @JoinColumn(name = "idProductManufacturer", referencedColumnName = "idProductManufacturer",
            nullable = true)
    @ManyToOne(cascade = CascadeType.REFRESH)
    private ProductManufacturer productManufacturer;

    @Column(name = "stocked", nullable = false)
    private Boolean stocked;

    @Override
    public Long getId() {
        return id;
    }

    @Override
    public void setId(Long id) {
        this.id = checkPositive(id);
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = Preconditions.checkNotNull(product);
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public ProductType getProductType() {
        return productType;
    }

    public void setProductType(ProductType productType) {
        this.productType = Preconditions.checkNotNull(productType);
    }

    public ProductMeasureUnit getProductMeasureUnit() {
        return productMeasureUnit;
    }

    public void setProductMeasureUnit(ProductMeasureUnit productMeasureUnit) {
        this.productMeasureUnit = productMeasureUnit;
    }

    public ProductManufacturer getProductManufacturer() {
        return productManufacturer;
    }

    public void setProductManufacturer(ProductManufacturer productManufacturer) {
        this.productManufacturer = productManufacturer;
    }

    public Boolean getStocked() {
        return stocked;
    }

    public void setStocked(Boolean stocked) {
        this.stocked = checkNotNull(stocked);
    }

    /**
     * Factory class for the {@code ProductHistory} entities.
     *
     * @author Edgar Rico (edgar.martinez.rico@gmail.com)
     */
    public static class Builder extends HistoryEntityBuilder<Product,
            ProductHistory> {

        /**
         * Create an instance of {@code ProductHistory}.
         *
         * @param product the instance that will be used to create a new {@code Product}.
         * @return a new instance
         */
        @Override
        public ProductHistory build(Product product) {
            ProductHistory productHistory = new ProductHistory();
            productHistory.setProduct(product);
            productHistory.setReferenceId(product.getReferenceId());
            productHistory.setName(product.getName());
            productHistory.setImage(product.getImage());
            productHistory.setProductType(product.getProductType());
            productHistory.setProductMeasureUnit(product.getProductMeasureUnit());
            productHistory.setProductManufacturer(product.getProductManufacturer());
            productHistory.setStocked(product.getStocked());
            productHistory.setActive(product.getActive());
            productHistory.setVersion(product.getVersion());
            return productHistory;
        }
    }
}
