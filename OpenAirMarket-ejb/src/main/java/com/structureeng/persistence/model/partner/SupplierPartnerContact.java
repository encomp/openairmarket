// Copyright 2013 Structure Eng Inc.

package com.structureeng.persistence.model.partner;

import static com.google.common.base.Preconditions.checkNotNull;

import javax.persistence.Column;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

/**
 * Specifies the contact details for an {@code SupplierPartner}.
 *
 * @author Edgar Rico (edgar.martinez.rico@gmail.com)
 */
@Entity
@DiscriminatorValue("SUPPLIER")
public class SupplierPartnerContact extends SupplierPartner {

    @Column(name = "isSupervisor")
    private Boolean supervisor;

    public Boolean getSupervisor() {
        return supervisor;
    }

    public void setSupervisor(Boolean supervisor) {
        this.supervisor = checkNotNull(supervisor);
    }
}