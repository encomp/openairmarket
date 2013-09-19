// Copyright 2013 Structure Eng Inc.

package com.structureeng.persistence.model.partner;

import static com.google.common.base.Preconditions.checkNotNull;

import com.structureeng.persistence.model.AbstractCatalogTenantModel;
import com.structureeng.persistence.model.business.Organization;
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

/**
 * Defines a business partner.
 *
 * @author Edgar Rico (edgar.martinez.rico@gmail.com)
 */
@Entity
@Table(name = "businessPartner", uniqueConstraints = {
        @UniqueConstraint(name = "businessPartnerPK",
                columnNames = {"idTenant", "idReference"}),
        @UniqueConstraint(name = "businessPartnerUK",
                columnNames = {"idTenant", "name"})})
public class BusinessPartner extends AbstractCatalogTenantModel<String, String> {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "idBusinessPartner")
    private String id;

    @JoinColumn(name = "idOrganization", referencedColumnName = "idOrganization", nullable = false)
    @ManyToOne(fetch = FetchType.LAZY)
    private Organization organization;

    @JoinColumn(name = "idBusinessPartnerCategory", referencedColumnName = "idRuleOrganization", nullable = false)
    @ManyToOne(fetch = FetchType.LAZY)
    private BusinessPartnerCategory businessPartnerCategory;

    @Column(name = "taxId")
    private String taxId;

    @Column(name = "url", length = 500)
    private String url;

    @Column(name = "referenceNumber", length = 500)
    private String referenceNumber;

    @Column(name = "isDefault", nullable = false)
    private Boolean defaulted;

    @Override
    public String getId() {
        return id;
    }

    @Override
    public void setId(String id) {
        this.id = checkNotEmpty(id);
    }

    public Organization getOrganization() {
        return organization;
    }

    public void setOrganization(Organization organization) {
        this.organization = checkNotNull(organization);
    }

    public BusinessPartnerCategory getBusinessPartnerCategory() {
        return businessPartnerCategory;
    }

    public void setBusinessPartnerCategory(BusinessPartnerCategory businessPartnerCategory) {
        this.businessPartnerCategory = checkNotNull(businessPartnerCategory);
    }

    public String getTaxId() {
        return taxId;
    }

    public void setTaxId(String taxId) {
        this.taxId = taxId;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getReferenceNumber() {
        return referenceNumber;
    }

    public void setReferenceNumber(String referenceNumber) {
        this.referenceNumber = referenceNumber;
    }

    public Boolean getDefaulted() {
        return defaulted;
    }

    public void setDefaulted(Boolean defaulted) {
        this.defaulted = defaulted;
    }
}
