// Copyright 2013 Structure Eng Inc.

package com.structureeng.persistence.model.history.product;

import com.structureeng.persistence.model.history.AbstractTenantHistoryModel;
import com.structureeng.persistence.model.product.Package;
import com.structureeng.persistence.model.product.Product;
import com.structureeng.persistence.model.product.RetailProduct;

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
 * Define the revision for the {@code RetailProduct} entities.
 *
 * @author Edgar Rico (edgar.martinez.rico@gmail.com)
 */
@Entity
@Table(name = "retailProductHistory", uniqueConstraints = {
        @UniqueConstraint(name = "productRetailHistoryUK",
                columnNames = {"idRetailProduct", "idHistoryTenant"})})
public class RetailProductHistory extends AbstractTenantHistoryModel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "idProductRetailHistory")
    private Long id;

    @JoinColumn(name = "idRetailProduct", referencedColumnName = "idRetailProduct",
            nullable = true)
    @ManyToOne(cascade = CascadeType.REFRESH)
    private RetailProduct retailProduct;

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

    @JoinColumn(name = "idPackage", referencedColumnName = "idPackage", nullable = false)
    @ManyToOne(cascade = CascadeType.REFRESH)
    private Package aPackage;

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

    public RetailProduct getRetailProduct() {
        return retailProduct;
    }

    public void setRetailProduct(RetailProduct retailProduct) {
        this.retailProduct = Preconditions.checkNotNull(retailProduct);
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

    public Package getaPackage() {
        return aPackage;
    }

    public void setaPackage(Package aPackage) {
        this.aPackage = Preconditions.checkNotNull(aPackage);
    }

    public RetailProduct getRetailParentProduct() {
        return retailParentProduct;
    }

    public void setRetailParentProduct(RetailProduct retailParentProdcut) {
        this.retailParentProduct = retailParentProdcut;
    }
}
