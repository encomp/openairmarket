// Copyright 2013 Structure Eng Inc.

package com.structureeng.persistence.model.history;

import com.structureeng.common.DateUtil;
import com.structureeng.persistence.history.HistoryEntity;
import com.structureeng.persistence.history.HistoryType;

import com.google.common.base.Preconditions;
import com.structureeng.persistence.model.AbstractActiveModel;

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
public abstract class AbstractHistoryModel extends AbstractActiveModel<Long> implements
        HistoryEntity<Audit> {

    @JoinColumn(name = "idAudit", referencedColumnName = "idAudit",
            nullable = false)
    @ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private Audit audit;

    @Enumerated(EnumType.STRING)
    @Column(name = "historyType", length = 25, nullable = false)
    private HistoryType historyType;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "effectiveStart")
    private Date effectiveStart;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "effectiveEnd")
    private Date effectiveEnd;

    public Audit getAudit() {
        return audit;
    }

    public void setAudit(Audit audit) {
        this.audit = Preconditions.checkNotNull(audit);
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

    @Override
    public Audit getHistory() {
        return getAudit();
    }

    @Override
    public void setHistory(Audit history) {
        setAudit(history);
    }
}
