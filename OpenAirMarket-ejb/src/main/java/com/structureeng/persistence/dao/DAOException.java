// Copyright 2013 Structure Eng Inc.

package com.structureeng.persistence.dao;

import java.util.ResourceBundle;

import javax.inject.Inject;
import javax.inject.Named;

/**
 * Base data access object exception.
 * 
 * @author Edgar Rico (edgar.martinez.rico@gmail.com)
 */
public class DAOException extends Exception {

    private String field;

    public DAOException(String message) {
        super(message);
    }

    public DAOException(String message, Throwable throwable) {
        super(message, throwable);
    }

    /**
     * @return the field
     */
    public String getField() {
        return field;
    }

    /**
     * @param field the field to set
     */
    public void setField(String field) {
        this.field = field;
    }

    /**
     * Builder that creates instances of {@code DAOException}.
     * 
     * @author Edgar Rico (edgar.martinez.rico@gmail.com)
     */
    public static class Builder {
        
        private static ResourceBundle resourceBundle;
        
        public static DAOException build(String field, String message) {
            DAOException dAOException = new DAOException(getResourceBundle().getString(message));
            dAOException.setField(getResourceBundle().getString(field));
            return dAOException;
        }

        public static DAOException build(String message, Object... params) {
            message = getResourceBundle().getString(message);
            DAOException dAOException = new DAOException(String.format(message, params));
            return dAOException;
        }

        public static DAOException build(String field, String message, Throwable throwable) {
            DAOException dAOException = new DAOException(getResourceBundle().getString(message), 
                    throwable);
            dAOException.setField(getResourceBundle().getString(field));
            return dAOException;
        }

        public static ResourceBundle getResourceBundle() {
            return resourceBundle;
        }

        @Inject
        @Named("daoResourceBundle")
        public static void setResourceBundle(ResourceBundle aResourceBundle) {
            resourceBundle = aResourceBundle;
        }
    }    
}
