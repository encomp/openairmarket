// Copyright 2013 Structure Eng Inc.

package com.structureeng.persistence.model.history.product;

import static com.structureeng.persistence.model.AbstractModel.checkNotEmpty;
import static com.structureeng.persistence.model.AbstractModel.checkPositive;

import com.structureeng.persistence.history.HistoryEntityBuilder;
import com.structureeng.persistence.model.business.ProductType;
import com.structureeng.persistence.model.business.Organization;
import com.structureeng.persistence.model.business.TaxType;
import com.structureeng.persistence.model.history.AbstractHistoryTenantModel;
import com.structureeng.persistence.model.product.ProductMeasureUnit;
import com.structureeng.persistence.model.product.Product;
import com.structureeng.persistence.model.product.ProductDefinition;

import com.google.common.base.Preconditions;

import java.math.BigDecimal;
import java.math.BigInteger;

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

/**
 * Define the revision for the {@code ProductType} entities.
 *
 * @author Edgar Rico (edgar.martinez.rico@gmail.com)
 */
@Entity
@Table(name = "productHistory", uniqueConstraints = {
        @UniqueConstraint(name = "productHistoryUK",
            columnNames = {"idProduct", "idAudit"})})
public class ProductHistory extends AbstractHistoryTenantModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "idProductHistory")
    private Long id;

    @JoinColumn(name = "idProduct", referencedColumnName = "idProduct", nullable = false)
    @ManyToOne(cascade = CascadeType.REFRESH)
    private Product product;

    @JoinColumn(name = "idOrganization", referencedColumnName = "idOrganization", nullable = false)
    @ManyToOne(cascade = CascadeType.REFRESH, fetch = FetchType.LAZY)
    private Organization organization;

    @JoinColumn(name = "idProductDefinition", referencedColumnName = "idProductDefinition",
            nullable = false)
    @ManyToOne(cascade = CascadeType.REFRESH)
    private ProductDefinition productDefinition;

    @JoinColumn(name = "idProductType", referencedColumnName = "idRule", nullable = false)
    @ManyToOne(cascade = CascadeType.REFRESH)
    private ProductType productType;

    @JoinColumn(name = "idTaxType", referencedColumnName = "idRule", nullable = false)
    @ManyToOne(cascade = CascadeType.REFRESH)
    private TaxType taxType;

    @JoinColumn(name = "idProductMeasureUnit", referencedColumnName = "idProductMeasureUnit", 
            nullable = false)
    @ManyToOne(cascade = CascadeType.REFRESH)
    private ProductMeasureUnit productMeasureUnit;

    @Column(name = "idReference", nullable = false)
    private BigInteger referenceId;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "quantity", nullable = false, precision = 13, scale = 4)
    private BigDecimal quantity;

    @Column(name = "autoStock", nullable = false)
    private Boolean autoStock;

    @Column(name = "wastable", nullable = false)
    private Boolean wastable;

    @Override
    public Long getId() {
        return id;
    }

    @Override
    public void setId(Long id) {
        this.id = Preconditions.checkNotNull(id);
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = Preconditions.checkNotNull(product);
    }

    public BigInteger getReferenceId() {
        return referenceId;
    }

    public void setReferenceId(BigInteger referenceId) {
        this.referenceId = checkPositive(referenceId);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = checkNotEmpty(name);
    }

    public Boolean getAutoStock() {
        return autoStock;
    }

    public void setAutoStock(Boolean autoStock) {
        this.autoStock = Preconditions.checkNotNull(autoStock);
    }

    public Boolean getWastable() {
        return wastable;
    }

    public void setWastable(Boolean wastable) {
        this.wastable = Preconditions.checkNotNull(wastable);
    }

    public BigDecimal getQuantity() {
        return quantity;
    }

    public void setQuantity(BigDecimal quantity) {
        this.quantity = Preconditions.checkNotNull(quantity);
    }

    public Organization getOrganization() {
        return organization;
    }

    public void setOrganization(Organization organization) {
        this.organization = Preconditions.checkNotNull(organization);
    }

    public ProductDefinition getProductDefinition() {
        return productDefinition;
    }

    public void setProductDefinition(ProductDefinition productDefinition) {
        this.productDefinition = Preconditions.checkNotNull(productDefinition);
    }

    public ProductType getProductType() {
        return productType;
    }

    public void setProductType(ProductType productType) {
        this.productType = Preconditions.checkNotNull(productType);
    }

    public TaxType getTaxType() {
        return taxType;
    }

    public void setTaxType(TaxType taxType) {
        this.taxType = Preconditions.checkNotNull(taxType);
    }

    public ProductMeasureUnit getProductMeasureUnit() {
        return productMeasureUnit;
    }

    public void setProductMeasureUnit(ProductMeasureUnit measureUnit) {
        this.productMeasureUnit = measureUnit;
    }

    /**
     * Factory class for the {@code ProductHistory} entities.
     *
     * @author Edgar Rico (edgar.martinez.rico@gmail.com)
     */
    public static class Builder extends HistoryEntityBuilder<Product, ProductHistory> {

        /**
         * Create an instance of {@code ProductHistory}.
         *
         * @param product the instance that will be used to create a new {@code Product}.
         * @return a new instance
         */
        @Override
        public ProductHistory build(Product product) {
            ProductHistory productHistory = new ProductHistory();
            productHistory.setProduct(product);
            productHistory.setReferenceId(product.getReferenceId());
            productHistory.setName(product.getName());
            productHistory.setActive(product.getActive());
            productHistory.setAutoStock(product.getAutoStock());
            productHistory.setWastable(product.getWastable());
            productHistory.setQuantity(product.getQuantity());
            productHistory.setOrganization(product.getOrganization());
            productHistory.setProductDefinition(product.getProductDefinition());
            productHistory.setProductType(product.getProductType());
            productHistory.setTaxType(product.getTaxType());
            productHistory.setProductMeasureUnit(product.getProductMeasureUnit());
            productHistory.setVersion(product.getVersion());
            return productHistory;
        }
    }
}
