// Copyright 2013 Structure Eng Inc.

package com.structureeng.persistence.model;

import java.io.Serializable;

/**
 * Specifies the behavior of all {@code Tenant} the entities.
 *
 * @author Edgar Rico (edgar.martinez.rico@gmail.com)
 * @param <T> specifies the {@link Class} of the id for the {@link javax.persistence.Entity}
 */
public interface TenantModel<T extends Serializable> extends Model<T> {
}
