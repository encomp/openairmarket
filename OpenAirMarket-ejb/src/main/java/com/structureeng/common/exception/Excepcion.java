// Copyright 2013 Structure Eng Inc.

package com.structureeng.common.exception;

import com.google.common.base.Preconditions;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Base exception that stores the {@code ErrorCode}.
 * 
 * @author Edgar Rico (edgar.martinez.rico@gmail.com)
 */
public class Excepcion extends Exception {
    
    private final List<ErrorCode> errorCodes;
    
    protected Excepcion(ErrorCode errorCode) {
        super(errorCode.getDescription());
        this.errorCodes = new ArrayList<ErrorCode>();
        this.errorCodes.add(Preconditions.checkNotNull(errorCode));
    }
    
    protected Excepcion(String message, ErrorCode... errorCodes) {
        super(message); 
        Preconditions.checkNotNull(errorCodes);
        Preconditions.checkState(errorCodes.length > 0);        
        this.errorCodes = Arrays.asList(errorCodes);
    }
    
    protected Excepcion(ErrorCode errorCode, Throwable cause) {
        super(errorCode.getDescription(), cause);
        this.errorCodes = new ArrayList<ErrorCode>();
        this.errorCodes.add(Preconditions.checkNotNull(errorCode));
    }
    
    public ErrorCode getErrorCode() {
        return errorCodes.get(0);
    }
    
    public List<ErrorCode> getErrorCodes() {
        return errorCodes;
    }
}
