// Copyright 2013 Structure Eng Inc.

package com.structureeng.persistence.model.product;

import com.structureeng.persistence.model.AbstractTenantModel;

import com.google.common.base.Preconditions;

import java.math.BigDecimal;
import java.math.BigInteger;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

/**
 * Specifies the way a {@code ProductType} will be sell.
 *
 * @author Edgar Rico (edgar.martinez.rico@gmail.com)
 */
@Entity
@Table(name = "retailProduct", uniqueConstraints = {
        @UniqueConstraint(name = "retailProductTenantPK",
                columnNames = {"idTenant", "idReference"}),
        @UniqueConstraint(name = "retailProductPK", columnNames = {"idTenant", "name"})})
public class RetailProduct extends AbstractTenantModel<Long> {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "idRetailProduct")
    private Long id;

    @Column(name = "idReference", precision = 20, scale = 0, nullable = false)
    private BigInteger idReference;

    @Column(name = "name", nullable = false, unique = true, length = 255)
    private String name;

    @Column(name = "quantity", nullable = false, precision = 13, scale = 4)
    private BigDecimal quantity;

    @Column(name = "price", nullable = false, precision = 13, scale = 4)
    private BigDecimal price;

    @Column(name = "profit", nullable = false, precision = 13, scale = 4)
    private BigDecimal profit;

    @JoinColumn(name = "idProduct", referencedColumnName = "idProduct", nullable = false)
    @ManyToOne(cascade = CascadeType.REFRESH)
    private Product product;

    @JoinColumn(name = "idMeasureUnit", referencedColumnName = "idMeasureUnit", nullable = false)
    @ManyToOne(cascade = CascadeType.REFRESH)
    private MeasureUnit measureUnit;

    @JoinColumn(name = "idParentRetailProduct", referencedColumnName = "idRetailProduct",
            nullable = true)
    @ManyToOne(cascade = CascadeType.REFRESH)
    private RetailProduct retailParentProduct;

    @Override
    public Long getId() {
        return id;
    }

    @Override
    public void setId(Long id) {
        this.id = Preconditions.checkNotNull(id);
    }

    public BigInteger getIdReference() {
        return idReference;
    }

    public void setIdReference(BigInteger idReference) {
        this.idReference = Preconditions.checkNotNull(idReference);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = Preconditions.checkNotNull(name);
    }

    public BigDecimal getQuantity() {
        return quantity;
    }

    public void setQuantity(BigDecimal quantity) {
        this.quantity = Preconditions.checkNotNull(quantity);
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = Preconditions.checkNotNull(price);
    }

    public BigDecimal getProfit() {
        return profit;
    }

    public void setProfit(BigDecimal profit) {
        this.profit = Preconditions.checkNotNull(profit);
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = Preconditions.checkNotNull(product);
    }

    public MeasureUnit getMeasureUnit() {
        return measureUnit;
    }

    public void setMeasureUnit(MeasureUnit measureUnit) {
        this.measureUnit = Preconditions.checkNotNull(measureUnit);
    }

    public RetailProduct getRetailParentProduct() {
        return retailParentProduct;
    }

    public void setRetailParentProduct(RetailProduct retailParentProdcut) {
        this.retailParentProduct = retailParentProdcut;
    }
}
