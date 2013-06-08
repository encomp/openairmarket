// Copyright 2013 Structure Eng Inc.

package com.structureeng.persistence.model.price;

import static com.google.common.base.Preconditions.checkNotNull;

import com.structureeng.persistence.model.AbstractSimpleCatalogTenantModel;
import com.structureeng.persistence.model.business.Organization;

import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.UniqueConstraint;

/**
 * The price schema is the overall system of pricing for your organization and can specify
 * discounts for products, product categories and business partners. It calculates the trade
 * discount percentage.
 *
 * @author Edgar Rico (edgar.martinez.rico@gmail.com)
 */
@Entity
@Table(name = "priceSchema", uniqueConstraints = {
    @UniqueConstraint(name = "priceSchemaPK", columnNames = {"idTenant", "idReference"})})
public class PriceSchema extends AbstractSimpleCatalogTenantModel<Long, String> {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "idPriceSchema")
    private Long id;

    @JoinColumn(name = "idOrganization", referencedColumnName = "idOrganization", nullable = false)
    @ManyToOne(fetch = FetchType.LAZY)
    private Organization organization;

    @Column(name = "description", length = 255, nullable = true)
    private String description;

    @Temporal(TemporalType.DATE)
    @Column(name = "validFrom", nullable = false)
    private Date validFrom;

    @Column(name = "isQuantityBased", nullable = false)
    private Boolean quantityBased;

    @Enumerated(EnumType.STRING)
    @Column(name = "discountType", length = 50, nullable = false)
    private DiscountType discountType;

    @Column(name = "flatDiscount", precision = 15, scale = 6)
    private BigDecimal flatDiscount;

    @Column(name = "discountFormula", length = 500)
    private String discountFormula;

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

    public Date getValidFrom() {
        return validFrom;
    }

    public void setValidFrom(Date validFrom) {
        this.validFrom = checkNotNull(validFrom);
    }

    public Boolean getQuantityBased() {
        return quantityBased;
    }

    public void setQuantityBased(Boolean quantityBased) {
        this.quantityBased = checkNotNull(quantityBased);
    }

    public DiscountType getDiscountType() {
        return discountType;
    }

    public void setDiscountType(DiscountType discountType) {
        this.discountType = checkNotNull(discountType);
    }

    public BigDecimal getFlatDiscount() {
        return flatDiscount;
    }

    public void setFlatDiscount(BigDecimal flatDiscount) {
        this.flatDiscount = checkNillablePositive(flatDiscount);
    }

    public String getDiscountFormula() {
        return discountFormula;
    }

    public void setDiscountFormula(String discountFormula) {
        this.discountFormula = discountFormula;
    }
}
