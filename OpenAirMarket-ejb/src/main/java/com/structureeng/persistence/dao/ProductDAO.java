// Copyright 2013 Structure Eng Inc.

package com.structureeng.persistence.dao;

import com.structureeng.persistence.model.product.Product;

import java.math.BigInteger;

/**
 * Specifies the contract for the {@code Product} data access object.
 * 
 * @author Edgar Rico (edgar.martinez.rico@gmail.com)
 */
public interface ProductDAO extends CatalogDAO<Product, Long, BigInteger> {
}
