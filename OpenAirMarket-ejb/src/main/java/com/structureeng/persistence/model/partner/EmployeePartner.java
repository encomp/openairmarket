// Copyright 2013 Structure Eng Inc.

package com.structureeng.persistence.model.partner;

import com.structureeng.persistence.model.AbstractSimpleCatalogTenantModel;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import static com.google.common.base.Preconditions.checkNotNull;
import static com.structureeng.persistence.model.AbstractModel.checkPositive;

/**
 * Defines a customer {@code BusinessPartner}.
 *
 * @author Edgar Rico (edgar.martinez.rico@gmail.com)
 */
@Entity
@Table(name = "employeePartner", uniqueConstraints = {
   @UniqueConstraint(name = "employeePartnerPK", columnNames = {"idTenant", "idReference"}),
   @UniqueConstraint(name = "employeePartnerUK", columnNames = {"idTenant", "idBusinessPartner"})})
public class EmployeePartner extends AbstractSimpleCatalogTenantModel<Long, String> {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "idEmployeePartner")
    private Long id;  
    
    @JoinColumn(name = "idBusinessPartner", referencedColumnName = "idBusinessPartner",
            nullable = false)
    @ManyToOne(fetch = FetchType.LAZY)
    private BusinessPartner businessPartner;
    
    @Override
    public Long getId() {
        return id;
    }

    @Override
    public void setId(Long id) {
        this.id = checkPositive(id);
    }
    
    public BusinessPartner getBusinessPartner() {
        return businessPartner;
    }

    public void setBusinessPartner(BusinessPartner businessPartner) {
        this.businessPartner = checkNotNull(businessPartner);
    }
}
