// Copyright 2013 Structure Eng Inc.

package com.structureeng.persistence.model.product;

import static com.structureeng.persistence.model.AbstractModel.checkNotEmpty;
import static com.structureeng.persistence.model.AbstractModel.checkPositive;

import com.structureeng.persistence.history.HistoryListener;
import com.structureeng.persistence.history.Revision;
import com.structureeng.persistence.model.AbstractCatalogTenantModel;
import com.structureeng.persistence.model.business.ProductType;
import com.structureeng.persistence.model.business.TaxType;
import com.structureeng.persistence.model.history.product.ProductHistory;

import com.google.common.base.Preconditions;

import java.math.BigDecimal;
import java.math.BigInteger;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

/**
 * Defines the way a {@code ProductDe} will be bought.
 *
 * @author Edgar Rico (edgar.martinez.rico@gmail.com)
 */
@EntityListeners(value = {HistoryListener.class})
@Revision(builder = ProductHistory.Builder.class)
@Entity
@Table(name = "product", uniqueConstraints = {
        @UniqueConstraint(name = "productTenantPK",
                columnNames = {"idTenant", "idReference"}),
        @UniqueConstraint(name = "productUK",
                columnNames = {"idTenant", "idProductDefinition", "idRule"})})
public class Product extends AbstractCatalogTenantModel<Long, BigInteger> {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "idProduct")
    private Long id;
        
    @Column(name = "quantity", nullable = false, precision = 13, scale = 4)
    private BigDecimal quantity;

    @Column(name = "cost", nullable = false, precision = 13, scale = 4)
    private BigDecimal cost;

    @Column(name = "lastCost", nullable = false, precision = 13, scale = 4)
    private BigDecimal lastCost;
    
    @Column(name = "autoStock", nullable = false)
    private Boolean autoStock;
    
    @Column(name = "wastable", nullable = false)
    private Boolean wastable;

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
    
    @Override
    public Long getId() {
        return id;
    }

    @Override
    public void setId(Long id) {
        this.id = Preconditions.checkNotNull(id);
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
        this.quantity = checkPositive(quantity);
    }

    public BigDecimal getCost() {
        return cost;
    }

    public void setCost(BigDecimal cost) {
        this.cost = checkPositive(cost);
    }

    public BigDecimal getLastCost() {
        return lastCost;
    }

    public void setLastCost(BigDecimal lastCost) {
        this.lastCost = checkPositive(lastCost);
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
    
    /**
     * Creates a new {@code Product.Builder} instance.
     *
     * @return - new instance
     */
    public static Product.Buider newBuilder() {
        return new Product.Buider();
    }

    /**
     * Builder class that creates instances of {@code Product}.
     *
     * @author Edgar Rico (edgar.martinez.rico@gmail.com)
     */
    public static class Buider {

        private BigInteger referenceId;
        private String name;
        private BigDecimal quantity;
        private BigDecimal cost;
        private BigDecimal lastCost;
        private Boolean autoStock = Boolean.FALSE;
        private Boolean wastable = Boolean.FALSE;
        private ProductDefinition productDefinition;
        private ProductType productType;
        private TaxType taxType;
        
        public Product.Buider setReferenceId(BigInteger referenceId) {
            this.referenceId = checkPositive(referenceId);
            return this;
        }

        public Product.Buider setName(String name) {
            this.name = checkNotEmpty(name);
            return this;
        }
        
        public Product.Buider setQuantity(BigDecimal quantity) {
            this.quantity = checkPositive(quantity);
            return this;
        }

        public Product.Buider setCost(BigDecimal cost) {
            this.cost = checkPositive(cost);
            return this;
        }

        public Product.Buider setLastCost(BigDecimal lastCost) {
            this.lastCost = checkPositive(lastCost);
            return this;
        }

        public Product.Buider setAutoStock(Boolean autoStock) {
            this.autoStock = Preconditions.checkNotNull(autoStock);
            return this;
        }

        public Product.Buider setWastable(Boolean wastable) {
            this.wastable = Preconditions.checkNotNull(wastable);
            return this;
        }

        public Product.Buider setProductDefinition(ProductDefinition productDefinition) {
            this.productDefinition = Preconditions.checkNotNull(productDefinition);
            return this;
        }

        public Product.Buider setProductType(ProductType productType) {
            this.productType = Preconditions.checkNotNull(productType);
            return this;
        }

        public Product.Buider setTaxType(TaxType taxType) {
            this.taxType = Preconditions.checkNotNull(taxType);
            return this;
        }
        
        /**
         * Creates a new instance of {@code Product}.
         *
         * @return - new instance
         */
        public Product build() {
            Product product = new Product();
            product.setReferenceId(referenceId);
            product.setName(name);
            product.setWastable(wastable);
            product.setAutoStock(autoStock);
            product.setQuantity(quantity);
            product.setCost(cost);
            product.setLastCost(lastCost);
            product.setProductDefinition(productDefinition);
            product.setProductType(productType);
            product.setTaxType(taxType);
            return product;
        }
    }
}
