// Copyright 2013 Structure Eng Inc.
package com.structureeng.persistence.model.partner;

import com.structureeng.persistence.model.AbstractSimpleCatalogTenantModel;
import com.structureeng.persistence.model.business.TaxCategory;

import javax.persistence.CascadeType;
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

/**
 * Defines a supplier {@code BusinessPartner}.
 *
 * @author Edgar Rico (edgar.martinez.rico@gmail.com)
 */
@Entity
@Table(name = "supplierPartner", uniqueConstraints = {
   @UniqueConstraint(name = "supplierPartnerPK", columnNames = {"idTenant", "idReference"}),
   @UniqueConstraint(name = "supplierPartnerUK", columnNames = {"idTenant", "idBusinessPartner"})})
public class SupplierPartner extends AbstractSimpleCatalogTenantModel<Long, String> {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "idSupplierPartner")
    private Long id;
    
    @JoinColumn(name = "idBusinessPartner", referencedColumnName = "idBusinessPartner",
            nullable = false)
    @ManyToOne(fetch = FetchType.LAZY)
    private BusinessPartner businessPartner;

    @JoinColumn(name = "idRuleOrganization", referencedColumnName = "idRuleOrganization",
            nullable = false)    
    @ManyToOne(fetch = FetchType.LAZY)
    private TaxCategory taxCategory;
    
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

    public TaxCategory getTaxCategory() {
        return taxCategory;
    }

    public void setTaxCategory(TaxCategory taxCategory) {
        this.taxCategory = checkNotNull(taxCategory);
    }
}
