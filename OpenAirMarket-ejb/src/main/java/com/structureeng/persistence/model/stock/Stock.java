// Copyright 2013 Structure Eng Inc.

package com.structureeng.persistence.model.stock;

import static com.structureeng.persistence.model.AbstractModel.checkPositive;

import static com.google.common.base.Preconditions.checkNotNull;

import com.structureeng.persistence.history.HistoryListener;
import com.structureeng.persistence.history.Revision;

import com.structureeng.persistence.model.AbstractTenantModel;
import com.structureeng.persistence.model.history.stock.StockHistory;
import com.structureeng.persistence.model.product.Product;

import java.math.BigDecimal;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
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
@EntityListeners(value = {HistoryListener.class})
@Revision(builder = StockHistory.Builder.class)
@Entity
@Table(name = "stock", uniqueConstraints = {
        @UniqueConstraint(name = "stockTenantPK",
                columnNames = {"idTenant", "idWareHouse", "idProduct"})})
public class Stock extends AbstractTenantModel<Long> {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
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
        this.id = checkPositive(id);
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
     * Creates a new {@code Builder} instance.
     *
     * @return - new instance
     */
    public static Stock.Buider newBuilder() {
        return new Stock.Buider();
    }
    
    /**
     * Builder class that creates instances of {@code Stock}.
     *
     * @author Edgar Rico (edgar.martinez.rico@gmail.com)
     */
    public static class Buider {
        
        private Product product;
        private Warehouse warehouse;        
        private BigDecimal stockAmount;
        private BigDecimal maximumStock;
        private BigDecimal minimumStock;
        private BigDecimal waste;

        public Stock.Buider setProduct(Product product) {
            this.product = checkNotNull(product);
            return this;
        }

        public Stock.Buider setWarehouse(Warehouse warehouse) {
            this.warehouse = checkNotNull(warehouse);
            return this;
        }
        
        public Stock.Buider setStockAmount(BigDecimal stockAmount) {
            this.stockAmount = checkNotNull(stockAmount);
            return this;
        }
        
        public Stock.Buider setMaximumStock(BigDecimal maximumStock) {
            this.maximumStock = checkPositive(maximumStock);
            return this;
        }
        
        public Stock.Buider setMinimumStock(BigDecimal minimumStock) {
            this.minimumStock = checkPositive(minimumStock);
            return this;
        }
        
        public Stock.Buider setWaste(BigDecimal waste) {
            this.waste = checkPositive(waste);
            return this;
        }

        /**
         * Creates a new instance of {@code Stock}.
         *
         * @return - new instance
         */
        public Stock build() {
            Stock stock = new Stock();
            stock.setProduct(product);
            stock.setWarehouse(warehouse);
            stock.setStockAmount(stockAmount);
            stock.setMaximumStock(maximumStock);
            stock.setMinimumStock(minimumStock);
            stock.setWaste(waste);
            return stock;
        }
    }
}
