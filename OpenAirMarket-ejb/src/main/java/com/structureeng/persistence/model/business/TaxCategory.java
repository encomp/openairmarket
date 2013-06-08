// Copyright 2013 Structure Eng Inc.

package com.structureeng.persistence.model.business;

import static com.google.common.base.Preconditions.checkNotNull;

import com.structureeng.persistence.history.HistoryListener;
import com.structureeng.persistence.history.Revision;
import com.structureeng.persistence.model.history.business.TaxCategoryHistory;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;

/**
 * Define the different types of taxes.
 *
 * @author Edgar Rico (edgar.martinez.rico@gmail.com)
 */
@EntityListeners(value = {HistoryListener.class})
@Revision(builder = TaxCategoryHistory.Builder.class)
@Entity
@DiscriminatorValue("TAX_CATEGORY")
public class TaxCategory extends RuleOrganization {

    /**
     * Creates a new {@code TaxCategory.Builder} instance.
     *
     * @return - new instance
     */
    public static Buider newBuilder() {
        return new TaxCategory.Buider();
    }

    /**
     * Builder class that creates instances of {@code TaxCategory}.
     *
     * @author Edgar Rico (edgar.martinez.rico@gmail.com)
     */
    public static class Buider {

        private String referenceId;
        private String name;
        private Organization organization;

        public Buider setReferenceId(String referenceId) {
            this.referenceId = checkNotEmpty(referenceId);
            return this;
        }

        public Buider setName(String name) {
            this.name = checkNotEmpty(name);
            return this;
        }

        public Buider setOrganization(Organization organization) {
            this.organization = checkNotNull(organization);
            return this;
        }

        /**
         * Creates a new instance of {@code TaxCategory}.
         *
         * @return - new instance
         */
        public TaxCategory build() {
            TaxCategory taxType = new TaxCategory();
            taxType.setReferenceId(referenceId);
            taxType.setName(name);
            taxType.setOrganization(organization);
            return taxType;
        }
    }
}
