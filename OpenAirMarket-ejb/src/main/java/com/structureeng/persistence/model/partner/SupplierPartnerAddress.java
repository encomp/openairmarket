// Copyright 2013 Structure Eng Inc.

package com.structureeng.persistence.model.partner;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

/**
 * Defines the {@code SupplierPartner} addresses.
 *
 * @author Edgar Rico (edgar.martinez.rico@gmail.com)
 */
@Entity
@DiscriminatorValue("SUPPLIER")
public class SupplierPartnerAddress extends BusinessPartnerAddress {
}
