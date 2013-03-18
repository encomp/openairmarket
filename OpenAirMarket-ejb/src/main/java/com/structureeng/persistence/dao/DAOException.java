// Copyright 2013 Structure Eng Inc.

package com.structureeng.persistence.dao;

import com.structureeng.common.exception.ErrorCode;
import com.structureeng.common.exception.ErrorPropertyProvider;
import com.structureeng.common.exception.Excepcion;
import java.util.ResourceBundle;

import javax.inject.Inject;
import javax.inject.Named;

/**
 * Base data access object exception.
 *
 * @author Edgar Rico (edgar.martinez.rico@gmail.com)
 */
public class DAOException extends Excepcion {

    public DAOException(ErrorCode errorCode) {
        super(errorCode);
    }

    public DAOException(ErrorCode errorCode, String message) {
        super(errorCode, message);
    }

    public DAOException(ErrorCode errorCode, Throwable throwable) {
        super(errorCode, throwable);
    }

    /**
     * Builder that creates instances of {@code DAOException}.
     *
     * @author Edgar Rico (edgar.martinez.rico@gmail.com)
     */
    public static class Builder {

        private static ResourceBundle resourceBundle;

        public static DAOException build(ErrorPropertyProvider errorCode) {
            ErrorCode error = ErrorCode.newBuilder().build(errorCode.get(), resourceBundle);
            DAOException dAOException = new DAOException(error);
            return dAOException;
        }

        public static DAOException build(DAOErrorCode errorCode, Object... params) {
            ErrorCode error = ErrorCode.newBuilder().build(errorCode.get(), resourceBundle);
            String message = String.format(error.getDescription(), params);
            DAOException dAOException = new DAOException(error, message);
            return dAOException;
        }

        public static DAOException build(DAOErrorCode errorCode, Throwable throwable) {
            ErrorCode error = ErrorCode.newBuilder().build(errorCode.get(), resourceBundle);
            DAOException dAOException = new DAOException(error, throwable);
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
