// Copyright 2013 Structure Eng Inc.

package com.structureeng.persistence.model.stock;

import com.structureeng.persistence.model.AbstractTenantModel;
import com.structureeng.persistence.model.product.Product;

import com.google.common.base.Preconditions;

import java.math.BigDecimal;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

/**
 * Stores the stock for a {@code Product}.
 *
 * @author Edgar Rico (edgar.martinez.rico@gmail.com)
 */
@Entity
@Table(name = "stock", uniqueConstraints = {
        @UniqueConstraint(name = "stockTenantPK",
                columnNames = {"idTenant", "idWareHouse", "idProduct"})})
public class Stock extends AbstractTenantModel<Long> {

    @Id
    @Column(name = "idStock")
    private Long id;

    @JoinColumn(name = "idProduct", referencedColumnName = "idProduct", nullable = false)
    @ManyToOne(cascade = CascadeType.REFRESH, fetch = FetchType.LAZY)
    private Product product;

    @JoinColumn(name = "idWarehouse", referencedColumnName = "idWarehouse", nullable = false)
    @ManyToOne(cascade = CascadeType.REFRESH)
    private Warehouse warehouse;

    @Column(name = "stock", nullable = false, precision = 13, scale = 4)
    private BigDecimal stockAmount;
            
    @Column(name = "maximumStock", nullable = false, precision = 13, scale = 4)
    private BigDecimal maximumStock;

    @Column(name = "minimumStock", nullable = false, precision = 13, scale = 4)
    private BigDecimal minimumStock;
    
    @Column(name = "waste", nullable = false, precision = 13, scale = 4)
    private BigDecimal waste;
                    
    @Override
    public Long getId() {
        return id;
    }

    @Override
    public void setId(Long id) {
        this.id = Preconditions.checkNotNull(id);
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = Preconditions.checkNotNull(product);
    }

    public Warehouse getWarehouse() {
        return warehouse;
    }

    public void setWarehouse(Warehouse warehouse) {
        this.warehouse = Preconditions.checkNotNull(warehouse);
    }

    public BigDecimal getStockAmount() {
        return stockAmount;
    }

    public void setStockAmount(BigDecimal stockAmount) {
        this.stockAmount = Preconditions.checkNotNull(stockAmount);
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
}
