// Copyright 2013 Structure Eng Inc.

package com.structureeng.persistence.dao;

import com.structureeng.persistence.model.CatalogModel;

import java.io.Serializable;

/**
 * Specifies the contract for all the data access objects for {@code AbstractCatalogModel} 
 * entities.
 *
 * @author Edgar Rico (edgar.martinez.rico@gmail.com)
 * @param <T> specifies the {@code AbstractCatalogModel} of the data access object
 * @param <S> specifies the {@code Serializable} identifier of the {@code AbstractCatalogModel}
 */
public interface CatalogDAO<T extends CatalogModel, S extends Serializable, 
        RID extends Serializable> extends ActiveDAO<T, S> {

    /**
     * Find by reference id.
     *
     * @param referenceId the reference id of the instance that will be retrieved.
     * @return the found entity instance or null if the entity does not exist.
     * @throws IllegalArgumentException - if the first argument does not denote an entity type or
     * the second argument is is not a valid type for that entity’s primary key or is null.
     */
    T findByReferenceId(RID referenceId);
    
    /**
     * Find inactive entities by reference id.
     *
     * @param referenceId the reference id of the instance that will be retrieved.
     * @return the found entity instance or null if the entity does not exist.
     * @throws IllegalArgumentException - if the first argument does not denote an entity type or
     * the second argument is is not a valid type for that entity’s primary key or is null.
     */
    T findInactiveByReferenceId(RID referenceId);
}
