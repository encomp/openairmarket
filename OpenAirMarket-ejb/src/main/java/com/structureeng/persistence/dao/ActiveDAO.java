// Copyright 2013 Structure Eng Inc.

package com.structureeng.persistence.dao;

import com.structureeng.persistence.model.AbstractActiveModel;

import java.io.Serializable;

/**
 * Specifies the contract for all the data access objects for AbstractActive 
 * entities.
 * 
 * @author Edgar Rico (edgar.martinez.rico@gmail.com)
 * @param <T> specifies the {@code AbstractActiveModel} of the data access object
 * @param <S> specifies the {@code Serializable} identifier of the {@code AbstractActiveModel}
 */
public interface ActiveDAO<T extends AbstractActiveModel, S extends Serializable> extends 
        DAO<T, S> {    
}
