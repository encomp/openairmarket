// Copyright 2013 Structure Eng Inc.

package com.structureeng.persistence.model.history.product;

import com.structureeng.persistence.history.HistoryEntityBuilder;
import com.structureeng.persistence.model.product.PurchasePrice;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

/**
 * Define the revision for the {@code PurchasePrice} entities.
 *
 * @author Edgar Rico (edgar.martinez.rico@gmail.com)
 */
@Entity
@DiscriminatorValue("PURCHASE_PRICE")
public class PurchasePriceHistory extends ProductPriceHistory {

    /**
     * Factory class for the {@code PurchasePriceHistory} entities.
     *
     * @author Edgar Rico (edgar.martinez.rico@gmail.com)
     */
    public static class Builder extends HistoryEntityBuilder<PurchasePrice, PurchasePriceHistory> {

        /**
         * Create an instance of {@code PurchasePriceHistory}.
         *
         * @param purchasePrice the instance that will be used to create a new
         *                      {@code PurchasePrice}.
         * @return a new instance
         */
        @Override
        public PurchasePriceHistory build(PurchasePrice purchasePrice) {
            PurchasePriceHistory purchasePriceHistory = new PurchasePriceHistory();
            purchasePriceHistory.setProductPrice(purchasePrice);
            purchasePriceHistory.setProduct(purchasePrice.getProduct());
            purchasePriceHistory.setPrice(purchasePrice.getPrice());            
            purchasePriceHistory.setActive(purchasePrice.getActive());
            purchasePriceHistory.setVersion(purchasePrice.getVersion());
            return purchasePriceHistory;
        }
    }
}
