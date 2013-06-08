// Copyright 2013 Structure Eng Inc.

package com.structureeng.persistence.model.sale;

import com.structureeng.common.DateUtil;
import com.structureeng.persistence.model.AbstractTenantModel;
import com.structureeng.persistence.model.security.SystemUser;

import com.google.common.base.Preconditions;

import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.UniqueConstraint;

/**
 * Define the characteristics of a sale.
 *
 * @author Edgar Rico (edgar.martinez.rico@gmail.com)
 */
@Entity
@Table(name = "sale", uniqueConstraints = {
        @UniqueConstraint(name = "saleTenantPK", columnNames = {"idTenant", "idReference"})})
public class Sale extends AbstractTenantModel<Long> {

    @Id
    @Column(name = "idSale")
    private Long id;

    @Column(name = "idReference", nullable = false)
    private Integer referenceId;

    @Enumerated(EnumType.STRING)
    @Column(name = "saleType", length = 30, nullable = false)
    private SaleType saleType;

    @Column(name = "saleDate", nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date date;

    @Column(name = "amout", precision = 13, scale = 4, nullable = false)
    private BigDecimal amount;

    @Column(name = "tax", precision = 13, scale = 4, nullable = false)
    private BigDecimal tax;

    @Column(name = "total", precision = 13, scale = 4, nullable = false)
    private BigDecimal total;

    @JoinColumn(name = "idSystemUser", referencedColumnName = "idSystemUser", nullable = false)
    @ManyToOne(fetch = FetchType.LAZY)
    private SystemUser systemUser;

    @Override
    public Long getId() {
        return id;
    }

    @Override
    public void setId(Long id) {
        this.id = Preconditions.checkNotNull(id);
    }

    public Integer getReferenceId() {
        return referenceId;
    }

    public void setReferenceId(Integer referenceId) {
        this.referenceId = referenceId;
    }

    public SaleType getSaleType() {
        return saleType;
    }

    public void setSaleType(SaleType saleType) {
        this.saleType = Preconditions.checkNotNull(saleType);
    }

    public Date getDate() {
        return DateUtil.clone(date);
    }

    public void setDate(Date date) {
        this.date = DateUtil.clone(Preconditions.checkNotNull(date));
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = Preconditions.checkNotNull(amount);
    }

    public BigDecimal getTax() {
        return tax;
    }

    public void setTax(BigDecimal tax) {
        this.tax = Preconditions.checkNotNull(tax);
    }

    public BigDecimal getTotal() {
        return total;
    }

    public void setTotal(BigDecimal total) {
        this.total = Preconditions.checkNotNull(total);
    }

    public SystemUser getSystemUser() {
        return systemUser;
    }

    public void setSystemUser(SystemUser systemUser) {
        this.systemUser = Preconditions.checkNotNull(systemUser);
    }
}
