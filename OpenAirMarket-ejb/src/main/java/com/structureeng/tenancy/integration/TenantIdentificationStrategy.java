// Copyright 2013 Structure Eng Inc.

package com.structureeng.tenancy.integration;

/**
 * Responsible for setting and removing the {@link com.structureeng.tenancy.context.TenancyContext
 * tenancy context} for the scope of every request. This filter should be installed before any
 * components that need access to the {@link com.structureeng.tenancy.context.TenancyContext
 * tenancy context}.
 *
 * @author Edgar Rico (edgar.martinez.rico@gmail.com)
 */
public interface TenantIdentificationStrategy {
}
