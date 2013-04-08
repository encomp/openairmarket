// Copyright 2013 Structure Eng Inc.

package com.structureeng.persistence.model.history.business;

import com.structureeng.persistence.history.HistoryEntityBuilder;
import com.structureeng.persistence.model.business.TaxType;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

/**
 * Define the revision for the {@code TaxType} entities.
 *
 * @author Edgar Rico (edgar.martinez.rico@gmail.com)
 */
@Entity
@DiscriminatorValue("TAX_TYPE")
public class TaxTypeHistory extends RuleHistory {

    /**
     * Factory class for the {@code TaxTypeHistory} entities.
     *
     * @author Edgar Rico (edgar.martinez.rico@gmail.com)
     */
    public static class Builder extends HistoryEntityBuilder<TaxType, TaxTypeHistory> {

        /**
         * Create an instance of {@code TaxTypeHistory}.
         *
         * @param taxType the instance that will be used to create a new {@code TaxType}.
         * @return a new instance
         */
        @Override
        public TaxTypeHistory build(TaxType taxType) {
            TaxTypeHistory taxTypeHistory = new TaxTypeHistory();
            taxTypeHistory.setRule(taxType);
            taxTypeHistory.setReferenceId(taxType.getReferenceId());
            taxTypeHistory.setName(taxType.getName());
            taxTypeHistory.setDescription(taxType.getDescription());
            taxTypeHistory.setActive(taxType.getActive());
            taxTypeHistory.setVersion(taxType.getVersion());
            return taxTypeHistory;
        }
    }
}
