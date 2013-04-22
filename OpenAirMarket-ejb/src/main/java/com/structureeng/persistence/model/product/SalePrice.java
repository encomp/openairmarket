// Copyright 2013 Structure Eng Inc.

package com.structureeng.persistence.model.product;

import static com.structureeng.persistence.model.AbstractModel.checkPositive;
import static com.google.common.base.Preconditions.checkNotNull;

import com.structureeng.persistence.history.HistoryListener;
import com.structureeng.persistence.history.Revision;
import com.structureeng.persistence.model.history.product.SalePriceHistory;

import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;

/**
 * Specifies the sell price of a {@code Product}.
 *
 * @author Edgar Rico (edgar.martinez.rico@gmail.com)
 */
@EntityListeners(value = {HistoryListener.class})
@Revision(builder = SalePriceHistory.Builder.class)
@Entity
@DiscriminatorValue("SALE_PRICE")
public class SalePrice extends ProductPrice {

    @Column(name = "profit", nullable = true, precision = 13, scale = 4)
    private BigDecimal profit;

    public BigDecimal getProfit() {
        return profit;
    }

    public void setProfit(BigDecimal profit) {
        this.profit = checkPositive(profit);
    }

    /**
     * Creates a new {@code Builder} instance.
     *
     * @return - new instance
     */
    public static SalePrice.Buider newBuilder() {
        return new SalePrice.Buider();
    }

    /**
     * Builder class that creates instances of {@code SalePrice}.
     *
     * @author Edgar Rico (edgar.martinez.rico@gmail.com)
     */
    public static class Buider {

        private Product product;
        private BigDecimal quantity;
        private BigDecimal price;
        private BigDecimal profit;

        public SalePrice.Buider setProduct(Product product) {
            this.product = checkNotNull(product);
            return this;
        }

        public SalePrice.Buider setQuantity(BigDecimal quantity) {
            this.quantity = checkPositive(quantity);
            return this;
        }

        public SalePrice.Buider setPrice(BigDecimal price) {
            this.price = checkPositive(price);
            return this;
        }

        public SalePrice.Buider setProfit(BigDecimal profit) {
            this.profit = checkPositive(profit);
            return this;
        }

        /**
         * Creates a new instance of {@code SalePrice}.
         *
         * @return - new instance
         */
        public SalePrice build() {
            SalePrice salePrice = new SalePrice();
            salePrice.setProduct(product);
            salePrice.setQuantity(quantity);
            salePrice.setPrice(price);
            salePrice.setProfit(profit);
            return salePrice;
        }
    }
}
