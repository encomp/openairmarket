// Copyright 2013 Structure Eng Inc.

package com.structureeng.persistence.dao;

import com.structureeng.persistence.model.AbstractActiveModel;

import java.io.Serializable;

/**
 * Specifies the contract for all the data access objects for {@code AbstractActive}
 * entities.
 * 
 * @author Edgar Rico (edgar.martinez.rico@gmail.com)
 * @param <T> specifies the {@code AbstractActiveModel} of the data access object
 * @param <S> specifies the {@code Serializable} identifier of the {@code AbstractActiveModel}
 */
public interface ActiveDAO<T extends AbstractActiveModel, S extends Serializable> extends 
        DAO<T, S> {
    
    /**
     * Count the number of instances in the persistent storage that are inactive.
     * 
     * @return the number of inactive entities.
     */
    long countInactive();
    //TODO (edgarrico) the active DAO should validate that associations among objects should have 
    //the same status meaning if an instance is Active all its associated objects has to be active
}
