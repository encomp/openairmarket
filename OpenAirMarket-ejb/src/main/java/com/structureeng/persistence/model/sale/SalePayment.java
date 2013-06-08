// Copyright 2013 Structure Eng Inc.

package com.structureeng.persistence.model.sale;

import com.structureeng.persistence.model.AbstractTenantModel;
import com.structureeng.persistence.model.business.PaymentMethod;

import com.google.common.base.Preconditions;

import java.math.BigDecimal;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

/**
 * Stores they payments for a {@code Sale}.
 *
 * @author Edgar Rico (edgar.martinez.rico@gmail.com)
 */
@Entity
@Table(name = "salePayment", uniqueConstraints = {
        @UniqueConstraint(name = "salePaymentTenantPK",
                columnNames = {"idTenant", "idSale", "idRule"})})
public class SalePayment extends AbstractTenantModel<Long> {

    @Id
    @Column(name = "idSalePayment")
    private Long id;

    @JoinColumn(name = "idSale", referencedColumnName = "idSale", nullable = false)
    @ManyToOne(fetch = FetchType.LAZY)
    private Sale sale;

    @JoinColumn(name = "idRule", referencedColumnName = "idRule", nullable = false)
    @ManyToOne(fetch = FetchType.LAZY)
    private PaymentMethod salePaymentType;

    @Column(name = "amout", precision = 13, scale = 4, nullable = false)
    private BigDecimal amount;

    @Override
    public Long getId() {
        return id;
    }

    @Override
    public void setId(Long id) {
        this.id = Preconditions.checkNotNull(id);
    }

    public Sale getSale() {
        return sale;
    }

    public void setSale(Sale sale) {
        this.sale = Preconditions.checkNotNull(sale);
    }

    public PaymentMethod getSalePaymentType() {
        return salePaymentType;
    }

    public void setSalePaymentType(PaymentMethod salePaymentType) {
        this.salePaymentType = Preconditions.checkNotNull(salePaymentType);
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = Preconditions.checkNotNull(amount);
    }
}
