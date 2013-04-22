// Copyright 2013 Structure Eng Inc.

package com.structureeng.persistence.dao;

import com.structureeng.persistence.model.product.Product;
import com.structureeng.persistence.model.product.PurchasePrice;

import java.math.BigDecimal;
import java.util.List;

/**
 * Specifies the contract for the {@code PurchasePrice} data access object.
 *
 * @author Edgar Rico (edgar.martinez.rico@gmail.com)
 */
public interface PurchasePriceDAO extends ActiveDAO<PurchasePrice, Long> {
    /**
     * Provides a set collection of {@code PurchasePrice} for a given {@code Product}.
     *
     * @param product the instance that for which the list of prices will be retrieved
     * @return a list of prices or empty if there is any prices available.
     */
    List<PurchasePrice> find(Product product);

    /**
     * Provides a {@code PurchasePrice} for a given {@code Product} and a quantity.
     *
     * @param product  the instance that for which a price will be retrieved
     * @param quantity the desired quantity for which the price will be searched
     * @return a price or null if a price is not defined.
     */
    PurchasePrice find(Product product, BigDecimal quantity);


    /**
     * Provides an inactive {@code PurchasePrice} for a given {@code Product} and a quantity.
     *
     * @param product  the instance that for which a price will be retrieved
     * @param quantity the desired quantity for which the price will be searched
     * @return a price or null if a price is not defined.
     */
    PurchasePrice findInactive(Product product, BigDecimal quantity);
}
