// Copyright 2013 Structure Eng Inc.

package com.structureeng.persistence.model.history;

import com.structureeng.persistence.model.SimpleCatalogModel;

import com.google.common.base.Preconditions;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;

/**
 * Specifies the behavior of the history of the entities ({@code SimpleCatalogModel}).
 *
 * @author Edgar Rico (edgar.martinez.rico@gmail.com)
 * @param <RID> specifies the {@link Class} of the referenceId for the
 *        {@link javax.persistence.Entity}.
 */
@MappedSuperclass
public abstract class AbstractHistorySimpleCatalogModel <RID extends Serializable>
        extends AbstractHistoryModel implements SimpleCatalogModel<Long, RID> {

    @Column(name = "idReference", nullable = false)
    private RID referenceId;

    @Override
    public RID getReferenceId() {
        return referenceId;
    }

    @SuppressWarnings("unchecked")
    @Override
    public void setReferenceId(RID referenceId) {
        if (referenceId instanceof Number) {
            checkPositive(Number.class.cast(referenceId));
            this.referenceId = referenceId;
        } else if (referenceId instanceof String) {
            this.referenceId = (RID) checkNotEmpty(String.class.cast(referenceId));
        } else {
            this.referenceId = Preconditions.checkNotNull(referenceId);
        }
    }
}
