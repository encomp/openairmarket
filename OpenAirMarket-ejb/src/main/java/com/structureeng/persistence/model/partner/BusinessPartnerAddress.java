// Copyright 2013 Structure Eng Inc.

package com.structureeng.persistence.model.partner;

import static com.google.common.base.Preconditions.checkNotNull;

import com.structureeng.persistence.model.AbstractSimpleCatalogTenantModel;
import com.structureeng.persistence.model.location.State;

import javax.persistence.Column;
import javax.persistence.DiscriminatorColumn;
import javax.persistence.DiscriminatorType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

/**
 * Defines the {@code BusinessPartner}s addresses.
 *
 * @author Edgar Rico (edgar.martinez.rico@gmail.com)
 */
@Entity
@Table(name = "businessPartnerAddress", uniqueConstraints = {
        @UniqueConstraint(name = "businessPartnerAddressPK",
                columnNames = {"idTenant", "idBusinessPartner", "type", "idReference"})})
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "type", discriminatorType = DiscriminatorType.STRING, length = 50)
public abstract class BusinessPartnerAddress extends 
        AbstractSimpleCatalogTenantModel<Long, String> {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "idBusinessPartnerAddress")
    private Long id;

    @JoinColumn(name = "idBusinessPartner", referencedColumnName = "idBusinessPartner",
            nullable = false)
    @ManyToOne(fetch = FetchType.LAZY)
    private BusinessPartner businessPartner;

    @Column(name = "addressLine1", nullable = false)
    private String addressLine1;

    @Column(name = "addressLine2")
    private String addressLine2;
    
    @JoinColumn(name = "idState", referencedColumnName = "idState", nullable = false)
    @ManyToOne(fetch = FetchType.LAZY)
    private State state;

    @Column(name = "city")
    private String city;
    
    @Column(name = "zipCode", nullable = false)
    private String zipCode;
    
    @Column(name = "isBilling", nullable = false)
    private Boolean billing;
    
    @Column(name = "isDefault", nullable = false)
    private Boolean defaulted;

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

    public String getAddressLine1() {
        return addressLine1;
    }

    public void setAddressLine1(String addressLine1) {
        this.addressLine1 = checkNotEmpty(addressLine1);
    }

    public String getAddressLine2() {
        return addressLine2;
    }

    public void setAddressLine2(String addressLine2) {
        this.addressLine2 = checkNillable(addressLine2);
    }

    public State getState() {
        return state;
    }

    public void setState(State state) {
        this.state = checkNotNull(state);
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = checkNotEmpty(city);
    }

    public String getZipCode() {
        return zipCode;
    }

    public void setZipCode(String zipCode) {
        this.zipCode = checkNotEmpty(zipCode);
    }
    
    public Boolean getBilling() {
        return billing;
    }

    public void setBilling(Boolean billing) {
        this.billing = checkNotNull(billing);
    }

    public Boolean getDefaulted() {
        return defaulted;
    }

    public void setDefaulted(Boolean defaulted) {
        this.defaulted = checkNotNull(defaulted);
    }
}
