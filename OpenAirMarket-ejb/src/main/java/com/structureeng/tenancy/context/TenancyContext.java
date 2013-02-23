// Copyright 2013 Structure Eng Inc.

package com.structureeng.tenancy.context;

import com.structureeng.persistence.model.tenant.Tenant;

import com.google.common.base.Preconditions;

import java.util.Objects;

/**
 * A context in which tenancy can be defined.
 *
 * @author Edgar Rico (edgar.martinez.rico@gmail.com)
 */
public class TenancyContext {
    
    private final Tenant tenant;
    
    public TenancyContext (Tenant tenant) {
        this.tenant = Preconditions.checkNotNull(tenant);
    }

    public Tenant getTenant() {
        return tenant;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getTenant());
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final TenancyContext other = (TenancyContext) obj;
        return Objects.equals(getTenant(), other.getTenant());
    }

    @Override
    public String toString() {
        return "TenancyContext{" + "tenant=" + tenant + '}';
    }
}
