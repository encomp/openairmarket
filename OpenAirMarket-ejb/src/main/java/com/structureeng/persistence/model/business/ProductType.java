// Copyright 2013 Structure Eng Inc.

package com.structureeng.persistence.model.business;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

/**
 * Define the different types of {@code com.structureeng.persistence.model.product.ProductType}.
 * This drives the business rules for a particular type.
 *
 * @author Edgar Rico (edgar.martinez.rico@gmail.com)
 */
@Entity
@DiscriminatorValue("PRODUCT_TYPE")
public class ProductType extends Rule {
}
