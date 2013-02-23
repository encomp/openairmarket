// Copyright 2013 Structure Eng Inc.

package com.structureeng.persistence.model.history;

import com.structureeng.common.DateUtil;
import com.structureeng.persistence.history.HistoryEntity;
import com.structureeng.persistence.history.HistoryType;
import com.structureeng.persistence.model.AbstractTenantModel;

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
 * Specifies the behavior of the history of the entities ({@code HistoryTenant}) that 
 * are required to keep tenancy.
 *
 * @author Edgar Rico (edgar.martinez.rico@gmail.com)
 */
@MappedSuperclass
public abstract class AbstractTenantHistoryModel extends AbstractTenantModel<Long> implements
        HistoryEntity<HistoryTenant> {

    @JoinColumn(name = "idHistoryTenant", referencedColumnName = "idHistoryTenant",
            nullable = false)
    @ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private HistoryTenant historyTenant;

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

    public HistoryTenant getHistoryTenant() {
        return historyTenant;
    }

    public void setHistoryTenant(HistoryTenant historyTenant) {
        this.historyTenant = Preconditions.checkNotNull(historyTenant);
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
    public HistoryTenant getHistory() {
        return getHistoryTenant();
    }

    @Override
    public void setHistory(HistoryTenant history) {
        setHistoryTenant(history);
    }
}
