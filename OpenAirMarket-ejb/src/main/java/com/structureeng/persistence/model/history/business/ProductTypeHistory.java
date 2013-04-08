// Copyright 2013 Structure Eng Inc.

package com.structureeng.persistence.model.history.business;

import com.structureeng.persistence.history.HistoryEntityBuilder;
import com.structureeng.persistence.model.business.ProductType;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

/**
 * Define the revision for the {@code ProductType} entities.
 *
 * @author Edgar Rico (edgar.martinez.rico@gmail.com)
 */
@Entity
@DiscriminatorValue("PRODUCT_TYPE")
public class ProductTypeHistory extends RuleHistory {

    /**
     * Factory class for the {@code ProductTypeHistory} entities.
     *
     * @author Edgar Rico (edgar.martinez.rico@gmail.com)
     */
    public static class Builder extends HistoryEntityBuilder<ProductType, ProductTypeHistory> {

        /**
         * Create an instance of {@code ProductTypeHistory}.
         *
         * @param productType the instance that will be used to create a new {@code ProductType}.
         * @return a new instance
         */
        @Override
        public ProductTypeHistory build(ProductType productType) {
            ProductTypeHistory productTypeHistory = new ProductTypeHistory();
            productTypeHistory.setRule(productType);
            productTypeHistory.setReferenceId(productType.getReferenceId());
            productTypeHistory.setName(productType.getName());
            productTypeHistory.setDescription(productType.getDescription());
            productTypeHistory.setActive(productType.getActive());
            productTypeHistory.setVersion(productType.getVersion());
            return productTypeHistory;
        }
    }
}
