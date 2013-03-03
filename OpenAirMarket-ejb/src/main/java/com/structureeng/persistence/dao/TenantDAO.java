// Copyright 2013 Structure Eng Inc.

package com.structureeng.persistence.dao;

import com.structureeng.persistence.model.tenant.Tenant;

/**
 * Specifies the contract for the {@code Tenant} data access object.
 * 
 * @author Edgar Rico (edgar.martinez.rico@gmail.com)
 */
public interface TenantDAO extends CatalogDAO<Tenant, Long> {
}
