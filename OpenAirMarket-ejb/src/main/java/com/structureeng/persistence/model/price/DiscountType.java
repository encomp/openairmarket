// Copyright 2013 Structure Eng Inc.

package com.structureeng.persistence.model.price;

/**
 * Defines the different types of discount types.
 *
 * @author Edgar Rico (edgar.martinez.rico@gmail.com)
 */
public enum DiscountType {

    /**
     * Discount based on a break.
     */
    BREAKS,

    /**
     * Discount based on a percentage.
     */
    FLAT_PERCENT,

    /**
     * Discount based on a price list.
     */
    PRICE_LIST,

    /**
     * Discount based on a particular formula.
     */
    FORMULA
}
