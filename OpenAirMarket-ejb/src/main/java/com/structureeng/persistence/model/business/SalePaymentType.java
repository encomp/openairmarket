// Copyright 2013 Structure Eng Inc.

package com.structureeng.persistence.model.business;

import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableList;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.CascadeType;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;

/**
 * Define the different types of payments. This drives the business rules for a particular payment
 * type.
 *
 * @author Edgar Rico (edgar.martinez.rico@gmail.com)
 */
@Entity
@DiscriminatorValue("SALE_PAYMENT_TYPE")
public class SalePaymentType extends Rule {

    /**
     * Specifies the attributes of a {@code SalePaymentType}.
     *
     * @author Edgar Rico (edgar.martinez.rico@gmail.com)
     */
    public enum PaymentAttributes {
        MINIMUM_AMOUNT, MAXIMUM_AMOUNT
    }

    /**
     * Defines a new {@code SalePaymentType} rule.
     *
     * @author Edgar Rico (edgar.martinez.rico@gmail.com)
     */
    public static class Builder {

        private static final PaymentAttributes ORDER[] = new PaymentAttributes[]
                {PaymentAttributes.MAXIMUM_AMOUNT, PaymentAttributes.MINIMUM_AMOUNT};
        private final Map<PaymentAttributes, BigDecimal> paymentAttributes =
                new HashMap<PaymentAttributes, BigDecimal>();
        private Integer referenceId;
        private String name;
        private String description;

        public Integer getReferenceId() {
            return referenceId;
        }

        public Builder setReferenceId(Integer referenceId) {
            this.referenceId = Preconditions.checkNotNull(referenceId);
            return this;
        }

        public String getName() {
            return name;
        }

        public Builder setName(String name) {
            this.name = Preconditions.checkNotNull(name);
            return this;
        }

        public String getDescription() {
            return description;
        }

        public Builder setDescription(String description) {
            this.description = Preconditions.checkNotNull(description);
            return this;
        }

        public Builder setMinimumAmount(BigDecimal amount) {
            setAmount(PaymentAttributes.MINIMUM_AMOUNT, amount);
            return this;
        }

        public Builder setMaximumAmount(BigDecimal amount) {
            setAmount(PaymentAttributes.MAXIMUM_AMOUNT, amount);
            return this;
        }

        private Builder setAmount(final PaymentAttributes paymentAttribute,
                                  final BigDecimal amount) {
            if (paymentAttributes.containsKey(paymentAttribute)) {
                paymentAttributes.remove(paymentAttribute);
            }
            paymentAttributes.put(paymentAttribute, amount);
            return this;
        }

        /**
         * Creates a new {@code SalePaymentType} rule.
         *
         * @return a new rule.
         */
        public SalePaymentType build() {
            Preconditions.checkNotNull(referenceId);
            Preconditions.checkNotNull(name);
            Preconditions.checkNotNull(description);
            Preconditions.checkState(paymentAttributes.size() == 2);
            SalePaymentType salePaymentType = new SalePaymentType();
            salePaymentType.setReferenceId(referenceId);
            salePaymentType.setName(name);
            salePaymentType.setDescription(description);
            ImmutableList.Builder<RuleDetail> ruleDetails = ImmutableList.builder();
            for (PaymentAttributes paymentAttribute : ORDER) {
                RuleDetail ruleDetail = new RuleDetail();
                ruleDetail.setRule(salePaymentType);
                ruleDetail.setReferenceId(paymentAttribute.toString());
                ruleDetail.setValue(paymentAttributes.get(paymentAttribute));
                ruleDetails.add(ruleDetail);
            }
            salePaymentType.setRuleDetails(ruleDetails.build());
            return salePaymentType;
        }
    }

    @OneToMany(mappedBy = "rule", fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @OrderBy(value = "name")
    private List<RuleDetail> ruleDetails;

    private List<RuleDetail> getRuleDetails() {
        return ruleDetails;
    }

    private void setRuleDetails(List<RuleDetail> ruleDetails) {
        this.ruleDetails = Preconditions.checkNotNull(ruleDetails);
    }

    public BigDecimal getMaximumAmount() {
        return checkMaximumAmount().getBigDecimalValue();
    }

    public void setMaximumAmount(BigDecimal amount) {
        RuleDetail ruleDetail = checkMaximumAmount();
        ruleDetail.setValue(amount);
    }

    public BigDecimal getMinimumAmount() {
        return checkMinimumAmount().getBigDecimalValue();
    }

    public void setMinimumAmount(BigDecimal amount) {
        RuleDetail ruleDetail = checkMinimumAmount();
        ruleDetail.setValue(amount);
    }

    private RuleDetail checkMaximumAmount() {
        return checkCommon(0, PaymentAttributes.MAXIMUM_AMOUNT);
    }

    private RuleDetail checkMinimumAmount() {
        return checkCommon(1, PaymentAttributes.MINIMUM_AMOUNT);
    }

    private RuleDetail checkCommon(int index, PaymentAttributes paymentAttribute) {
        Preconditions.checkNotNull(ruleDetails);
        Preconditions.checkState(ruleDetails.size() == 2);
        RuleDetail ruleDetail = ruleDetails.get(index);
        Preconditions.checkState(paymentAttribute.toString().equals(ruleDetail.getReferenceId()));
        return ruleDetail;
    }
}
