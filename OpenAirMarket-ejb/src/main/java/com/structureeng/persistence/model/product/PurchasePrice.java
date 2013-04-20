// Copyright 2013 Structure Eng Inc.

package com.structureeng.persistence.model.product;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

/**
 * Specifies the purchase price of a {@code Product}.
 *
 * @author Edgar Rico (edgar.martinez.rico@gmail.com)
 */
@Entity
@DiscriminatorValue("PURCHASE_PRICE")
public class PurchasePrice extends ProductPrice {
}
