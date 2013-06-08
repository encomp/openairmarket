// Copyright 2013 Structure Eng Inc.

package com.structureeng.persistence.dao;

import com.structureeng.persistence.model.product.ProductOrganization;
import com.structureeng.persistence.model.product.SalePrice;

import java.math.BigDecimal;
import java.util.List;

/**
 * Specifies the contract for the {@code SalePrice} data access object.
 *
 * @author Edgar Rico (edgar.martinez.rico@gmail.com)
 */
public interface SalePriceDAO extends ActiveDAO<SalePrice, Long> {

    /**
     * Provides a set collection of {@code SalePrice} for a given {@code Product}.
     *
     * @param product the instance that for which the list of prices will be retrieved
     * @return a list of prices or empty if there is any prices available.
     */
    List<SalePrice> find(ProductOrganization product);

    /**
     * Provides a {@code SalePrice} for a given {@code Product} and a quantity.
     *
     * @param product  the instance that for which a price will be retrieved
     * @param quantity the desired quantity for which the price will be searched
     * @return a price or null if a price is not defined.
     */
    SalePrice find(ProductOrganization product, BigDecimal quantity);


    /**
     * Provides an inactive {@code SalePrice} for a given {@code Product} and a quantity.
     *
     * @param product  the instance that for which a price will be retrieved
     * @param quantity the desired quantity for which the price will be searched
     * @return a price or null if a price is not defined.
     */
    SalePrice findInactive(ProductOrganization product, BigDecimal quantity);
}
