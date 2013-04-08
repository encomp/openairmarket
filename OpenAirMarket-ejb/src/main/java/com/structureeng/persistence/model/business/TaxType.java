// Copyright 2013 Structure Eng Inc.

package com.structureeng.persistence.model.business;

import com.structureeng.persistence.history.HistoryListener;
import com.structureeng.persistence.history.Revision;
import com.structureeng.persistence.model.history.business.TaxTypeHistory;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;

/**
 * Define the different types of taxes.
 *
 * @author Edgar Rico (edgar.martinez.rico@gmail.com)
 */
@EntityListeners(value = {HistoryListener.class})
@Revision(builder = TaxTypeHistory.Builder.class)
@Entity
@DiscriminatorValue("TAX_TYPE")
public class TaxType extends Rule {

    /**
     * Creates a new {@code TaxType.Builder} instance.
     *
     * @return - new instance
     */
    public static TaxType.Buider newBuilder() {
        return new TaxType.Buider();
    }

    /**
     * Builder class that creates instances of {@code TaxType}.
     *
     * @author Edgar Rico (edgar.martinez.rico@gmail.com)
     */
    public static class Buider {

        private Integer referenceId;
        private String name;
        private String description;

        public TaxType.Buider setReferenceId(Integer referenceId) {
            this.referenceId = checkPositive(referenceId);
            return this;
        }

        public TaxType.Buider setName(String name) {
            this.name = checkNotEmpty(name);
            return this;
        }

        public TaxType.Buider setDescription(String description) {
            this.description = checkNotEmpty(description);
            return this;
        }

        /**
         * Creates a new instance of {@code TaxType}.
         *
         * @return - new instance
         */
        public TaxType build() {
            TaxType taxType = new TaxType();
            taxType.setReferenceId(referenceId);
            taxType.setName(name);
            taxType.setDescription(description);
            return taxType;
        }
    }
}
