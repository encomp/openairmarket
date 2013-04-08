// Copyright 2013 Structure Eng Inc.

package com.structureeng.persistence.model;

import static com.structureeng.persistence.model.AbstractModel.checkNotEmpty;
import static com.structureeng.persistence.model.AbstractModel.checkPositive;

import com.google.common.base.Preconditions;

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
    extends AbstractActiveModel<T> {

    @Column(name = "idReference", nullable = false)
    private RID referenceId;
    
    @Column(name = "name", nullable = false)
    private String name;
    
    public RID getReferenceId() {
        return referenceId;
    }

    @SuppressWarnings("unchecked")
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
    
    public String getName() {
        return name;
    }

    public void setName(String name) {        
        this.name = checkNotEmpty(name);
    }
}
