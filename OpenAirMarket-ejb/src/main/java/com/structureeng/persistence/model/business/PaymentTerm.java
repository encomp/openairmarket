// Copyright 2013 Structure Eng Inc.

package com.structureeng.persistence.model.business;

import com.structureeng.persistence.model.partner.CustomerPartner;
import com.google.common.base.Preconditions;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;

/**
 * Define the different types of terms for a payment. This drives the business rules for a
 * particular terms of a payment.
 *
 * @author Edgar Rico (edgar.martinez.rico@gmail.com)
 */
@Entity
@DiscriminatorValue("PAYMENT_TERM")
public class PaymentTerm extends RuleOrganization {
    
    @JoinColumn(name = "idCustomerPartner", referencedColumnName = "idCustomerPartner", 
            nullable = false)
    @ManyToOne(fetch = FetchType.LAZY)
    private CustomerPartner customerPartner;

    @OneToMany(mappedBy = "ruleOrganization", fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @OrderBy(value = "name")
    private List<RuleOrganizationDetail> ruleDetails;

    private List<RuleOrganizationDetail> getRuleDetails() {
        return ruleDetails;
    }

    private void setRuleDetails(List<RuleOrganizationDetail> ruleDetails) {
        this.ruleDetails = Preconditions.checkNotNull(ruleDetails);
    }

    /**
     * Specifies the attributes of a {@code PaymentMethod}.
     *
     * @author Edgar Rico (edgar.martinez.rico@gmail.com)
     */
    public enum PaymentTermAttributes {
        /**
         * Specifies the number of days of a payment term.
         */
        DUE_PAYMENT_DAYS,

        /**
         * Specifies if a payment need to be paid on exact date for the end of the term considering
         * that the due date is a weekend or holiday.
         */
        FIXED_DUE_DATE,

        /**
         * Specifies if a payment need to be paid next business day if the due date is a weekend or
         * holiday.
         */
        NEXT_BUSINESS_DAY,

        /**
         * Specifies that a payment needs to be paid the closest to the due date of a given week
         * day.
         */
        FIXED_DAY_WEEK,

        /**
         * Specifies that a payment term is the default if is not specified.
         */
        DEFAULTED
    }

    /**
     * Defines a new {@code PaymentTerm} rule.
     *
     * @author Edgar Rico (edgar.martinez.rico@gmail.com)
     */
    public static class Builder {
    }
}
