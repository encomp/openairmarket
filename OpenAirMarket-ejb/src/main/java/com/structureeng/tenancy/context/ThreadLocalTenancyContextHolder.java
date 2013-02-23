// Copyright 2013 Structure Eng Inc.

package com.structureeng.tenancy.context;

import com.google.common.base.Preconditions;
import com.google.common.cache.Cache;

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
    private static Cache<Long, TenancyContext> contextHolder;

    @Override
    public void clearCurrentTenancyContext() {
        Thread thread = Thread.currentThread();
        contextHolder.asMap().remove(thread.getId());
    }

    @Override
    public TenancyContext getCurrentTenancyContext() {
        Thread thread = Thread.currentThread();
        return contextHolder.asMap().get(thread.getId());
    }

    @Override
    public void registerTenancyContext(TenancyContext context) {
        Thread thread = Thread.currentThread();
        LOGGER.debug(String.format("Registering [%s] for ThreadId [%d]", context, thread.getId()));
        contextHolder.asMap().put(thread.getId(), Preconditions.checkNotNull(context));
    }

    public static Cache<Long, TenancyContext> getContextHolder() {
        return contextHolder;
    }

    public static void setContextHolder(Cache<Long, TenancyContext> contextHolder) {
        ThreadLocalTenancyContextHolder.contextHolder = Preconditions.checkNotNull(contextHolder);
    } 

    @Override
    public String toString() {
        return "Thread Local Tenancy Stragtegy";
    }
}
