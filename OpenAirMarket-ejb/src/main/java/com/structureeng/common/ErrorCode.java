// Copyright 2013 Structure Eng Inc.

package com.structureeng.common;

import java.util.Objects;
import java.util.ResourceBundle;

/**
 * Stores the code, name and description for a specific error.
 *
 * @author Edgar Rico (edgar.martinez.rico@gmail.com)
 */
public class ErrorCode {

    private int code;
    private String name;
    private String description;

    private ErrorCode(Builder builder) {
        this.code = builder.code;
        this.name = builder.name;
        this.description = builder.description;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getCode());
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (!getClass().equals(obj.getClass())) {
            return false;
        }
        ErrorCode other = (ErrorCode) obj;
        return Objects.equals(getCode(), other.getCode());
    }
    
    public static ErrorCode.Builder newBuilder() {
        return new ErrorCode.Builder();
    }

    /**
     * Builder class that creates instances of {@code ErrorCode}.
     *
     * @author Edgar Rico (edgar.martinez.rico@gmail.com)
     */
    public static class Builder {

        private int code;
        private String name;
        private String description;
        
        /**
         * Creates a new instance of {@code ErrorCode}.
         * 
         * @param errorCodeProperty the properties that will be used to create the error.
         * @param resourceBundle the resource in which the properties are being stored.
         * @return a new instance.
         */
        public ErrorCode build(ErrorCodeProperty errorCodeProperty, ResourceBundle resourceBundle) {
            code = errorCodeProperty.getCode(resourceBundle);
            name = errorCodeProperty.getName(resourceBundle);
            description = errorCodeProperty.getDescription(resourceBundle);
            return new ErrorCode(this);
        }
    }
}
