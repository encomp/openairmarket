// Copyright 2013 Structure Eng Inc.

package com.structureeng.persistence.dao;

import com.structureeng.persistence.model.product.Product;
import com.structureeng.persistence.model.stock.Stock;
import com.structureeng.persistence.model.stock.Warehouse;

/**
 * Specifies the contract for the {@code Stock} data access object.
 *
 * @author Edgar Rico (edgar.martinez.rico@gmail.com)
 */
public interface StockDAO extends ActiveDAO<Stock, Long> {

    /**
     * Search for a specific Stock based on the {@code Product} and {@code Warehouse}.
     *
     * @param product   the {@code Product} instance that is being requested.
     * @param warehouse the {@code Warehouse} instance that is being requested.
     * @return the instance of {@code Stock} if exist on the database.
     */
    Stock find(Product product, Warehouse warehouse);
    
    /**
     * Search for a specific INACTIVE Stock based on the {@code Product} and {@code Warehouse}.
     *
     * @param product   the {@code Product} instance that is being requested.
     * @param warehouse the {@code Warehouse} instance that is being requested.
     * @return the instance of {@code Stock} if exist on the database.
     */
    Stock findInactive(Product product, Warehouse warehouse);
}
