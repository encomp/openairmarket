// Copyright 2013 Structure Eng Inc.

package com.structureeng.persistence.model.price;

import static com.google.common.base.Preconditions.checkNotNull;

import com.structureeng.persistence.model.AbstractSimpleCatalogTenantModel;
import com.structureeng.persistence.model.partner.BusinessPartner;
import com.structureeng.persistence.model.product.ProductCategory;
import com.structureeng.persistence.model.product.ProductOrganization;

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
 * Price lists are created based on Product Purchase and Category Discounts. The parameters listed
 * here allow to copy and calculate price lists.
 *
 * @author Edgar Rico (edgar.martinez.rico@gmail.com)
 */
@Entity
@Table(name = "priceSchemaDetail", uniqueConstraints = {
        @UniqueConstraint(name = "priceSchemaDetailPK",
                columnNames = {"idTenant", "idPriceSchema", "idReference"})})
public class PriceSchemaDetail extends AbstractSimpleCatalogTenantModel<Long, Integer> {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "idPriceSchemaDetail")
    private Long id;

    @JoinColumn(name = "idPriceSchema", referencedColumnName = "idPriceSchema", nullable = false)
    @ManyToOne(fetch = FetchType.LAZY)
    private PriceSchema priceSchema;

    @JoinColumn(name = "idBusinessPartner", referencedColumnName = "idBusinessPartner")
    @ManyToOne(fetch = FetchType.LAZY)
    private BusinessPartner businessPartner;

    @JoinColumn(name = "idProductCategory", referencedColumnName = "idProductCategory")
    @ManyToOne(fetch = FetchType.LAZY)
    private ProductCategory productCategory;

    @JoinColumn(name = "idProductOrganization", referencedColumnName = "idProductOrganization")
    @ManyToOne(fetch = FetchType.LAZY)
    private ProductOrganization productOrganization;

    @Enumerated(EnumType.STRING)
    @Column(name = "conversionRateType", length = 50)
    private ConversionRateType conversionRateType;

    @Temporal(TemporalType.DATE)
    @Column(name = "conversion")
    private Date conversion;

    /**
     * The List Price Base indicates the price to use as the basis for the calculation of a new
     * price list.
     */
    @Enumerated(EnumType.STRING)
    @Column(name = "listPriceType", length = 50, nullable = false)
    private PriceType listPriceType;

    /**
     * The List Price Rounding indicates how the final list price will be rounded.
     */
    @Enumerated(EnumType.STRING)
    @Column(name = "listRoundingType", length = 50, nullable = false)
    private RoundingType listRoundingType;

    /**
     * List Price Surcharge Amount.
     */
    @Column(name = "listAddAmount", precision = 15, scale = 6)
    private BigDecimal listAddAmount;

    /**
     * Discount from list price as a percentage.
     */
    @Column(name = "listDiscountAmount", precision = 15, scale = 6)
    private BigDecimal listDiscountAmount;

    /**
     * Minimum margin for a product.
     */
    @Column(name = "listMinimumAmount", precision = 15, scale = 6)
    private BigDecimal listMinimumAmount;

    /**
     * Maximum margin for a product.
     */
    @Column(name = "listMaximumAmount", precision = 15, scale = 6)
    private BigDecimal listMaximumAmount;

    /**
     * Fixes list price (not calculated).
     */
    @Column(name = "listFixedAmount", precision = 15, scale = 6)
    private BigDecimal listFixedAmount;

    /**
     * The List Price Base indicates the price to use as the basis for the calculation of a new
     * price unit.
     */
    @Enumerated(EnumType.STRING)
    @Column(name = "unitPriceType", length = 50, nullable = false)
    private PriceType unitPriceType;

    /**
     * The unit Price Rounding indicates how the final unit price will be rounded.
     */
    @Enumerated(EnumType.STRING)
    @Column(name = "unitRoundingType", length = 50, nullable = false)
    private RoundingType unitRoundingType;

    /**
     * Unit Price Surcharge Amount.
     */
    @Column(name = "unitAddAmount", precision = 15, scale = 6)
    private BigDecimal unitAddAmount;

    /**
     * Discount from unit price as a percentage.
     */
    @Column(name = "unitDiscountAmount", precision = 15, scale = 6)
    private BigDecimal unitDiscountAmount;

    /**
     * Minimum margin for a product.
     */
    @Column(name = "unitMinimumAmount", precision = 15, scale = 6)
    private BigDecimal unitMinimumAmount;

    /**
     * Maximum margin for a product.
     */
    @Column(name = "unitMaximumAmount", precision = 15, scale = 6)
    private BigDecimal unitMaximumAmount;

    /**
     * Fixes Unit Price (not calculated).
     */
    @Column(name = "unitFixedAmount", precision = 15, scale = 6)
    private BigDecimal unitFixedAmount;

    @Override
    public Long getId() {
        return id;
    }

    @Override
    public void setId(Long id) {
        this.id = checkPositive(id);
    }

    public PriceSchema getPriceSchema() {
        return priceSchema;
    }

    public void setPriceSchema(PriceSchema priceSchema) {
        this.priceSchema = priceSchema;
    }

    public BusinessPartner getBusinessPartner() {
        return businessPartner;
    }

    public void setBusinessPartner(BusinessPartner businessPartner) {
        this.businessPartner = businessPartner;
    }

    public ProductCategory getProductCategory() {
        return productCategory;
    }

    public void setProductCategory(ProductCategory productCategory) {
        this.productCategory = productCategory;
    }

    public ProductOrganization getProductOrganization() {
        return productOrganization;
    }

    public void setProductOrganization(ProductOrganization productOrganization) {
        this.productOrganization = productOrganization;
    }

    public ConversionRateType getConversionRateType() {
        return conversionRateType;
    }

    public void setConversionRateType(ConversionRateType conversionRateType) {
        this.conversionRateType = conversionRateType;
    }

    public Date getConversion() {
        return conversion;
    }

    public void setConversion(Date conversion) {
        this.conversion = conversion;
    }

    public PriceType getListPriceType() {
        return listPriceType;
    }

    public void setListPriceType(PriceType listPriceType) {
        this.listPriceType = checkNotNull(listPriceType);
    }

    public RoundingType getListRoundingType() {
        return listRoundingType;
    }

    public void setListRoundingType(RoundingType listRoundingType) {
        this.listRoundingType = checkNotNull(listRoundingType);
    }

    public BigDecimal getListAddAmount() {
        return listAddAmount;
    }

    public void setListAddAmount(BigDecimal listAddAmount) {
        this.listAddAmount = checkNillablePositive(listAddAmount);
    }

    public BigDecimal getListDiscountAmount() {
        return listDiscountAmount;
    }

    public void setListDiscountAmount(BigDecimal listDiscountAmount) {
        this.listDiscountAmount = checkNillablePositive(listDiscountAmount);
    }

    public BigDecimal getListMinimumAmount() {
        return listMinimumAmount;
    }

    public void setListMinimumAmount(BigDecimal listMinimumAmount) {
        this.listMinimumAmount = checkNillablePositive(listMinimumAmount);
    }

    public BigDecimal getListMaximumAmount() {
        return listMaximumAmount;
    }

    public void setListMaximumAmount(BigDecimal listMaximumAmount) {
        this.listMaximumAmount = checkNillablePositive(listMaximumAmount);
    }

    public BigDecimal getListFixedAmount() {
        return listFixedAmount;
    }

    public void setListFixedAmount(BigDecimal listFixedAmount) {
        this.listFixedAmount = checkNillablePositive(listFixedAmount);
    }

    public PriceType getUnitPriceType() {
        return unitPriceType;
    }

    public void setUnitPriceType(PriceType unitPriceType) {
        this.unitPriceType = checkNotNull(unitPriceType);
    }

    public RoundingType getUnitRoundingType() {
        return unitRoundingType;
    }

    public void setUnitRoundingType(RoundingType unitRoundingType) {
        this.unitRoundingType = checkNotNull(unitRoundingType);
    }

    public BigDecimal getUnitAddAmount() {
        return unitAddAmount;
    }

    public void setUnitAddAmount(BigDecimal unitAddAmount) {
        this.unitAddAmount = checkNillablePositive(unitAddAmount);
    }

    public BigDecimal getUnitDiscountAmount() {
        return unitDiscountAmount;
    }

    public void setUnitDiscountAmount(BigDecimal unitDiscountAmount) {
        this.unitDiscountAmount = checkNillablePositive(unitDiscountAmount);
    }

    public BigDecimal getUnitMinimumAmount() {
        return unitMinimumAmount;
    }

    public void setUnitMinimumAmount(BigDecimal unitMinimumAmount) {
        this.unitMinimumAmount = checkNillablePositive(unitMinimumAmount);
    }

    public BigDecimal getUnitMaximumAmount() {
        return unitMaximumAmount;
    }

    public void setUnitMaximumAmount(BigDecimal unitMaximumAmount) {
        this.unitMaximumAmount = checkNillablePositive(unitMaximumAmount);
    }

    public BigDecimal getUnitFixedAmount() {
        return unitFixedAmount;
    }

    public void setUnitFixedAmount(BigDecimal unitFixedAmount) {
        this.unitFixedAmount = checkNillablePositive(unitFixedAmount);
    }
}
