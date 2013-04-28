// Copyright 2013 Structure Eng Inc.

package com.structureeng.persistence.model.product;

import com.structureeng.persistence.history.HistoryListener;
import com.structureeng.persistence.history.Revision;
import com.structureeng.persistence.model.AbstractCatalogTenantModel;
import com.structureeng.persistence.model.history.product.ProductDefinitionHistory;

import com.google.common.base.Preconditions;

import java.math.BigInteger;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
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
@Revision(builder = ProductDefinitionHistory.Builder.class)
@Entity
@Table(name = "productDefinition", uniqueConstraints = {
        @UniqueConstraint(name = "productDefinitionTenantPK",
                columnNames = {"idTenant", "idReference"}),
        @UniqueConstraint(name = "productDefinitionPK", columnNames = {"idTenant", "name"}),
        @UniqueConstraint(name = "productDefinitionUK", columnNames = {"idTenant", "idKey"})})
public class ProductDefinition extends AbstractCatalogTenantModel<Long, BigInteger> {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "idProductDefinition")
    private Long id;

    @Column(name = "idKey", nullable = false, length = 50)
    private String key;

    @Column(name = "image", length = 500)
    private String image;

    @JoinColumn(name = "idProductManufacturer", referencedColumnName = "idProductManufacturer", 
            nullable = false)
    @ManyToOne(cascade = CascadeType.REFRESH)
    private ProductManufacturer productManufacturer;

    @JoinColumn(name = "idProductCategory", referencedColumnName = "idProductCategory", 
            nullable = false)
    @ManyToOne(cascade = CascadeType.REFRESH)
    private ProductCategory productCategory;

    @Override
    public Long getId() {
        return id;
    }

    @Override
    public void setId(Long id) {
        this.id = checkPositive(id);
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = checkNotEmpty(key);
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public ProductManufacturer getProductManufacturer() {
        return productManufacturer;
    }

    public void setProductManufacturer(ProductManufacturer productManufacturer) {
        this.productManufacturer = Preconditions.checkNotNull(productManufacturer);
    }

    public ProductCategory getProductCategory() {
        return productCategory;
    }

    public void setProductCategory(ProductCategory productCategory) {
        this.productCategory = Preconditions.checkNotNull(productCategory);
    }

    /**
     * Creates a new {@code Builder} instance.
     *
     * @return - new instance
     */
    public static ProductDefinition.Buider newBuilder() {
        return new ProductDefinition.Buider();
    }

    /**
     * Builder class that creates instances of {@code ProductDefinition}.
     *
     * @author Edgar Rico (edgar.martinez.rico@gmail.com)
     */
    public static class Buider {

        private BigInteger referenceId;
        private String name;
        private String key;
        private String image;
        private ProductManufacturer productManufacturer;
        private ProductCategory productCategory;

        public Buider setReferenceId(BigInteger referenceId) {
            this.referenceId = checkPositive(referenceId);
            return this;
        }

        public Buider setName(String name) {
            this.name = checkNotEmpty(name);
            return this;
        }

        public Buider setKey(String key) {
            this.key = checkNotEmpty(key);
            return this;
        }

        public Buider setImage(String image) {
            this.image = image;
            return this;
        }

        public Buider setProductManufacturer(ProductManufacturer productManufacturer) {
            this.productManufacturer = Preconditions.checkNotNull(productManufacturer);
            return this;
        }

        public Buider setProductCategory(ProductCategory productCategory) {
            this.productCategory = Preconditions.checkNotNull(productCategory);
            return this;
        }

        /**
         * Creates a new instance of {@code ProductDefinition}.
         *
         * @return - new instance
         */
        public ProductDefinition build() {
            ProductDefinition productDefinition = new ProductDefinition();
            productDefinition.setReferenceId(referenceId);
            productDefinition.setName(name);
            productDefinition.setKey(key);
            productDefinition.setImage(image);
            productDefinition.setProductManufacturer(productManufacturer);
            productDefinition.setProductCategory(productCategory);
            return productDefinition;
        }
    }
}
