// Copyright 2013 Structure Eng Inc.

package com.structureeng.persistence.model.history.product;

import com.structureeng.persistence.history.HistoryEntityBuilder;
import com.structureeng.persistence.model.history.AbstractHistoryCatalogTenantModel;
import com.structureeng.persistence.model.history.AbstractHistoryTenantModel;
import com.structureeng.persistence.model.product.ProductManufacturer;

import com.google.common.base.Preconditions;

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
 * Define the revision for the {@code ProductManufacturer} entities.
 *
 * @author Edgar Rico (edgar.martinez.rico@gmail.com)
 */
@Entity
@Table(name = "productManufacturerHistory", uniqueConstraints = {
    @UniqueConstraint(name = "productManufacturerHistoryUK",
    columnNames = {"idProductManufacturer", "idAudit"})})
public class ProductManufacturerHistory extends AbstractHistoryCatalogTenantModel<Integer> {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "idProductManufacturerHistory")
    private Long id;

    @JoinColumn(name = "idProductManufacturer", referencedColumnName = "idProductManufacturer", 
            nullable = false)
    @ManyToOne(fetch = FetchType.LAZY)
    private ProductManufacturer productManufacturer;

    @Override
    public Long getId() {
        return id;
    }

    @Override
    public void setId(Long id) {
        this.id = checkPositive(id);
    }

    public ProductManufacturer getProductManufacturer() {
        return productManufacturer;
    }

    public void setProductManufacturer(ProductManufacturer productManufacturer) {
        this.productManufacturer = Preconditions.checkNotNull(productManufacturer);
    }

    /**
     * Factory class for the {@code ProductManufacturerHistory} entities.
     *
     * @author Edgar Rico (edgar.martinez.rico@gmail.com)
     */
    public static class Builder extends HistoryEntityBuilder<ProductManufacturer, 
            ProductManufacturerHistory> {

        /**
         * Create an instance of {@code ProductManufacturerHistory}.
         *
         * @param productManufacturer the instance that will be used to create a new 
         *                            {@code ProductManufacturer}.
         * @return a new instance
         */
        @Override
        public ProductManufacturerHistory build(ProductManufacturer productManufacturer) {
            ProductManufacturerHistory pmHistory = new ProductManufacturerHistory();
            pmHistory.setProductManufacturer(productManufacturer);
            pmHistory.setReferenceId(productManufacturer.getReferenceId());
            pmHistory.setName(productManufacturer.getName());
            pmHistory.setActive(productManufacturer.getActive());
            pmHistory.setVersion(productManufacturer.getVersion());
            return pmHistory;
        }
    }
}
