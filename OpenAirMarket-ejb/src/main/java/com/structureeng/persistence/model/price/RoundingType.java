// Copyright 2013 Structure Eng Inc.

package com.structureeng.persistence.model.price;

/**
 * Defines the starting point for the price before any subsequent discount is applied.
 *
 * @author Edgar Rico (edgar.martinez.rico@gmail.com)
 */
public enum RoundingType {

    /**
     * (Whole Number .00): Number w/o decimals
     */
    ZERO,

    /**
     * Two Decimals
     */
    TWO,

    /**
     * Three decimals
     */
    THREE,

    /**
     * Four decimals
     */
    FOUR,

    /**
     * Nickel .05, .10, .15, ...
     */
    Nickel,

    /**
     * Currency Precision.
     */
    CURRENCY,

    /**
     * Dime .10, .20, .30, ...
     */
    DIME,

    /**
     * No Rounding
     */
    NONE,

    /**
     * Quarter .25 .50 .75
     */
    QUARTER,

    /**
     * Ten 10.00, 20.00, ..
     */
    TEN
}
