// Copyright 2013 Structure Eng Inc.

package com.structureeng.persistence.model.price;

import static com.google.common.base.Preconditions.checkNotNull;

import com.structureeng.persistence.model.AbstractSimpleCatalogTenantModel;
import com.structureeng.persistence.model.business.Organization;
import com.structureeng.persistence.model.location.Currency;

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
 * Price lists determine currency of the document as well as tax treatment. The price list is a
 * further refinement of the price list schema which you can apply to particular situations or
 * markets.
 *
 * @author Edgar Rico (edgar.martinez.rico@gmail.com)
 */
@Entity
@Table(name = "priceList", uniqueConstraints = {
        @UniqueConstraint(name = "priceListPK", columnNames = {"idTenant", "idReference"})})
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "type", discriminatorType = DiscriminatorType.STRING, length = 50)
public abstract class PriceList extends AbstractSimpleCatalogTenantModel<Long, String> {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "idPriceList")
    private Long id;

    @JoinColumn(name = "idOrganization", referencedColumnName = "idOrganization", nullable = false)
    @ManyToOne(fetch = FetchType.LAZY)
    private Organization organization;

    @Column(name = "description", length = 255)
    private String description;

    @JoinColumn(name = "idCurrency", referencedColumnName = "idCurrency", nullable = false)
    @ManyToOne(fetch = FetchType.LAZY)
    private Currency currency;

    @Column(name = "isDefault", nullable = false)
    private Boolean defaulted;

    @Column(name = "isTaxIncluded", nullable = false)
    private Boolean taxIncluded;

    @Override
    public Long getId() {
        return id;
    }

    @Override
    public void setId(Long id) {
        this.id = checkPositive(id);
    }

    public Organization getOrganization() {
        return organization;
    }

    public void setOrganization(Organization organization) {
        this.organization = checkNotNull(organization);
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = checkNillable(description);
    }

    public Currency getCurrency() {
        return currency;
    }

    public void setCurrency(Currency currency) {
        this.currency = checkNotNull(currency);
    }

    public Boolean getDefaulted() {
        return defaulted;
    }

    public void setDefaulted(Boolean defaulted) {
        this.defaulted = checkNotNull(defaulted);
    }

    public Boolean getTaxIncluded() {
        return taxIncluded;
    }

    public void setTaxIncluded(Boolean taxIncluded) {
        this.taxIncluded = checkNotNull(taxIncluded);
    }
}
