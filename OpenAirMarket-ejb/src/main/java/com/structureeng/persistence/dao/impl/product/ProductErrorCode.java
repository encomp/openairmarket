// Copyright 2013 Structure Eng Inc.

package com.structureeng.persistence.dao.impl.product;

import com.structureeng.common.exception.ErrorCodeProperty;
import com.structureeng.common.exception.ErrorPropertyProvider;

/**
 * Defines the error codes for the entities related with a {@code ProductDefinition}.
 *
 * @author Edgar Rico (edgar.martinez.rico@gmail.com)
 */
public enum ProductErrorCode implements ErrorPropertyProvider {    
        
    /**
     * Property that specifies the error in case the foreign key has been violated for a 
     * {@code Company} entity.
     */
    COMPANY_FK("dao.company.foreignKey.productDefinition"),
    
    /**
     * Property that specifies the error in case the foreign key has been violated for a 
     * {@code Division} entity.
     */
    DIVISION_FK("dao.division.foreignKey.productDefinition"),
    
    /**
     * Property that specifies the error in case the foreign key has been violated for a 
     * {@code MeasureUnit} entity.
     */
    MEASURE_UNIT_FK("dao.measureUnit.foreignKey.retailProduct"),
    
    /**
     * Property that specifies the error in case the foreign key has been violated for a 
     * {@code ProductDefinition} entity.
     */
    PRODUCT_DEFFINITION_FK("dao.productDefinition.foreignKey.product"),
    
    /**
     * Property that specifies the error in case the unique key for the key attribute has been 
     * violated.
     */
    PRODUCT_DEFFINITION_KEY_UK("dao.productDefinition.uniqueKey.key");

    private final ErrorCodeProperty errorCodeProperty;

    private ProductErrorCode(String property) {
        this.errorCodeProperty = new ErrorCodeProperty(property);
    }
    
    @Override
    public ErrorCodeProperty get() {
        return errorCodeProperty;
    }
}
