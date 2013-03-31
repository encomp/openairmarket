// Copyright 2013 Structure Eng Inc.

package com.structureeng.persistence.dao;

import com.structureeng.persistence.model.product.ProductDefinition;

import java.math.BigInteger;

/**
 * Specifies the contract for the {@code ProductDefinition} data access object.
 * 
 * @author Edgar Rico (edgar.martinez.rico@gmail.com)
 */
public interface ProductDefinitionDAO extends CatalogDAO<ProductDefinition, Long, BigInteger> {
    
}
