// Copyright 2013 Structure Eng Inc.

package com.structureeng.persistence.model.history.stock;

import com.structureeng.persistence.model.history.AbstractHistoryTenantModel;
import com.structureeng.persistence.model.product.Product;
import com.structureeng.persistence.model.stock.Stock;
import com.structureeng.persistence.model.stock.Warehouse;

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
 * Define the revision for the {@code Stock} entities.
 *
 * @author Edgar Rico (edgar.martinez.rico@gmail.com)
 */
@Entity
@Table(name = "stockHistory", uniqueConstraints = {
        @UniqueConstraint(name = "stockHistoryUK",
            columnNames = {"idStock", "idAudit"})})
public class StockHistory extends AbstractHistoryTenantModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "idProductHistory")
    private Long id;

    @JoinColumn(name = "idStock", referencedColumnName = "idStock", nullable = false)
    @ManyToOne(cascade = CascadeType.REFRESH)
    private Stock stock;

    @JoinColumn(name = "idProduct", referencedColumnName = "idProduct", nullable = false)
    @ManyToOne(cascade = CascadeType.REFRESH)
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

    public Stock getStock() {
        return stock;
    }

    public void setStock(Stock stock) {
        this.stock = Preconditions.checkNotNull(stock);
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
