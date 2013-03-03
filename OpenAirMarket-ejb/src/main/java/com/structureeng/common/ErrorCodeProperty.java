// Copyright 2013 Structure Eng Inc.

package com.structureeng.common;

import com.google.common.base.Preconditions;
import com.google.common.base.Strings;

import java.io.Serializable;
import java.util.ResourceBundle;

/**
 * Stores and retrieves the properties for a specific error code.
 * 
 * @author Edgar Rico (edgar.martinez.rico@gmail.com)
 */
public class ErrorCodeProperty implements Serializable {

    private static final String CODE = ".code";
    private static final String NAME = ".name";
    private static final String DESCRIPTION = ".description";
    private final String property;

    public ErrorCodeProperty(String property) {
        Preconditions.checkState(!Strings.isNullOrEmpty(property));
        this.property = property;
    }

    public Integer getCode(ResourceBundle bundle) {
        return Integer.valueOf(getProperty(bundle, property.concat(CODE)));
    }

    public String getName(ResourceBundle bundle) {
        return getProperty(bundle, property.concat(NAME));
    }

    public String getDescription(ResourceBundle bundle) {
        return getProperty(bundle, property.concat(DESCRIPTION));
    }

    private String getProperty(ResourceBundle bundle, String property){
        bundle = Preconditions.checkNotNull(bundle);
        return bundle.getString(property);
    }
        
}
