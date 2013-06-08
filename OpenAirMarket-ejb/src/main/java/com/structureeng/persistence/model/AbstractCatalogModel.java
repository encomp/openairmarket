// Copyright 2013 Structure Eng Inc.

package com.structureeng.persistence.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;

/**
 * Specifies the behavior of the entities that are catalogs.
 *
 * @author Edgar Rico (edgar.martinez.rico@gmail.com)
 * @param <T> specifies the {@link Class} of the id for the {@link javax.persistence.Entity}.
 * @param <RID> specifies the {@link Class} of the referenceId for the
 *        {@link javax.persistence.Entity}.
 */
@MappedSuperclass
public abstract class AbstractCatalogModel <T extends Serializable, RID extends Serializable>
    extends AbstractSimpleCatalogModel<T, RID> implements CatalogModel<T, RID> {

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
