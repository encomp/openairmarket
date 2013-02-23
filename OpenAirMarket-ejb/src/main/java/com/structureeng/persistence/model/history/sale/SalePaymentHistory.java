// Copyright 2013 Structure Eng Inc.

package com.structureeng.persistence.model.history.sale;

import com.structureeng.persistence.model.business.SalePaymentType;
import com.structureeng.persistence.model.history.AbstractTenantHistoryModel;
import com.structureeng.persistence.model.sale.Sale;
import com.structureeng.persistence.model.sale.SalePayment;

import com.google.common.base.Preconditions;

import java.math.BigDecimal;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

/**
 * Define the different companies of a {@code SalePayment}.
 *
 * @author Edgar Rico (edgar.martinez.rico@gmail.com)
 */
@Entity
@Table(name = "salePaymentHistory", uniqueConstraints = {
        @UniqueConstraint(name = "salePaymentHistoryUK",
                columnNames = {"idSalePayment", "idHistoryTenant"})})
public class SalePaymentHistory extends AbstractTenantHistoryModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "idSalePaymentHistory")
    private Long id;

    @JoinColumn(name = "idSalePayment", referencedColumnName = "idSalePayment", nullable = false)
    @ManyToOne(cascade = CascadeType.REFRESH)
    private SalePayment salePayment;

    @JoinColumn(name = "idSale", referencedColumnName = "idSale", nullable = false)
    @ManyToOne(cascade = CascadeType.REFRESH)
    private Sale sale;

    @JoinColumn(name = "idRule", referencedColumnName = "idRule", nullable = false)
    @ManyToOne(cascade = CascadeType.REFRESH)
    private SalePaymentType salePaymentType;

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

    public SalePayment getSalePayment() {
        return salePayment;
    }

    public void setSalePayment(SalePayment salePayment) {
        this.salePayment = Preconditions.checkNotNull(salePayment);
    }

    public Sale getSale() {
        return sale;
    }

    public void setSale(Sale sale) {
        this.sale = Preconditions.checkNotNull(sale);
    }

    public SalePaymentType getSalePaymentType() {
        return salePaymentType;
    }

    public void setSalePaymentType(SalePaymentType salePaymentType) {
        this.salePaymentType = Preconditions.checkNotNull(salePaymentType);
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = Preconditions.checkNotNull(amount);
    }
}
