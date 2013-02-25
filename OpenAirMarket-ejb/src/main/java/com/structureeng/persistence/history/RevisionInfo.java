// Copyright 2013 Structure Eng Inc.

package com.structureeng.persistence.history;

import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

import java.util.List;
import java.util.Map;

/**
 * Stores the revision's entities for a given transaction.
 *
 * @author Edgar Rico (edgar.martinez.rico@gmail.com)
 */
public class RevisionInfo {

    private final List<HistoryEntity> historyEntities;
    private final Map<Class, Map<HistoryType, History>> historys;

    public RevisionInfo() {
        this.historys = Maps.newConcurrentMap();
        this.historyEntities = Lists.newLinkedList();
    }

    public void add(HistoryEntity historyEntity) {
        historyEntities.add(Preconditions.checkNotNull(historyEntity));
    }

    public List<HistoryEntity> getHistoryEntity() {
        ImmutableList.Builder<HistoryEntity> builder = ImmutableList.builder();
        builder.addAll(historyEntities);
        return builder.build();
    }

    public History getHistory(Class clase, HistoryType historyType) {
        Map<HistoryType, History> map = historys.get(clase);
        if (map != null && map.size() > 0) {
            return map.get(historyType);
        }
        return null;
    }

    public void setHistory(Class clase, HistoryType historyType, History history) {
        Map<HistoryType, History> map = historys.get(clase);
        if (map == null) {
            map = Maps.newConcurrentMap();
            historys.put(clase, map);
        }
        map.put(historyType, history);
    }
}
