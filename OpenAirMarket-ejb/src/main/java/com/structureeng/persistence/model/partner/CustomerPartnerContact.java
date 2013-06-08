// Copyright 2013 Structure Eng Inc.

package com.structureeng.persistence.model.partner;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

/**
 * Specifies the contact details for an {@code CustomerPartner}.
 *
 * @author Edgar Rico (edgar.martinez.rico@gmail.com)
 */
@Entity
@DiscriminatorValue("CUSTOMER")
public class CustomerPartnerContact extends BusinessPartnerContact {
}
