// Copyright 2013 Structure Eng Inc.

package com.structureeng.common.exception;

import javax.inject.Provider;

/**
 * Provides an {@code ErrorCodeProperty} to build a specific error code.
 * 
 * @author Edgar Rico (edgar.martinez.rico@gmail.com)
 */
public interface ErrorPropertyProvider extends Provider<ErrorCodeProperty>{
    
    /**
     * Provides the property for a specific error code.
     * 
     * @return the property that will be used to build an {@code ErrorCode}.
     */
    @Override
    ErrorCodeProperty get();
}
