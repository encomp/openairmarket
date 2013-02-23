// Copyright 2013 Structure Eng Inc.

package com.structureeng.tenancy.provider;

import com.structureeng.persistence.model.tenant.Tenant;

/**
 * A means of providing a tenant for any given tenant identity.
 *
 * @author Edgar Rico (edgar.martinez.rico@gmail.com)
 */
public interface TenantProvider {

    /**
     * Attempt to find a tenant based on a given tenant reference id.
     *
     * @param referenceId the identify of a tenant, or null if there is none
     * @return a tenant for the corresponding identity, or <code>null</code> if the given identity
     *         has no corresponding tenant.
     */
    Tenant findTenant(String referenceId);

    /**
     * Attempt to find a tenant based on a given tenant identity.
     *
     * @param identity the identify of a tenant, or null if there is none
     * @return a tenant for the corresponding identity, or <code>null</code> if the given identity
     *         has no corresponding tenant.
     */
    Tenant findTenant(Long identity);
}
