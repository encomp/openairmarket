// Copyright 2013 Structure Eng Inc.

package com.structureeng.persistence.model.price;

/**
 * Defines the starting point for the price before any subsequent discount is applied.
 *
 * @author Edgar Rico (edgar.martinez.rico@gmail.com)
 */
public enum PriceType {

    /**
     * Fixed Price.
     */
    FIXED,

    /**
     * List Price.
     */
    LIST,

    /**
     * Standard Price.
     */
    STANDARD,

    /**
     * Limit Purchase Order Price.
     */
    LIMIT,
}
