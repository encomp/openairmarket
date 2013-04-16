// Copyright 2013 Structure Eng Inc.

package com.structureeng.persistence.model.history.stock;

import static com.structureeng.persistence.model.AbstractModel.checkPositive;

import static com.google.common.base.Preconditions.checkNotNull;

import com.structureeng.persistence.history.HistoryEntityBuilder;
import com.structureeng.persistence.model.history.AbstractHistoryTenantModel;
import com.structureeng.persistence.model.product.Product;
import com.structureeng.persistence.model.stock.Stock;
import com.structureeng.persistence.model.stock.Warehouse;

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
        this.id = checkPositive(id);
    }

    public Stock getStock() {
        return stock;
    }

    public void setStock(Stock stock) {
        this.stock = checkNotNull(stock);
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = checkNotNull(product);
    }

    public Warehouse getWarehouse() {
        return warehouse;
    }

    public void setWarehouse(Warehouse warehouse) {
        this.warehouse = checkNotNull(warehouse);
    }

    public BigDecimal getStockAmount() {
        return stockAmount;
    }

    public void setStockAmount(BigDecimal stockAmount) {
        this.stockAmount = checkNotNull(stockAmount);
    }
    
    public BigDecimal getMaximumStock() {
        return maximumStock;
    }

    public void setMaximumStock(BigDecimal maximumStock) {
        this.maximumStock = checkPositive(maximumStock);
    }

    public BigDecimal getMinimumStock() {
        return minimumStock;
    }

    public void setMinimumStock(BigDecimal minimumStock) {
        this.minimumStock = checkPositive(minimumStock);
    }

    public BigDecimal getWaste() {
        return waste;
    }

    public void setWaste(BigDecimal waste) {
        this.waste = checkPositive(waste);
    }
    
    /**
     * Factory class for the {@code StockHistory} entities.
     *
     * @author Edgar Rico (edgar.martinez.rico@gmail.com)
     */
    public static class Builder extends HistoryEntityBuilder<Stock, StockHistory> {

        /**
         * Create an instance of {@code StockHistory}.
         *
         * @param stock the instance that will be used to create a new {@code Stock}.
         * @return a new instance
         */
        @Override
        public StockHistory build(Stock stock) {
            StockHistory stockHistory = new StockHistory();
            stockHistory.setStock(stock);
            stockHistory.setProduct(stock.getProduct());
            stockHistory.setWarehouse(stock.getWarehouse());
            stockHistory.setStockAmount(stock.getStockAmount());
            stockHistory.setMaximumStock(stock.getMaximumStock());
            stockHistory.setMinimumStock(stock.getMinimumStock());
            stockHistory.setWaste(stock.getWaste());
            stockHistory.setActive(stock.getActive());
            stockHistory.setVersion(stock.getVersion());
            return stockHistory;
        }
    }
}
