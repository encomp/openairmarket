// Copyright 2013 Structure Eng Inc.

package com.structureeng.persistence.model.history.product;

import com.structureeng.persistence.history.HistoryEntityBuilder;
import com.structureeng.persistence.model.history.AbstractHistoryTenantModel;
import com.structureeng.persistence.model.product.ProductManufacturer;
import com.structureeng.persistence.model.product.ProductCategory;
import com.structureeng.persistence.model.product.ProductDefinition;

import com.google.common.base.Preconditions;

import java.math.BigInteger;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

/**
 * Define the revision for the {@code ProductDefinition} entities.
 *
 * @author Edgar Rico (edgar.martinez.rico@gmail.com)
 */
@Entity
@Table(name = "productDefinitionHistory", uniqueConstraints = {
        @UniqueConstraint(name = "productDefinitionHistoryUK",
                columnNames = {"idProductDefinition", "idAudit"})})
public class ProductDefinitionHistory extends AbstractHistoryTenantModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "idProductDefinitionHistory")
    private Long id;

    @JoinColumn(name = "idProductDefinition", referencedColumnName = "idProductDefinition",
            nullable = false)
    @ManyToOne(fetch = FetchType.LAZY)
    private ProductDefinition productDefinition;

    @Column(name = "idReference", nullable = false)
    private BigInteger referenceId;

    @Column(name = "idKey", nullable = false, length = 50)
    private String key;

    @Column(name = "name", nullable = false, length = 500)
    private String name;

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

    public ProductDefinition getProductDefinition() {
        return productDefinition;
    }

    public void setProductDefinition(ProductDefinition productDefinition) {
        this.productDefinition = Preconditions.checkNotNull(productDefinition);
    }

    public BigInteger getReferenceId() {
        return referenceId;
    }

    public void setReferenceId(BigInteger referenceId) {
        this.referenceId = checkPositive(referenceId);
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = checkNotEmpty(key);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = checkNotEmpty(name);
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
     * Factory class for the {@code ProductDefinitionHistory} entities.
     *
     * @author Edgar Rico (edgar.martinez.rico@gmail.com)
     */
    public static class Builder extends HistoryEntityBuilder<ProductDefinition,
            ProductDefinitionHistory> {

        /**
         * Create an instance of {@code ProductDefinitionHistory}.
         *
         * @param productDefinition the instance that will be used to create a new
         *        {@code ProductDefinition}.
         * @return a new instance
         */
        @Override
        public ProductDefinitionHistory build(ProductDefinition productDefinition) {
            ProductDefinitionHistory productDefinitionHistory = new ProductDefinitionHistory();
            productDefinitionHistory.setProductDefinition(productDefinition);
            productDefinitionHistory.setReferenceId(productDefinition.getReferenceId());
            productDefinitionHistory.setName(productDefinition.getName());
            productDefinitionHistory.setKey(productDefinition.getKey());
            productDefinitionHistory.setImage(productDefinition.getImage());
            productDefinitionHistory
                    .setProductManufacturer(productDefinition.getProductManufacturer());
            productDefinitionHistory.setProductCategory(productDefinition.getProductCategory());
            productDefinitionHistory.setActive(productDefinition.getActive());
            productDefinitionHistory.setVersion(productDefinition.getVersion());
            return productDefinitionHistory;
        }
    }
}
