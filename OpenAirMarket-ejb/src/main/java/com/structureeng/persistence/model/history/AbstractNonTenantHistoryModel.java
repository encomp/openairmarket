// Copyright 2013 Structure Eng Inc.

package com.structureeng.persistence.model.history;

import com.structureeng.common.DateUtil;
import com.structureeng.persistence.history.HistoryEntity;
import com.structureeng.persistence.history.HistoryType;
import com.structureeng.persistence.model.AbstractActiveModel;

import com.google.common.base.Preconditions;

import java.util.Date;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.MappedSuperclass;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 * Specifies the behavior of the history entities ({@code HistoryNonTenant}) that are 
 * not required to keep tenancy.
 *
 * @author Edgar Rico (edgar.martinez.rico@gmail.com)
 */
@MappedSuperclass
public abstract class AbstractNonTenantHistoryModel extends AbstractActiveModel<Long> implements
        HistoryEntity<HistoryNonTenant> {

    @JoinColumn(name = "idHistoryNonTenant", referencedColumnName = "idHistoryNonTenant",
            nullable = false)
    @ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private HistoryNonTenant nonTenantHistory;

    @Enumerated(EnumType.STRING)
    @Column(name = "historyType", length = 25, nullable = false)
    private HistoryType historyType;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "effectiveStart")
    private Date effectiveStart;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "effectiveEnd")
    private Date effectiveEnd;
    
    @Column(name = "revision", nullable = false)
    private Long revision;

    public HistoryNonTenant getNonTenantHistory() {
        return nonTenantHistory;
    }

    public void setNonTenantHistory(HistoryNonTenant nonTenantHistory) {
        this.nonTenantHistory = nonTenantHistory;
    }

    @Override
    public HistoryType getHistoryType() {
        return historyType;
    }

    @Override
    public void setHistoryType(HistoryType historyType) {
        this.historyType = Preconditions.checkNotNull(historyType);
    }

    @Override
    public Date getEffectiveStart() {
        return DateUtil.clone(effectiveStart);
    }

    @Override
    public void setEffectiveStart(Date effectiveStart) {
        if (effectiveStart != null) {
            this.effectiveStart = DateUtil.clone(effectiveStart);
        }
    }

    @Override
    public Date getEffectiveEnd() {
        return DateUtil.clone(effectiveEnd);
    }

    @Override
    public void setEffectiveEnd(Date effectiveEnd) {
        if (effectiveEnd != null) {
            this.effectiveEnd = DateUtil.clone(effectiveEnd);
        }
    }

    public Long getRevision() {
        return revision;
    }

    public void setRevision(Long revision) {
        this.revision = checkPositive(revision);
    }

    @Override
    public HistoryNonTenant getHistory() {
        return getNonTenantHistory();
    }

    @Override
    public void setHistory(HistoryNonTenant history) {
        setNonTenantHistory(history);
    }
}
