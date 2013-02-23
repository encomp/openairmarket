// Copyright 2013 Structure Eng Inc.

package com.structureeng.persistence.history;

import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Specifies the builder class of a 
 * {@code com.structureeng.persistence.history.HistoryEntity}.
 *
 * @author Edgar Rico (edgar.martinez.rico@gmail.com)
 */
@Retention(RetentionPolicy.RUNTIME)
@Inherited
@Target(ElementType.TYPE)
public @interface Revision {
    public Class builder();
}
