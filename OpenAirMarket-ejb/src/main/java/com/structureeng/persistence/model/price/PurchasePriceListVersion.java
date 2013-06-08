// Copyright 2013 Structure Eng Inc.

package com.structureeng.persistence.model.price;

import static com.google.common.base.Preconditions.checkNotNull;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

/**
 * Specifies a purchase price list that can be assigned to a
 * {@code com.structureeng.persistence.model.product.Product}.
 *
 * @author Edgar Rico (edgar.martinez.rico@gmail.com)
 */
@Entity
@DiscriminatorValue("PURCHASE")
public class PurchasePriceListVersion extends PriceListVersion {

    @JoinColumn(name = "idPriceList", referencedColumnName = "idPriceList", nullable = false)
    @ManyToOne(fetch = FetchType.LAZY)
    private PurchasePriceList purchasePriceList;

    public PurchasePriceList getPurchasePriceList() {
        return purchasePriceList;
    }

    public void setPurchasePriceList(PurchasePriceList purchasePriceList) {
        this.purchasePriceList = checkNotNull(purchasePriceList);
    }
}
