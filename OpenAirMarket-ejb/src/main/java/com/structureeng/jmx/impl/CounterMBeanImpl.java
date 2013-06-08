// Copyright 2013 Structure Eng Inc.

package com.structureeng.jmx.impl;

import com.structureeng.jmx.CounterMBean;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * Implementation for the {@code CounterMBean}.
 *
 * @author Edgar Rico (edgar.martinez.rico@gmail.com)
 */
public class CounterMBeanImpl implements CounterMBean {

    private final AtomicInteger success = new AtomicInteger(0);
    private final AtomicInteger failure = new AtomicInteger(0);

    @Override
    public void incrementSuccess() {
        success.getAndIncrement();
    }

    @Override
    public int getSuccessCounter() {
        return success.get();
    }

    @Override
    public void incrementFailure() {
        failure.getAndIncrement();
    }

    @Override
    public int getFailureCounter() {
        return failure.get();
    }
}
