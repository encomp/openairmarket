// Copyright 2013 Structure Eng Inc.

package com.structureeng.persistence.model.history.product;

import com.structureeng.persistence.model.business.ProductType;
import com.structureeng.persistence.model.history.AbstractTenantHistoryModel;
import com.structureeng.persistence.model.product.Product;
import com.structureeng.persistence.model.product.ProductDefinition;

import com.google.common.base.Preconditions;

import java.math.BigDecimal;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

/**
 * Define the revision for the {@code ProductType} entities.
 *
 * @author Edgar Rico (edgar.martinez.rico@gmail.com)
 */
@Entity
@Table(name = "productHistory", uniqueConstraints = {
        @UniqueConstraint(name = "productHistoryUK", 
            columnNames = {"idProduct", "idHistoryTenant"})})
public class ProductHistory extends AbstractTenantHistoryModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "idProductHistory")
    private Long id;

    @JoinColumn(name = "idProduct", referencedColumnName = "idProduct", nullable = false)
    @ManyToOne(cascade = CascadeType.REFRESH)
    private Product product;

    @Column(name = "taxable", nullable = false)
    private Boolean taxable;

    @Column(name = "autoStock", nullable = false)
    private Boolean autoStock;

    @Column(name = "wastable", nullable = false)
    private Boolean wastable;

    @Column(name = "quantity", nullable = false, precision = 13, scale = 4)
    private BigDecimal quantity;

    @Column(name = "cost", nullable = false, precision = 13, scale = 4)
    private BigDecimal cost;

    @Column(name = "lastCost", nullable = false, precision = 13, scale = 4)
    private BigDecimal lastCost;

    @Column(name = "maximumStock", nullable = false, precision = 13, scale = 4)
    private BigDecimal maximumStock;

    @Column(name = "minimumStock", nullable = false, precision = 13, scale = 4)
    private BigDecimal minimumStock;

    @Column(name = "waste", nullable = false, precision = 13, scale = 4)
    private BigDecimal waste;

    @JoinColumn(name = "idProductDefinition", referencedColumnName = "idProductDefinition",
            nullable = false)
    @ManyToOne(cascade = CascadeType.REFRESH)
    private ProductDefinition productDefinition;

    @JoinColumn(name = "idRule", referencedColumnName = "idRule", nullable = false)
    @ManyToOne(cascade = CascadeType.REFRESH)
    private ProductType productType;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = Preconditions.checkNotNull(id);
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = Preconditions.checkNotNull(product);
    }

    public Boolean getTaxable() {
        return taxable;
    }

    public void setTaxable(Boolean taxable) {
        this.taxable = Preconditions.checkNotNull(taxable);
    }

    public Boolean getAutoStock() {
        return autoStock;
    }

    public void setAutoStock(Boolean autoStock) {
        this.autoStock = Preconditions.checkNotNull(autoStock);
    }

    public Boolean getWastable() {
        return wastable;
    }

    public void setWastable(Boolean wastable) {
        this.wastable = Preconditions.checkNotNull(wastable);
    }

    public BigDecimal getQuantity() {
        return quantity;
    }

    public void setQuantity(BigDecimal quantity) {
        this.quantity = Preconditions.checkNotNull(quantity);
    }

    public BigDecimal getCost() {
        return cost;
    }

    public void setCost(BigDecimal cost) {
        this.cost = Preconditions.checkNotNull(cost);
    }

    public BigDecimal getLastCost() {
        return lastCost;
    }

    public void setLastCost(BigDecimal lastCost) {
        this.lastCost = Preconditions.checkNotNull(lastCost);
    }

    public BigDecimal getMaximumStock() {
        return maximumStock;
    }

    public void setMaximumStock(BigDecimal maximumStock) {
        this.maximumStock = Preconditions.checkNotNull(maximumStock);
    }

    public BigDecimal getMinimumStock() {
        return minimumStock;
    }

    public void setMinimumStock(BigDecimal minimumStock) {
        this.minimumStock = Preconditions.checkNotNull(minimumStock);
    }

    public BigDecimal getWaste() {
        return waste;
    }

    public void setWaste(BigDecimal waste) {
        this.waste = Preconditions.checkNotNull(waste);
    }

    public ProductDefinition getProductDefinition() {
        return productDefinition;
    }

    public void setProductDefinition(ProductDefinition productDefinition) {
        this.productDefinition = Preconditions.checkNotNull(productDefinition);
    }

    public ProductType getProductType() {
        return productType;
    }

    public void setProductType(ProductType productType) {
        this.productType = Preconditions.checkNotNull(productType);
    }
}
