// Copyright 2013 Structure Eng Inc.

package com.structureeng.persistence.model;

import java.io.Serializable;

/**
 * Specifies the behavior of the entities that need to keep the active or inactive state.
 *
 * @author Edgar Rico (edgar.martinez.rico@gmail.com)
 * @param <T> specifies the {@link Class} of the id for the {@link javax.persistence.Entity}
 */
public interface ActiveModel<T extends Serializable> extends Model<T> {

    /**
     * Returns the current state of an entity.
     *
     * @return true is the entity is active or false for an inactive entity.
     */
    Boolean getActive();

    /**
     * Specifies the state of an entity.
     *
     * @param active if the value being pass is true the entity will be active otherwise will be
     * inactive
     */
    void setActive(Boolean active);
}
