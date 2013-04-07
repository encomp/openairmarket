// Copyright 2013 Structure Eng Inc.

package com.structureeng.persistence.model.product;

import com.structureeng.persistence.model.AbstractCatalogTenantModel;
import com.structureeng.persistence.model.business.ProductType;
import com.structureeng.persistence.model.business.TaxType;

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
 * Defines the way a {@code ProductDe} will be bought.
 *
 * @author Edgar Rico (edgar.martinez.rico@gmail.com)
 */
@Entity
@Table(name = "product", uniqueConstraints = {
        @UniqueConstraint(name = "productTenantPK",
                columnNames = {"idTenant", "idReference"}),
        @UniqueConstraint(name = "productUK",
                columnNames = {"idTenant", "idProductDefinition", "idRule"})})
public class Product extends AbstractCatalogTenantModel<Long, Integer> {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "idProduct")
    private Long id;
        
    @Column(name = "quantity", nullable = false, precision = 13, scale = 4)
    private BigDecimal quantity;

    @Column(name = "cost", nullable = false, precision = 13, scale = 4)
    private BigDecimal cost;

    @Column(name = "lastCost", nullable = false, precision = 13, scale = 4)
    private BigDecimal lastCost;
    
    @Column(name = "autoStock", nullable = false)
    private Boolean autoStock;
    
    @Column(name = "wastable", nullable = false)
    private Boolean wastable;

    @JoinColumn(name = "idProductDefinition", referencedColumnName = "idProductDefinition",
            nullable = false)
    @ManyToOne(cascade = CascadeType.REFRESH)
    private ProductDefinition productDefinition;
    
    @JoinColumn(name = "idProductType", referencedColumnName = "idRule", nullable = false)
    @ManyToOne(cascade = CascadeType.REFRESH)
    private ProductType productType;
    
    @JoinColumn(name = "idTaxType", referencedColumnName = "idRule", nullable = false)
    @ManyToOne(cascade = CascadeType.REFRESH)
    private TaxType taxType;
    
    @Override
    public Long getId() {
        return id;
    }

    @Override
    public void setId(Long id) {
        this.id = Preconditions.checkNotNull(id);
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

    public TaxType getTaxType() {
        return taxType;
    }

    public void setTaxType(TaxType taxType) {
        this.taxType = Preconditions.checkNotNull(taxType);
    }
}
