// Copyright 2013 Structure Eng Inc.

package com.structureeng.persistence.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;

import static com.structureeng.persistence.model.AbstractModel.checkPositive;

/**
 * Specifies the behavior of the entities that are catalogs.
 *
 * @author Edgar Rico (edgar.martinez.rico@gmail.com)
 * @param <T> specifies the {@link Class} of the id for the {@link javax.persistence.Entity}
 */
@MappedSuperclass
public abstract class AbstractCatalogModel <T extends Serializable> extends AbstractActiveModel<T> {

    @Column(name = "idReference", nullable = false)
    private Integer referenceId;
    
    public Integer getReferenceId() {
        return referenceId;
    }

    public void setReferenceId(Integer referenceId) {
        this.referenceId = checkPositive(referenceId);
    }
}
