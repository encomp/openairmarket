// Copyright 2013 Structure Eng Inc.

package com.structureeng.persistence.model.product;

import static com.google.common.base.Preconditions.checkNotNull;

import com.structureeng.persistence.history.HistoryListener;
import com.structureeng.persistence.history.Revision;
import com.structureeng.persistence.model.history.product.PurchasePriceHistory;

import java.math.BigDecimal;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;

/**
 * Specifies the purchase price of a {@code Product}.
 *
 * @author Edgar Rico (edgar.martinez.rico@gmail.com)
 */
@EntityListeners(value = {HistoryListener.class})
@Revision(builder = PurchasePriceHistory.Builder.class)
@Entity
@DiscriminatorValue("PURCHASE_PRICE")
public class PurchasePrice extends ProductPrice {

    /**
     * Creates a new {@code Builder} instance.
     *
     * @return - new instance
     */
    public static Builder newBuilder() {
        return new Builder();
    }

    /**
     * Builder class that creates instances of {@code PurchasePrice}.
     *
     * @author Edgar Rico (edgar.martinez.rico@gmail.com)
     */
    public static class Builder {

        private ProductOrganization product;
        private BigDecimal price;

        public Builder setProduct(ProductOrganization product) {
            this.product = checkNotNull(product);
            return this;
        }

        public Builder setPrice(BigDecimal price) {
            this.price = checkPositive(price);
            return this;
        }

        /**
         * Creates a new instance of {@code PurchasePrice}.
         *
         * @return - new instance
         */
        public PurchasePrice build() {
            PurchasePrice purchasePrice = new PurchasePrice();
            purchasePrice.setProduct(product);
            purchasePrice.setPrice(price);
            return purchasePrice;
        }
    }
}
