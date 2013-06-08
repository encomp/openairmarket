// Copyright 2013 Structure Eng Inc.

package com.structureeng.persistence.model.price;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

/**
 * Purchase price list is a further refinement of the {@code PriceList}.
 *
 * @author Edgar Rico (edgar.martinez.rico@gmail.com)
 */
@Entity
@DiscriminatorValue("PURCHASE")
public class PurchasePriceList extends PriceList {
}
