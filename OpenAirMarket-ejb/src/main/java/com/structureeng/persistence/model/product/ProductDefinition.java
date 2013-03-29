// Copyright 2013 Structure Eng Inc.

package com.structureeng.persistence.model.product;

import com.structureeng.persistence.model.AbstractCatalogTenantModel;

import com.google.common.base.Preconditions;

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
 * Specifies the characteristics of a product.
 *
 * @author Edgar Rico (edgar.martinez.rico@gmail.com)
 */
@Entity
@Table(name = "productDefinition", uniqueConstraints = {
        @UniqueConstraint(name = "productDefinitionTenantPK",
                columnNames = {"idTenant", "idReference"}),
        @UniqueConstraint(name = "productDefinitionPK", columnNames = {"idTenant", "name"}),
        @UniqueConstraint(name = "productDefinitionUK", columnNames = {"idTenant", "idKey"})})
public class ProductDefinition extends AbstractCatalogTenantModel<Long> {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "idProductDefinition")
    private Long id;
    
    @Column(name = "idKey", nullable = false, length = 50)
    private String key;

    @Column(name = "image", length = 500)
    private String image;

    @Column(name = "countable", nullable = false)
    private Boolean countable;

    @Column(name = "expire", nullable = false)
    private Boolean expire;

    @JoinColumn(name = "idCompany", referencedColumnName = "idCompany", nullable = false)
    @ManyToOne(cascade = CascadeType.REFRESH)
    private Company company;

    @JoinColumn(name = "idDivision", referencedColumnName = "idDivision", nullable = false)
    @ManyToOne(cascade = CascadeType.REFRESH)
    private Division division;

    @Override
    public Long getId() {
        return id;
    }

    @Override
    public void setId(Long id) {
        this.id = checkPositive(id);
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = checkNotEmpty(key);
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public Boolean getCountable() {
        return countable;
    }

    public void setCountable(Boolean countable) {
        this.countable = Preconditions.checkNotNull(countable);
    }

    public Boolean getExpire() {
        return expire;
    }

    public void setExpire(Boolean expire) {
        this.expire = Preconditions.checkNotNull(expire);
    }

    public Company getCompany() {
        return company;
    }

    public void setCompany(Company company) {
        this.company = Preconditions.checkNotNull(company);
    }

    public Division getDivision() {
        return division;
    }

    public void setDivision(Division division) {
        this.division = Preconditions.checkNotNull(division);
    }
    
    /**
     * Builder class that creates instances of {@code ProductDefinition}.
     *
     * @author Edgar Rico (edgar.martinez.rico@gmail.com)
     */
    public static class Buider {

        private Integer referenceId;
        private String name;
        private String key;
        private String image;
        private Boolean countable;
        private Boolean expire;
        private Company company;
        private Division division;        

        public Buider setReferenceId(Integer referenceId) {
            this.referenceId = checkPositive(referenceId);
            return this;
        }

        public Buider setName(String name) {
            this.name = checkNotEmpty(name);
            return this;
        }

        public Buider setKey(String key) {
            this.key = checkNotEmpty(key);
            return this;
        }

        public Buider setImage(String image) {
            this.image = image;
            return this;
        }

        public Buider setCountable(Boolean countable) {
            this.countable = Preconditions.checkNotNull(countable);
            return this;
        }

        public Buider setExpire(Boolean expire) {
            this.expire = Preconditions.checkNotNull(expire);
            return this;
        }

        public Buider setCompany(Company company) {
            this.company = Preconditions.checkNotNull(company);
            return this;
        }

        public Buider setDivision(Division division) {
            this.division = Preconditions.checkNotNull(division);
            return this;
        }

        /**
         * Creates a new instance of {@code ProductDefinition}.
         *
         * @return - new instance
         */
        public ProductDefinition build() {
            ProductDefinition productDefinition = new ProductDefinition();
            productDefinition.setReferenceId(referenceId);
            productDefinition.setName(name);
            productDefinition.setKey(key);
            productDefinition.setImage(image);
            productDefinition.setCountable(countable);
            productDefinition.setExpire(expire);
            productDefinition.setCompany(company);
            productDefinition.setDivision(division);
            return productDefinition;
        }
    }
}
