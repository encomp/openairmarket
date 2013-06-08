// Copyright 2013 Structure Eng Inc.

package com.structureeng.persistence.model.product;

import static com.google.common.base.Preconditions.checkNotNull;

import com.structureeng.persistence.model.AbstractTenantModel;
import com.structureeng.persistence.model.price.PriceListVersion;

import java.math.BigDecimal;

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
 * Specifies the price of a {@code Product} the price of how it should be bought and sold.
 *
 * @author Edgar Rico (edgar.martinez.rico@gmail.com)
 */
@Entity
@Table(name = "productPrice", uniqueConstraints = {
        @UniqueConstraint(name = "retailProductTenantPK",
                columnNames = {"idTenant", "idReference"}),
        @UniqueConstraint(name = "retailProductPK",
                columnNames = {"idTenant", "idProduct", "idPriceListVersion", "priceType"})})
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "priceType", discriminatorType = DiscriminatorType.STRING, length = 50)
public abstract class ProductPrice extends AbstractTenantModel<Long> {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "idProductPrice")
    private Long id;

    @JoinColumn(name = "idPriceListVersion", referencedColumnName = "idPriceListVersion",
            nullable = false)
    @ManyToOne(fetch = FetchType.LAZY)
    private PriceListVersion priceListVersion;

    @JoinColumn(name = "idProduct", referencedColumnName = "idProduct", nullable = false)
    @ManyToOne(fetch = FetchType.LAZY)
    private ProductOrganization product;

    /**
     * The official net price of a product in a specified currency.
     */
    @Column(name = "listPrice", nullable = false, precision = 15, scale = 6)
    private BigDecimal listPrice;

    /**
     * The regular or normal price of a product in the respective price list.
     */
    @Column(name = "price", nullable = false, precision = 15, scale = 6)
    private BigDecimal price;

    /**
     * The lowest net price a specified item may be sold for.
     */
    @Column(name = "limitPrice", nullable = false, precision = 15, scale = 6)
    private BigDecimal limitPrice;

    @Override
    public Long getId() {
        return id;
    }

    @Override
    public void setId(Long id) {
        this.id = checkPositive(id);
    }

    public PriceListVersion getPriceListVersion() {
        return priceListVersion;
    }

    public void setPriceListVersion(PriceListVersion priceListVersion) {
        this.priceListVersion = checkNotNull(priceListVersion);
    }

    public ProductOrganization getProduct() {
        return product;
    }

    public void setProduct(ProductOrganization product) {
        this.product = checkNotNull(product);
    }

    public BigDecimal getListPrice() {
        return listPrice;
    }

    public void setListPrice(BigDecimal listPrice) {
        this.listPrice = checkPositive(listPrice);
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = checkPositive(price);
    }

    public BigDecimal getLimitPrice() {
        return limitPrice;
    }

    public void setLimitPrice(BigDecimal limitPrice) {
        this.limitPrice = checkPositive(limitPrice);
    }
}
