// Copyright 2013 Structure Eng Inc.

package com.structureeng.persistence.model.history.product;

import com.structureeng.persistence.history.HistoryEntityBuilder;
import com.structureeng.persistence.model.business.Organization;
import com.structureeng.persistence.model.business.TaxCategory;
import com.structureeng.persistence.model.history.AbstractHistoryTenantModel;
import com.structureeng.persistence.model.product.ProductOrganization;
import com.structureeng.persistence.model.product.Product;
import com.structureeng.persistence.model.product.ProductCategory;

import com.google.common.base.Preconditions;

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

import static com.google.common.base.Preconditions.checkNotNull;
import static com.structureeng.persistence.model.AbstractModel.checkPositive;

/**
 * Define the revision for the {@code ProductOrganization} entities.
 *
 * @author Edgar Rico (edgar.martinez.rico@gmail.com)
 */
@Entity
@Table(name = "productOrganizationHistory", uniqueConstraints = {
        @UniqueConstraint(name = "productOrganizationHistoryUK",
            columnNames = {"idProductOrganization", "idAudit"})})
public class ProductOrganizationHistory extends AbstractHistoryTenantModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "idProductOrganizationHistory")
    private Long id;

    @JoinColumn(name = "idProductOrganization", referencedColumnName = "idProduct",
            nullable = false)
    @ManyToOne(cascade = CascadeType.REFRESH)
    private ProductOrganization productOrganization;

    @JoinColumn(name = "idOrganization", referencedColumnName = "idOrganization", nullable = false)
    @ManyToOne(cascade = CascadeType.REFRESH, fetch = FetchType.LAZY)
    private Organization organization;

    @JoinColumn(name = "idProduct", referencedColumnName = "idProduct",
            nullable = false)
    @ManyToOne(cascade = CascadeType.REFRESH, fetch = FetchType.LAZY)
    private Product product;

    @Column(name = "upc", nullable = false)
    private BigInteger upc;
    
    @JoinColumn(name = "idProductCategory", referencedColumnName = "idProductCategory",
            nullable = false)
    @ManyToOne(cascade = CascadeType.REFRESH)
    private ProductCategory productCategory;

    @JoinColumn(name = "idTaxCategory", referencedColumnName = "idRule", nullable = false)
    @ManyToOne(cascade = CascadeType.REFRESH, fetch = FetchType.LAZY)
    private TaxCategory taxCategory;

    @Override
    public Long getId() {
        return id;
    }

    @Override
    public void setId(Long id) {
        this.id = Preconditions.checkNotNull(id);
    }

    public ProductOrganization ProductOrganization() {
        return productOrganization;
    }

    public void setProductOrganization(ProductOrganization productOrganization) {
        this.productOrganization = Preconditions.checkNotNull(productOrganization);
    }

    public Organization getOrganization() {
        return organization;
    }

    public void setOrganization(Organization organization) {
        this.organization = Preconditions.checkNotNull(organization);
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = Preconditions.checkNotNull(product);
    }

    public BigInteger getUpc() {
        return upc;
    }

    public void setUpc(BigInteger upc) {
        this.upc = checkPositive(upc);
    }
    
    public ProductCategory getProductCategory() {
        return productCategory;
    }

    public void setProductCategory(ProductCategory productCategory) {
        this.productCategory = checkNotNull(productCategory);
    }

    public TaxCategory getTaxCategory() {
        return taxCategory;
    }

    public void setTaxCategory(TaxCategory taxCategory) {
        this.taxCategory = Preconditions.checkNotNull(taxCategory);
    }

    /**
     * Factory class for the {@code ProductOrganizationHistory} entities.
     *
     * @author Edgar Rico (edgar.martinez.rico@gmail.com)
     */
    public static class Builder extends HistoryEntityBuilder<ProductOrganization,
            ProductOrganizationHistory> {

        /**
         * Create an instance of {@code ProductOrganizationHistory}.
         *
         * @param productOrganization the instance that will be used to create a new
         *                            {@code ProductOrganization}.
         * @return a new instance
         */
        @Override
        public ProductOrganizationHistory build(ProductOrganization productOrganization) {
            ProductOrganizationHistory productHistory = new ProductOrganizationHistory();
            productHistory.setProductOrganization(productOrganization);
            productHistory.setActive(productOrganization.getActive());
            productHistory.setOrganization(productOrganization.getOrganization());
            productHistory.setProduct(productOrganization.getProduct());
            productHistory.setProductCategory(productOrganization.getProductCategory());
            productHistory.setUpc(productOrganization.getUpc());
            productHistory.setTaxCategory(productOrganization.getTaxCategory());
            productHistory.setVersion(productOrganization.getVersion());
            return productHistory;
        }
    }
}
