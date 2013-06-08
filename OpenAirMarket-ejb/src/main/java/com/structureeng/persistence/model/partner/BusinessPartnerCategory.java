// Copyright 2013 Structure Eng Inc.

package com.structureeng.persistence.model.partner;

import static com.google.common.base.Preconditions.checkNotNull;

import com.structureeng.persistence.model.business.Organization;
import com.structureeng.persistence.model.business.RuleOrganization;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

/**
 * Defines the different types of {@code BusinessPartner}s categories per {@code Organization}.
 *
 * @author Edgar Rico (edgar.martinez.rico@gmail.com)
 */
@Entity
@DiscriminatorValue("BUSINESS_PARTNER_CATEGORY")
public class BusinessPartnerCategory extends RuleOrganization {

    /**
     * Creates a new {@code BusinessPartnerCategory.Builder} instance.
     *
     * @return - new instance
     */
    public static Buider newBuilder() {
        return new Buider();
    }

    /**
     * Builder class that creates instances of {@code BusinessPartnerCategory}.
     *
     * @author Edgar Rico (edgar.martinez.rico@gmail.com)
     */
    public static class Buider {

        private String referenceId;
        private String name;
        private Organization organization;
        private Boolean defaulted;

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

        public Buider setDefaulted(Boolean defaulted) {
            this.defaulted = checkNotNull(defaulted);
            return this;
        }

        /**
         * Creates a new instance of {@code BusinessPartnerCategory}.
         *
         * @return - new instance
         */
        public BusinessPartnerCategory build() {
            BusinessPartnerCategory businessPartnerCategory = new BusinessPartnerCategory();
            businessPartnerCategory.setReferenceId(referenceId);
            businessPartnerCategory.setName(name);
            businessPartnerCategory.setOrganization(organization);
            businessPartnerCategory.setDefaulted(defaulted);
            return businessPartnerCategory;
        }
    }
}
