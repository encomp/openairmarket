// Copyright 2013 Structure Eng Inc.

package com.structureeng.persistence.model.partner;

import static com.google.common.base.Preconditions.checkNotNull;

import com.structureeng.persistence.model.AbstractSimpleCatalogTenantModel;

import javax.persistence.CascadeType;
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
 * Defines the {@code BusinessPartner}s contacts.
 *
 * @author Edgar Rico (edgar.martinez.rico@gmail.com)
 */
@Entity
@Table(name = "businessPartnerContact" , uniqueConstraints = {
        @UniqueConstraint(name = "businessPartnerContactPK",
                columnNames = {"idTenant", "idBusinessPartner", "type", "idReference"})})
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "type", discriminatorType = DiscriminatorType.STRING, length = 50)
public abstract class BusinessPartnerContact extends 
        AbstractSimpleCatalogTenantModel<Long, String> {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "idBusinessPartnerContact")
    private Long id;

    @JoinColumn(name = "idBusinessPartner", referencedColumnName = "idBusinessPartner",
            nullable = false)
    @ManyToOne(fetch = FetchType.LAZY)
    private BusinessPartner businessPartner;
        
    @Column(name = "firstName", nullable = false)
    private String firstName;
    
    @Column(name = "middleName")
    private String middleName;
    
    @Column(name = "lastName", nullable = false)
    private String lastName;
    
    @Column(name = "email")
    private String email;
    
    @Column(name = "phone", length = 50)
    private String phone;
    
    @Column(name = "alternativePhone", length = 50)
    private String alternativePhone;
    
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

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = checkNotEmpty(firstName);
    }

    public String getMiddleName() {
        return middleName;
    }

    public void setMiddleName(String middleName) {
        this.middleName = checkNillable(middleName);
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = checkNotEmpty(lastName);
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = checkNillable(email);
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = checkNillable(phone);
    }

    public String getAlternativePhone() {
        return alternativePhone;
    }

    public void setAlternativePhone(String alternativePhone) {
        this.alternativePhone = checkNillable(alternativePhone);
    }    
}
