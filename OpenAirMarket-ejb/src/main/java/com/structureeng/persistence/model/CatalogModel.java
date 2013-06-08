// Copyright 2013 Structure Eng Inc.

package com.structureeng.persistence.model;

import java.io.Serializable;

/**
 * Specifies the behavior for the entities that are catalogs that required two alternate unique 
 * keys.
 *
 * @author Edgar Rico (edgar.martinez.rico@gmail.com)
 * @param <T> specifies the {@link Class} of the id for the {@link javax.persistence.Entity}.
 * @param <RID> specifies the {@link Class} of the referenceId for the
 *        {@link javax.persistence.Entity}.
 */
public interface CatalogModel <T extends Serializable, RID extends Serializable> extends 
        SimpleCatalogModel<T, RID> {
        
    /**
     * Provides the description for the unique key of this entity on the database.
     * 
     * @return the description assigned
     */
    String getName();
    
    /**
     * Specifies the description for the unique key of this entity on the database.
     * 
     * @param name the description that will be assigned
     */
    void setName(String name);
}
