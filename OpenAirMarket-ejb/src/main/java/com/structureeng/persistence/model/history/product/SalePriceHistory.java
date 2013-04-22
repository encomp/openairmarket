// Copyright 2013 Structure Eng Inc.

package com.structureeng.persistence.model.history.product;

import static com.structureeng.persistence.model.AbstractModel.checkPositive;

import com.structureeng.persistence.history.HistoryEntityBuilder;
import com.structureeng.persistence.model.product.SalePrice;

import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

/**
 * Define the revision for the {@code SalePrice} entities.
 *
 * @author Edgar Rico (edgar.martinez.rico@gmail.com)
 */
@Entity
@DiscriminatorValue("SALE_PRICE")
public class SalePriceHistory extends ProductPriceHistory {

    @Column(name = "profit", nullable = true, precision = 13, scale = 4)
    private BigDecimal profit;

    public BigDecimal getProfit() {
        return profit;
    }

    public void setProfit(BigDecimal profit) {
        this.profit = checkPositive(profit);
    }

    /**
     * Factory class for the {@code SalePriceHistory} entities.
     *
     * @author Edgar Rico (edgar.martinez.rico@gmail.com)
     */
    public static class Builder extends HistoryEntityBuilder<SalePrice, SalePriceHistory> {

        /**
         * Create an instance of {@code SalePriceHistory}.
         *
         * @param salePrice the instance that will be used to create a new {@code SalePrice}.
         * @return a new instance
         */
        @Override
        public SalePriceHistory build(SalePrice salePrice) {
            SalePriceHistory salePriceHistory = new SalePriceHistory();
            salePriceHistory.setProductPrice(salePrice);
            salePriceHistory.setProduct(salePrice.getProduct());
            salePriceHistory.setPrice(salePrice.getPrice());
            salePriceHistory.setQuantity(salePrice.getQuantity());
            salePriceHistory.setProfit(salePrice.getProfit());
            salePriceHistory.setActive(salePrice.getActive());
            salePriceHistory.setVersion(salePrice.getVersion());
            return salePriceHistory;
        }
    }
}
