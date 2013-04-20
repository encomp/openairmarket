// Copyright 2013 Structure Eng Inc.

package com.structureeng.tenancy.integration;

import com.structureeng.persistence.model.tenant.Tenant;
import com.structureeng.tenancy.context.TenancyContext;
import com.structureeng.tenancy.context.TenancyContextHolder;

import java.util.concurrent.Callable;

import javax.inject.Inject;

/**
 * A callable that sets the tenancy context for the duration of the call.
 *
 * @author Edgar Rico (edgar.martinez.rico@gmail.com)
 * @param <T> the result type of method <tt>call</tt>.
 */
public abstract class TenancyCallable<T> implements Callable<T>, TenantIdentificationStrategy {

    private final Tenant tenant;

    /**
     * Create the callable with the given tenant as the context.
     *
     * @param tenant the tenant that will be used.
     */
    @Inject
    public TenancyCallable(Tenant tenant) {
        this.tenant = tenant;
    }

    /**
     * Create the callable with the current context tenant as the context of the callable.
     */
    public TenancyCallable() {
        this(TenancyContextHolder.getCurrentTenancyContext().getTenant());
    }

    @Override
    public final T call() throws Exception {
        final TenancyContext previousTenancyContext =
                TenancyContextHolder.getCurrentTenancyContext();
        TenancyContextHolder.registerTenancyContext(new TenancyContext(tenant));
        try {
            return callAsTenant();
        } finally {
            TenancyContextHolder.registerTenancyContext(previousTenancyContext);
        }
    }

    protected abstract T callAsTenant() throws Exception;
}
