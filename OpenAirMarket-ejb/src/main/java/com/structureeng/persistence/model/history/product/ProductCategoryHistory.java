// Copyright 2013 Structure Eng Inc.

package com.structureeng.persistence.model.history.product;

import com.structureeng.persistence.history.HistoryEntityBuilder;
import com.structureeng.persistence.model.history.AbstractHistoryTenantModel;
import com.structureeng.persistence.model.product.ProductCategory;

import com.google.common.base.Preconditions;

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
 * Define the revision for the {@code ProductCategory} entities.
 *
 * @author Edgar Rico (edgar.martinez.rico@gmail.com)
 */
@Entity
@Table(name = "productCategoryHistory", uniqueConstraints = {
        @UniqueConstraint(name = "productCategoryHistoryUK",
            columnNames = {"idProductCategory", "idAudit"})})
public class ProductCategoryHistory extends AbstractHistoryTenantModel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "idProductCategoryHistory")
    private Long id;

    @JoinColumn(name = "idProductCategory", referencedColumnName = "idProductCategory",
            nullable = false)
    @ManyToOne(fetch = FetchType.LAZY)
    private ProductCategory productCategory;

    @Column(name = "idReference", nullable = false)
    private Integer referenceId;

    @ManyToOne(cascade = CascadeType.REFRESH)
    @JoinColumn(name = "idParentProductCategory", referencedColumnName = "idProductCategory",
            nullable = true)
    private ProductCategory parentProductCategory;

    @Column(name = "name", nullable = false)
    private String name;

    @Override
    public Long getId() {
        return id;
    }

    @Override
    public void setId(Long id) {
        this.id = checkPositive(id);
    }

    public ProductCategory getProductCategory() {
        return productCategory;
    }

    public void setProductCategory(ProductCategory productCategory) {
        this.productCategory = Preconditions.checkNotNull(productCategory);
    }

    public Integer getReferenceId() {
        return referenceId;
    }

    public void setReferenceId(Integer referenceId) {
        this.referenceId = checkPositive(referenceId);
    }

    public ProductCategory getParentProductCategory() {
        return parentProductCategory;
    }

    public void setParentProductCategory(ProductCategory parentProductCategory) {
        this.parentProductCategory = parentProductCategory;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = checkNotEmpty(name);
    }

    /**
     * Factory class for the {@code ProductCategoryHistory} entities.
     *
     * @author Edgar Rico (edgar.martinez.rico@gmail.com)
     */
    public static class Builder extends HistoryEntityBuilder<ProductCategory,
            ProductCategoryHistory> {

        /**
         * Create an instance of {@code ProductCategoryHistory}.
         *
         * @param productCategory the instance that will be used to create a new
         *                        {@code ProductCategory}.
         * @return a new instance
         */
        @Override
        public ProductCategoryHistory build(ProductCategory productCategory) {
            ProductCategoryHistory pcHistory = new ProductCategoryHistory();
            pcHistory.setProductCategory(productCategory);
            pcHistory.setReferenceId(productCategory.getReferenceId());
            pcHistory.setName(productCategory.getName());
            pcHistory.setActive(productCategory.getActive());
            pcHistory.setVersion(productCategory.getVersion());
            return pcHistory;
        }
    }
}
