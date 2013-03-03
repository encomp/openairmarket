// Copyright 2013 Structure Eng Inc.

package com.structureeng.common;

import com.google.common.base.Preconditions;

/**
 * Base exception.
 * 
 * @author Edgar Rico (edgar.martinez.rico@gmail.com)
 */
public class Excepcion extends Exception {
    
    private final ErrorCode errorCode;
    
    public Excepcion(ErrorCode errorCode) {
        super(errorCode.getDescription());
        this.errorCode = Preconditions.checkNotNull(errorCode);
    }
    
    public Excepcion(ErrorCode errorCode, String message) {
        super(message);
        this.errorCode = Preconditions.checkNotNull(errorCode);
    }
    
    public Excepcion(ErrorCode errorCode, Throwable cause) {
        super(errorCode.getDescription(), cause);
        this.errorCode = Preconditions.checkNotNull(errorCode);
    }
    
    public ErrorCode getErrorCode() {
        return errorCode;
    }
}
