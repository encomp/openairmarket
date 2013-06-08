// Copyright 2013 Structure Eng Inc.

package com.structureeng.jmx;

/**
 * Keeps track of the success and failure invocation for a particular operation.
 *
 * @author Edgar Rico (edgar.martinez.rico@gmail.com)
 */
public interface CounterMBean {

    /**
     * Increments by one the success counter.
     */
    void incrementSuccess();

    /**
     * Provides the count of the number of times the success counter has been invoked.
     *
     * @return the number of success operations.
     */
    int getSuccessCounter();

    /**
     * Increments by one the failure counter.
     */
    void incrementFailure();

    /**
     * Provides the count of the number of times the failure counter has been invoked.
     *
     * @return the number of failure operations.
     */
    int getFailureCounter();
}
