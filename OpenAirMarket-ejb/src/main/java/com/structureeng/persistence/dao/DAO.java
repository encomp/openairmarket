// Copyright 2013 Structure Eng Inc.

package com.structureeng.persistence.dao;

import com.structureeng.persistence.model.Model;

import java.io.Serializable;
import java.util.List;

import javax.persistence.LockModeType;

/**
 * Specifies the contract for all the data access objects.
 * 
 * @author Edgar Rico (edgar.martinez.rico@gmail.com)
 * @param <T> specifies the {@code Model} of the data access object
 * @param <S> specifies the {@code Serializable} identifier of the {@code Model}
 */
public interface DAO<T extends Model, S extends Serializable> {

    /**
     * Persist the given entity.
     * 
     * @param entity the instance that will be persisted.
     * @throws DAOException -in case of errors; will be propagated to the caller.
     */
    void persist(T entity) throws DAOException;

    /**
     * Merge the given entity.
     * 
     * @param entity the instance that will be merged.
     * @throws DAOException - in case of errors; will be propagated to the caller.
     */
    void merge(T entity) throws DAOException;

    /**
     * Removed the given entity.
     * 
     * @param entity the instance that will be merged.
     * @throws DAOException - in case of errors; will be propagated to the caller.
     */
    void remove(T entity) throws DAOException;

    /**
     * Refresh the given entity.
     * 
     * @param entity the instance that will be merged.
     */
    void refresh(T entity);

    /**
     * Refresh the state of the instance from the database, overwriting changes made to 
     * the entity, if any, and lock it with respect to given lock mode type.
     * 
     * @param entity - instance
     * @param modeType - lock mode
     */
    void refresh(T entity, LockModeType modeType);
    
    /**
     * Find by primary key.
     * 
     * @param id the primary key of the instance that will be retrieved.
     * @return the found entity instance or null if the entity does not exist.
     * @throws IllegalArgumentException - if the first argument does not denote an entity type or 
     *          the second argument is is not a valid type for that entity’s primary key or is null.
     */
    T find(S id);

    /**
     * Retrieves an instance by a particular version.
     * 
     * @param id the primary key of the instance that will be retrieved.
     * @param version the particular version of the entity that is being requested.
     * @return - instance
     * @throws DAOException - in case of errors; will be propagated to the caller.
     */
    T find(S id, long version) throws DAOException;

    /**
     * Retrieves a {@code List} of entities from a particular start point.
     * 
     * @param start - specifies the start count.
     * @param count - specifies the number of entities that will be retrieved from the page.
     * @return the {@code List} of entities found or an empty list.
     */
    List<T> findRange(int start, int count);

    /**
     * Count the number of instances in the persistent storage.
     * 
     * @return the number of entities.
     */
    long count();

    /**
     * Synchronize the persistence context to the underlying database.
     */
    void flush();

    /**
     * Verifies if a particular entity has been modified by another transaction.
     * 
     * @param entity - the instance that will be verified.
     * @return true if it has been changed by another transaction otherwise else.
     * @throws DAOException - in case of errors; will be propagated to the caller.
     */
    boolean hasVersionChanged(T entity) throws DAOException;
}
