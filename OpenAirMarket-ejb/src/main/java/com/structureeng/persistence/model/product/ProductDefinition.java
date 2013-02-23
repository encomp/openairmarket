// Copyright 2013 Structure Eng Inc.

package com.structureeng.persistence.model.product;

import com.structureeng.persistence.model.AbstractTenantModel;

import com.google.common.base.Preconditions;

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
public class ProductDefinition extends AbstractTenantModel<Long> {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "idProductDefinition")
    private Long id;

    @Column(name = "idReference", nullable = false)
    private BigInteger referenceId;

    @Column(name = "idKey", nullable = false, length = 50)
    private String key;

    @Column(name = "name", nullable = false, length = 500)
    private String name;

    @Column(name = "imagen", length = 500)
    private String imagen;

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
        this.id = Preconditions.checkNotNull(id);
    }

    public BigInteger getReferenceId() {
        return referenceId;
    }

    public void setReferenceId(BigInteger referenceId) {
        this.referenceId = Preconditions.checkNotNull(referenceId);
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = Preconditions.checkNotNull(key);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = Preconditions.checkNotNull(name);
    }

    public String getImagen() {
        return imagen;
    }

    public void setImagen(String imagen) {
        this.imagen = imagen;
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
}
