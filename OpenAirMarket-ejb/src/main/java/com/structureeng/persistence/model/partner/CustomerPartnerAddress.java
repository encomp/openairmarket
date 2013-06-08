// Copyright 2013 Structure Eng Inc.

package com.structureeng.persistence.model.partner;

import static com.google.common.base.Preconditions.checkNotNull;

import javax.persistence.Column;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

/**
 * Defines the {@code CustomerPartner} addresses.
 *
 * @author Edgar Rico (edgar.martinez.rico@gmail.com)
 */
@Entity
@DiscriminatorValue("CUSTOMER")
public class CustomerPartnerAddress extends BusinessPartnerAddress {

    @Column(name = "isShipping")
    private Boolean shipping;
    
    public Boolean getShipping() {
        return shipping;
    }

    public void setShipping(Boolean shipping) {
        this.shipping = checkNotNull(shipping);
    }
}
