// Copyright 2013 Structure Eng Inc.

package com.structureeng.persistence.model.partner;

import static com.google.common.base.Preconditions.checkNotNull;

import com.structureeng.persistence.model.AbstractSimpleCatalogTenantModel;
import com.structureeng.persistence.model.business.PaymentMethod;
import com.structureeng.persistence.model.business.PaymentTerm;

import java.math.BigDecimal;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

/**
 * Defines a customer {@code BusinessPartner}.
 *
 * @author Edgar Rico (edgar.martinez.rico@gmail.com)
 */
@Entity
@Table(name = "customerPartner", uniqueConstraints = {
   @UniqueConstraint(name = "customerPartnerPK", columnNames = {"idTenant", "idReference"}),
   @UniqueConstraint(name = "customerPartnerUK", columnNames = {"idTenant", "idBusinessPartner"})})
public class CustomerPartner extends AbstractSimpleCatalogTenantModel<Long, String> {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "idCustomerPartner")
    private Long id;

    @JoinColumn(name = "idBusinessPartner", referencedColumnName = "idBusinessPartner",
            nullable = false)
    @ManyToOne(fetch = FetchType.LAZY)
    private BusinessPartner businessPartner;

    @Column(name = "creditLimit", precision = 15, scale = 6, nullable = false)
    private BigDecimal creditLimit;

    @JoinColumn(name = "idPaymentMethod", referencedColumnName = "idRuleOrganization")
    @ManyToOne(fetch = FetchType.LAZY)
    private PaymentMethod paymentMethod;

    @JoinColumn(name = "idPaymentTerm", referencedColumnName = "idRuleOrganization")
    @ManyToOne(fetch = FetchType.LAZY)
    private PaymentTerm paymentTerm;

    @Override
    public Long getId() {
        return id;
    }

    @Override
    public void setId(Long id) {
        this.id = checkPositive(id);
    }

    public BusinessPartner getBusinessPartner() {
        return businessPartner;
    }

    public void setBusinessPartner(BusinessPartner businessPartner) {
        this.businessPartner = checkNotNull(businessPartner);
    }

    public BigDecimal getCreditLimit() {
        return creditLimit;
    }

    public void setCreditLimit(BigDecimal creditLimit) {
        this.creditLimit = checkPositive(creditLimit);
    }

    public PaymentMethod getPaymentMethod() {
        return paymentMethod;
    }

    public void setPaymentMethod(PaymentMethod paymentMethod) {
        this.paymentMethod = paymentMethod;
    }

    public PaymentTerm getPaymentTerm() {
        return paymentTerm;
    }

    public void setPaymentTerm(PaymentTerm paymentTerm) {
        this.paymentTerm = paymentTerm;
    }
}
