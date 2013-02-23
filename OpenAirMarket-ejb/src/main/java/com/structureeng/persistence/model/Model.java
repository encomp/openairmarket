// Copyright 2013 Structure Eng Inc.

package com.structureeng.persistence.model;

import java.io.Serializable;

/**
 * Specifies a persistent instance behavior.
 * 
 * @author Edgar Rico (edgar.martinez.rico@gmail.com)
 */
public interface Model extends Serializable {
    
    /**
     * Provides the value of the optimistic locking column.
     * 
     * @return - the value
     */
    Long getVersion();
}
