// Copyright 2013 Structure Eng Inc.

package com.structureeng.persistence.model.business;

import com.structureeng.persistence.history.HistoryListener;
import com.structureeng.persistence.history.Revision;
import com.structureeng.persistence.model.history.business.ProductTypeHistory;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;

/**
 * Define the different types of {@code com.structureeng.persistence.model.product.Product}.
 * This drives the business rules for a particular type.
 *
 * @author Edgar Rico (edgar.martinez.rico@gmail.com)
 */
@EntityListeners(value = {HistoryListener.class})
@Revision(builder = ProductTypeHistory.Builder.class)
@Entity
@DiscriminatorValue("PRODUCT_TYPE")
public class ProductType extends Rule {

    /**
     * Creates a new {@code ProductType.Builder} instance.
     *
     * @return - new instance
     */
    public static ProductType.Buider newBuilder() {
        return new ProductType.Buider();
    }

    /**
     * Builder class that creates instances of {@code ProductType}.
     *
     * @author Edgar Rico (edgar.martinez.rico@gmail.com)
     */
    public static class Buider {

        private Integer referenceId;
        private String name;
        private String description;

        public ProductType.Buider setReferenceId(Integer referenceId) {
            this.referenceId = checkPositive(referenceId);
            return this;
        }

        public ProductType.Buider setName(String name) {
            this.name = checkNotEmpty(name);
            return this;
        }

        public ProductType.Buider setDescription(String description) {
            this.description = checkNotEmpty(description);
            return this;
        }

        /**
         * Creates a new instance of {@code ProductType}.
         *
         * @return - new instance
         */
        public ProductType build() {
            ProductType productType = new ProductType();
            productType.setReferenceId(referenceId);
            productType.setName(name);
            productType.setDescription(description);
            return productType;
        }
    }
}
