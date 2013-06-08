// Copyright 2013 Structure Eng Inc.

package com.structureeng.persistence.model.product;

import static com.google.common.base.Preconditions.checkNotNull;

import com.structureeng.persistence.history.HistoryListener;
import com.structureeng.persistence.history.Revision;
import com.structureeng.persistence.model.AbstractCatalogTenantModel;
import com.structureeng.persistence.model.history.product.ProductHistory;

import com.google.common.base.Preconditions;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
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
 * Specifies the characteristics of a product.
 *
 * @author Edgar Rico (edgar.martinez.rico@gmail.com)
 */
@EntityListeners(value = {HistoryListener.class})
@Revision(builder = ProductHistory.Builder.class)
@Entity
@Table(name = "product", uniqueConstraints = {
        @UniqueConstraint(name = "productTenantPK",
                columnNames = {"idTenant", "idReference"}),
        @UniqueConstraint(name = "productPK", columnNames = {"idTenant", "name"}),
        @UniqueConstraint(name = "productUK", columnNames = {"idTenant", "idKey"})})
public class Product extends AbstractCatalogTenantModel<Long, String> {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "idProduct")
    private Long id;

    @Column(name = "image", length = 500)
    private String image;

    @Enumerated(EnumType.STRING)
    @Column(name = "productType", length = 50, nullable = false)
    private ProductType productType;

    @JoinColumn(name = "idProductMeasureUnit", referencedColumnName = "idProductMeasureUnit",
            nullable = false)
    @ManyToOne(fetch = FetchType.LAZY)
    private ProductMeasureUnit productMeasureUnit;

    @JoinColumn(name = "idProductManufacturer", referencedColumnName = "idProductManufacturer",
            nullable = true)
    @ManyToOne(fetch = FetchType.LAZY)
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
     * Creates a new {@code Builder} instance.
     *
     * @return - new instance
     */
    public static Product.Buider newBuilder() {
        return new Product.Buider();
    }

    /**
     * Builder class that creates instances of {@code Product}.
     *
     * @author Edgar Rico (edgar.martinez.rico@gmail.com)
     */
    public static class Buider {

        private String referenceId;
        private String name;
        private String image;
        private ProductType productType;
        private ProductMeasureUnit productMeasureUnit;
        private ProductManufacturer productManufacturer;
        private Boolean stocked;

        public Buider setReferenceId(String referenceId) {
            this.referenceId = checkNotEmpty(referenceId);
            return this;
        }

        public Buider setName(String name) {
            this.name = checkNotEmpty(name);
            return this;
        }

        public Buider setImage(String image) {
            this.image = image;
            return this;
        }

        public Buider setProductType(ProductType productType) {
            this.productType = checkNotNull(productType);
            return this;
        }

        public Buider setProductMeasureUnit(ProductMeasureUnit productMeasureUnit) {
            this.productMeasureUnit = Preconditions.checkNotNull(productMeasureUnit);
            return this;
        }

        public Buider setProductManufacturer(ProductManufacturer productManufacturer) {
            this.productManufacturer = productManufacturer;
            return this;
        }

        public Buider setStocked(Boolean stocked) {
            this.stocked = checkNotNull(stocked);
            return this;
        }

        /**
         * Creates a new instance of {@code Product}.
         *
         * @return - new instance
         */
        public Product build() {
            Product productDefinition = new Product();
            productDefinition.setReferenceId(referenceId);
            productDefinition.setName(name);
            productDefinition.setImage(image);
            productDefinition.setProductType(productType);
            productDefinition.setProductMeasureUnit(productMeasureUnit);
            productDefinition.setProductManufacturer(productManufacturer);
            productDefinition.setStocked(stocked);
            return productDefinition;
        }
    }
}
