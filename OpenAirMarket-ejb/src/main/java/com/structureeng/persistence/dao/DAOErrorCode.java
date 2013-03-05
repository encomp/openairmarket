// Copyright 2013 Structure Eng Inc.

package com.structureeng.persistence.dao;

import com.structureeng.common.exception.ErrorCodeProperty;

/**
 * Defines the error codes for the {@code DAO}.
 *
 * @author Edgar Rico (edgar.martinez.rico@gmail.com)
 */
public enum DAOErrorCode {
    /**
     * Property that specifies the error in case the primary key passed is not found.
     */
    NO_RESULT("dao.noResult"),
    /**
     * Property that specifies the error in case the persistence layer failed.
     */
    PERSISTENCE("dao.persistence"),
    /**
     * Property that specifies the error message in case the entity has been modified recently by
     * another transaction.
     */
    OPRIMISTIC_LOCKING("dao.optimisticLocking"),
    /**
     * Property that specifies the error message in case that the primary key queried failed
     * unexpectedly.
     */
    UNEXPECTED("dao.unexpected"),
    /**
     * Property that specifies the error in case the unique has been violated for the reference id
     * field for an entity that is a catalog.
     */
    CATALOG_REFERENCE_ID_UK("dao.catalog.referenceId.UK"),
    /**
     * Property that specifies the error in case the unique has been violated for the name field 
     * for an entity that is a catalog.
     */
    CATALOG_NAME_UK("dao.catalog.name.UK");

    private final ErrorCodeProperty errorCode;

    private DAOErrorCode(String property) {
        this.errorCode = new ErrorCodeProperty(property);
    }

    public ErrorCodeProperty getErrorCode() {
        return errorCode;
    }
}
