// Copyright 2013 Structure Eng Inc.

package com.structureeng.tenancy.context;

import com.google.common.base.Preconditions;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * A <code>ThreadLocal</code>-based implementation of {@link TenancyContextHolderStrategy}.
 *
 * @author Edgar Rico (edgar.martinez.rico@gmail.com)
 */
public class ThreadLocalTenancyContextHolder implements TenancyContextHolderStrategy {
    private static final Logger LOGGER = 
            LoggerFactory.getLogger(ThreadLocalTenancyContextHolder.class);
    private static final ThreadLocal<TenancyContext> contextHolder 
            = new ThreadLocal<TenancyContext>();

    @Override
    public void clearCurrentTenancyContext() {
        contextHolder.set(null);
    }

    @Override
    public TenancyContext getCurrentTenancyContext() {        
        return contextHolder.get();
    }

    @Override
    public void registerTenancyContext(TenancyContext context) {
        Thread thread = Thread.currentThread();
        LOGGER.debug(String.format("Registering [%s] for ThreadId [%d]", context, thread.getId()));
        contextHolder.set(Preconditions.checkNotNull(context));
    }

    @Override
    public String toString() {
        return "Thread Local Tenancy Stragtegy";
    }
}
