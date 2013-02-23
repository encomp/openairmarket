// Copyright 2013 Structure Eng Inc.

package com.structureeng.tenancy.provider;

import com.structureeng.persistence.model.tenant.Tenant;

/**
 * Basic implementation of tenant provider which provides a {@link Tenant} with the given identity.
 * This is useful when only the identity of the tenant is needed to be stored.
 *
 * @author Edgar Rico (edgar.martinez.rico@gmail.com)
 */
public class DefaultTenantProvider implements TenantProvider {

    @Override
    public Tenant findTenant(Long identity) {
        Tenant tenant = new Tenant();
        tenant.setId(identity);
        return tenant;
    }

    @Override
    public Tenant findTenant(String referenceId) {
        return findTenant(Long.valueOf(referenceId));
    }
}
