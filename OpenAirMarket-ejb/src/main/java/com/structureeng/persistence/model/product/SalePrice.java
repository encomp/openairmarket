// Copyright 2013 Structure Eng Inc.

package com.structureeng.persistence.model.product;

import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

/**
 * Specifies the sell price of a {@code Product}.
 *
 * @author Edgar Rico (edgar.martinez.rico@gmail.com)
 */
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
}
