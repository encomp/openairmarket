// Copyright 2013 Structure Eng Inc.

package com.structureeng.persistence.model.history;

import com.structureeng.persistence.model.CatalogModel;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;

/**
 * Specifies the behavior of the history of the entities ({@code CatalogModel}).
 *
 * @author Edgar Rico (edgar.martinez.rico@gmail.com)
 * @param <RID> specifies the {@link Class} of the referenceId for the
 *        {@link javax.persistence.Entity}.
 */
@MappedSuperclass
public abstract class AbstractHistoryCatalogModel<RID extends Serializable>
        extends AbstractHistorySimpleCatalogModel<RID> implements CatalogModel<Long, RID> {

    @Column(name = "name", nullable = false)
    private String name;

    @Override
    public String getName() {
        return name;
    }

    @Override
    public void setName(String name) {
        this.name = checkNotEmpty(name);
    }
}
