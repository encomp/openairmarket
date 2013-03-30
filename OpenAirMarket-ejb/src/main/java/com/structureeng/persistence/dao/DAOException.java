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

    protected DAOException(ErrorCode errorCode) {
        super(errorCode);
    }

    protected DAOException(String message, ErrorCode... errorCodes) {
        super(message, errorCodes);
    }

    protected DAOException(ErrorCode errorCode, Throwable throwable) {
        super(errorCode, throwable);
    }

    /**
     * Builder that creates instances of {@code DAOException}.
     *
     * @author Edgar Rico (edgar.martinez.rico@gmail.com)
     */
    public static class Builder {

        private static ResourceBundle resourceBundle;
        private static String SEPARATOR = ";";

        public static DAOException build(ErrorPropertyProvider errorCode) {
            ErrorCode error = ErrorCode.newBuilder().build(errorCode.get(), getResourceBundle());
            DAOException dAOException = new DAOException(error);
            return dAOException;
        }

        public static DAOException build(DAOErrorCode errorCode, DAOException daoException) {
            if (daoException != null) {
                ErrorCode error = ErrorCode.newBuilder().build(errorCode.get(), getResourceBundle());
                String message = String.format("%s%s%s", daoException.getMessage(), getSeparator(),
                        error.getDescription());
                DAOException dAOException = new DAOException(message, daoException.getErrorCode(),
                        error);
                return dAOException;
            } else {
                return build(errorCode);
            }
        }

        public static DAOException build(DAOErrorCode errorCode, Throwable throwable) {
            ErrorCode error = ErrorCode.newBuilder().build(errorCode.get(), getResourceBundle());
            DAOException dAOException = new DAOException(error, throwable);
            return dAOException;
        }

        public static ResourceBundle getResourceBundle() {
            return resourceBundle;
        }

        public static String getSeparator() {
            return SEPARATOR;
        }

        @Inject
        @Named("daoResourceBundle")
        public static void setResourceBundle(ResourceBundle aResourceBundle) {
            resourceBundle = aResourceBundle;
        }
    }
}
