// Copyright 2013 Structure Eng Inc.

package com.structureeng.persistence.history;

import java.util.Date;

/**
 * Defines the {@code Entity} should be kept the history of this Entity.
 *
 * @author Edgar Rico (edgar.martinez.rico@gmail.com)
 * @param <T> specifies the {@code History}.
 */
public interface HistoryEntity<T extends History> {
    public T getHistory();
    public void setHistory(T history);
    public HistoryType getHistoryType();
    public void setHistoryType(HistoryType historyType);
    public Date getEffectiveStart();
    public void setEffectiveStart(Date effectiveStart);
    public Date getEffectiveEnd();
    public void setEffectiveEnd(Date effectiveEnd);
}

