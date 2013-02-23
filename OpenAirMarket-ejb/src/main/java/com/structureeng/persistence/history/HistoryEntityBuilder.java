// Copyright 2013 Structure Eng Inc.

package com.structureeng.persistence.history;

import com.structureeng.persistence.model.AbstractModel;

/**
 * Specifies the behavior of a builder class that creates a {@code HistoryEntity}.
 *
 * @author Edgar Rico (edgar.martinez.rico@gmail.com)
 * @param <E> specifies the {@code javax.persistence.Entity}.
 * @param <HE> specifies the {@code HistoryEntity}.
 */
public abstract class HistoryEntityBuilder<E extends AbstractModel, HE extends HistoryEntity> {
    public abstract HE build(E entity);
}
