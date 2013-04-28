// Copyright 2013 Structure Eng Inc.

package com.structureeng.persistence.model.product;

import com.structureeng.persistence.history.HistoryListener;
import com.structureeng.persistence.history.Revision;
import com.structureeng.persistence.model.AbstractCatalogTenantModel;
import com.structureeng.persistence.model.history.product.ProductCategoryHistory;

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
 * Define the different categories in which a
 * {@code com.structureeng.persistence.model.product.ProductType} can be assigned.
 *
 * @author Edgar Rico (edgar.martinez.rico@gmail.com)
 */
@EntityListeners(value = {HistoryListener.class})
@Revision(builder = ProductCategoryHistory.Builder.class)
@Entity
@Table(name = "productCategory", uniqueConstraints = {
        @UniqueConstraint(name = "productCategoryPK", columnNames = {"idTenant", "idReference"}),
        @UniqueConstraint(name = "productCategoryUK", columnNames = {"idTenant", "name"})})
public class ProductCategory extends AbstractCatalogTenantModel<Long, Integer> {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "idProductCategory")
    private Long id;

    @ManyToOne(cascade = CascadeType.REFRESH)
    @JoinColumn(name = "idParentProductCategory", referencedColumnName = "idProductCategory",
            nullable = true)
    private ProductCategory parentProductCategory;

    @Override
    public Long getId() {
        return id;
    }

    @Override
    public void setId(Long id) {
        this.id = checkPositive(id);
    }

    public ProductCategory getParentProductCategory() {
        return parentProductCategory;
    }

    public void setParentProductCategory(ProductCategory parentProductCategory) {
        this.parentProductCategory = parentProductCategory;
    }

    /**
     * Creates a new {@code Builder} instance.
     *
     * @return - new instance
     */
    public static ProductCategory.Buider newBuilder() {
        return new ProductCategory.Buider();
    }

    /**
     * Builder class that creates instances of {@code ProductCategory}.
     *
     * @author Edgar Rico (edgar.martinez.rico@gmail.com)
     */
    public static class Buider {

        private Integer referenceId;
        private String name;

        public Buider setReferenceId(Integer referenceId) {
            this.referenceId = checkPositive(referenceId);
            return this;
        }

        public Buider setName(String name) {
            this.name = checkNotEmpty(name);
            return this;
        }

        /**
         * Creates a new instance of {@code Division}.
         *
         * @return - new instance
         */
        public ProductCategory build() {
            ProductCategory division = new ProductCategory();
            division.setReferenceId(referenceId);
            division.setName(name);
            return division;
        }
    }
}
