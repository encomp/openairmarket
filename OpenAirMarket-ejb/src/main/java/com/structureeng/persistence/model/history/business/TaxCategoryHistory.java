// Copyright 2013 Structure Eng Inc.

package com.structureeng.persistence.model.history.business;

import com.structureeng.persistence.history.HistoryEntityBuilder;
import com.structureeng.persistence.model.business.TaxCategory;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

/**
 * Define the revision for the {@code TaxType} entities.
 *
 * @author Edgar Rico (edgar.martinez.rico@gmail.com)
 */
@Entity
@DiscriminatorValue("TAX_CATEGORY")
public class TaxCategoryHistory extends RuleOrganizationHistory {

    /**
     * Factory class for the {@code TaxCategoryHistory} entities.
     *
     * @author Edgar Rico (edgar.martinez.rico@gmail.com)
     */
    public static class Builder extends HistoryEntityBuilder<TaxCategory, TaxCategoryHistory> {

        /**
         * Create an instance of {@code TaxCategoryHistory}.
         *
         * @param taxCategory the instance that will be used to create a new {@code TaxCategory}.
         * @return a new instance
         */
        @Override
        public TaxCategoryHistory build(TaxCategory taxCategory) {
            TaxCategoryHistory taxCategoryHistory = new TaxCategoryHistory();
            taxCategoryHistory.setRuleOrganization(taxCategory);
            taxCategoryHistory.setReferenceId(taxCategory.getReferenceId());
            taxCategoryHistory.setName(taxCategory.getName());
            taxCategoryHistory.setOrganization(taxCategory.getOrganization());
            taxCategoryHistory.setActive(taxCategory.getActive());
            taxCategoryHistory.setVersion(taxCategory.getVersion());
            return taxCategoryHistory;
        }
    }
}
