// Copyright 2013 Structure Eng Inc.

package com.structureeng.persistence.model.partner;

import static com.google.common.base.Preconditions.checkNotNull;

import javax.persistence.Column;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

/**
 * Specifies the contact details for an {@code EmployeePartner}.
 *
 * @author Edgar Rico (edgar.martinez.rico@gmail.com)
 */
@Entity
@DiscriminatorValue("EMPLOYEE")
public class EmployeePartnerContact extends BusinessPartnerContact {

    @Column(name = "isEmergency")
    private Boolean emergency;

    public Boolean getEmergency() {
        return emergency;
    }

    public void setEmergency(Boolean emergency) {
        this.emergency = checkNotNull(emergency);
    }
}
