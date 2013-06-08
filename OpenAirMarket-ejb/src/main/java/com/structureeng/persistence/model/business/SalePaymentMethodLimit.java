// Copyright 2013 Structure Eng Inc.

package com.structureeng.persistence.model.business;

import static com.google.common.base.Preconditions.checkNotNull;
import static com.google.common.base.Preconditions.checkState;

import com.google.common.collect.ImmutableList;

import java.math.BigDecimal;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;

import javax.persistence.CascadeType;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;

/**
 * Define the different types of payment methods. This drives the business rules for a particular
 * payment method.
 *
 * @author Edgar Rico (edgar.martinez.rico@gmail.com)
 */
@Entity
@DiscriminatorValue("SALE_PAYMENT_METHOD_LIMIT")
public class SalePaymentMethodLimit extends RuleOrganization {

    public void setPaymentMethod(PaymentMethod paymentMethod) {
        this.setParentRule(checkNotNull(paymentMethod));
    }

    public PaymentMethod getPaymentMethod() {
        return PaymentMethod.class.cast(getParentRule());
    }

    @OneToMany(mappedBy = "ruleOrganization", fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @OrderBy(value = "name")
    private List<RuleOrganizationDetail> ruleDetails;

    private List<RuleOrganizationDetail> getRuleDetails() {
        return ruleDetails;
    }

    private void setRuleDetails(List<RuleOrganizationDetail> ruleDetails) {
        this.ruleDetails = checkNotNull(ruleDetails);
    }

    /**
     * Specifies the attributes of a {@code SalePaymentMethodLimit}.
     *
     * @author Edgar Rico (edgar.martinez.rico@gmail.com)
     */
    public static enum PaymentMethodAttributes {

        /**
         * Specifies the minimum amount required to use a {@code PaymentMethod}.
         */
        MINIMUM_AMOUNT,

        /**
         * Specifies the maximum amount required to use a {@code PaymentMethod}.
         */
        MAXIMUM_AMOUNT
    }

    /**
     * Defines a new {@code SalePaymentMethodLimit} rule.
     *
     * @author Edgar Rico (edgar.martinez.rico@gmail.com)
     */
    public static class Builder {

        private static final PaymentMethodAttributes ORDER[] = new PaymentMethodAttributes[]
                {PaymentMethodAttributes.MAXIMUM_AMOUNT, PaymentMethodAttributes.MINIMUM_AMOUNT};
        private final Map<PaymentMethodAttributes, BigDecimal> paymentAttributes =
                new EnumMap<PaymentMethodAttributes, BigDecimal>(PaymentMethodAttributes.class);
        private String referenceId;
        private String name;
        private PaymentMethod paymentMethod;

        public String getReferenceId() {
            return referenceId;
        }

        public Builder setReferenceId(String referenceId) {
            this.referenceId = checkNotEmpty(referenceId);
            return this;
        }

        public String getName() {
            return name;
        }

        public Builder setName(String name) {
            this.name = checkNotNull(name);
            return this;
        }

        public PaymentMethod getPaymentMethod() {
            return paymentMethod;
        }

        public Builder setPaymentMethod(PaymentMethod paymentMethod) {
            this.paymentMethod = checkNotNull(paymentMethod);
            return this;
        }

        public Builder setMinimumAmount(BigDecimal amount) {
            setAmount(PaymentMethodAttributes.MINIMUM_AMOUNT, amount);
            return this;
        }

        public Builder setMaximumAmount(BigDecimal amount) {
            setAmount(PaymentMethodAttributes.MAXIMUM_AMOUNT, amount);
            return this;
        }

        private Builder setAmount(final PaymentMethodAttributes paymentAttribute,
                                  final BigDecimal amount) {
            if (paymentAttributes.containsKey(paymentAttribute)) {
                paymentAttributes.remove(paymentAttribute);
            }
            paymentAttributes.put(paymentAttribute, amount);
            return this;
        }

        /**
         * Creates a new {@code SalePaymentMethodLimit} rule.
         *
         * @return a new rule.
         */
        public SalePaymentMethodLimit build() {
            checkNotNull(referenceId);
            checkNotNull(name);
            checkNotNull(paymentMethod);
            checkState(paymentAttributes.size() == 2);
            SalePaymentMethodLimit salePaymentMethod = new SalePaymentMethodLimit();
            salePaymentMethod.setReferenceId(referenceId);
            salePaymentMethod.setName(name);
            salePaymentMethod.setPaymentMethod(paymentMethod);
            ImmutableList.Builder<RuleOrganizationDetail> ruleDetails = ImmutableList.builder();
            for (PaymentMethodAttributes paymentAttribute : ORDER) {
                RuleOrganizationDetail ruleDetailOrganization = new RuleOrganizationDetail();
                ruleDetailOrganization.setRuleOrganization(salePaymentMethod);
                ruleDetailOrganization.setReferenceId(paymentAttribute.toString());
                ruleDetailOrganization.setValue(paymentAttributes.get(paymentAttribute));
                ruleDetails.add(ruleDetailOrganization);
            }
            salePaymentMethod.setRuleDetails(ruleDetails.build());
            return salePaymentMethod;
        }
    }

    public BigDecimal getMaximumAmount() {
        return checkMaximumAmount().getBigDecimalValue();
    }

    public void setMaximumAmount(BigDecimal amount) {
        RuleOrganizationDetail ruleDetail = checkMaximumAmount();
        ruleDetail.setValue(amount);
    }

    public BigDecimal getMinimumAmount() {
        return checkMinimumAmount().getBigDecimalValue();
    }

    public void setMinimumAmount(BigDecimal amount) {
        RuleOrganizationDetail ruleDetail = checkMinimumAmount();
        ruleDetail.setValue(amount);
    }

    private RuleOrganizationDetail checkMaximumAmount() {
        return checkCommon(0, PaymentMethodAttributes.MAXIMUM_AMOUNT);
    }

    private RuleOrganizationDetail checkMinimumAmount() {
        return checkCommon(1, PaymentMethodAttributes.MINIMUM_AMOUNT);
    }

    private RuleOrganizationDetail checkCommon(int index, PaymentMethodAttributes attribute) {
        checkNotNull(ruleDetails);
        checkState(ruleDetails.size() == 2);
        RuleOrganizationDetail ruleDetail = ruleDetails.get(index);
        checkState(attribute.toString().equals(ruleDetail.getReferenceId()));
        return ruleDetail;
    }
}
