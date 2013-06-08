// Copyright 2013 Structure Eng Inc.

package com.structureeng.persistence.model.price;

/**
 * Defines the different types of conversion rate types.
 *
 * @author Edgar Rico (edgar.martinez.rico@gmail.com)
 */
public enum ConversionRateType {

    /**
     * Average Rates.
     */
    AVERAGE,

    /**
     * Company Rate.
     */
    COMPANY,

    /**
     * Fixed Currency Rate.
     */
    FIXED,

    /**
     * Manual Currency Rate.
     */
    MANUAL,

    /**
     * No Conversion Rate.
     */
    NONE,

    /**
     * Period Conversion Type.
     */
    PERIOD_END,

    /**
     * Spot Conversation Rate Type.
     */
    SPOT,

    /**
     * User Rate Type.
     */
    USER
}
