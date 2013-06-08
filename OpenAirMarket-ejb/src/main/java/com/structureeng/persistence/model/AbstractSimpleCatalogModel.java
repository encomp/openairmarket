// Copyright 2013 Structure Eng Inc.

package com.structureeng.persistence.model;

import com.google.common.base.Preconditions;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;

/**
 * Specifies the behavior of the entities that required an alternate primary key.
 *
 * @author Edgar Rico (edgar.martinez.rico@gmail.com)
 * @param <T> specifies the {@link Class} of the id for the {@link javax.persistence.Entity}.
 * @param <RID> specifies the {@link Class} of the referenceId for the
 *        {@link javax.persistence.Entity}.
 */
@MappedSuperclass
public abstract class AbstractSimpleCatalogModel <T extends Serializable, RID extends Serializable>
    extends AbstractActiveModel<T> implements SimpleCatalogModel<T, RID> {

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
