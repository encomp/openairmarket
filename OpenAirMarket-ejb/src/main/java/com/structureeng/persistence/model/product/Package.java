// Copyright 2013 Structure Eng Inc.

package com.structureeng.persistence.model.product;

import com.structureeng.persistence.model.AbstractTenantModel;

import com.google.common.base.Preconditions;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

/**
 * Define the different packages of a
 * {@code com.structureeng.persistence.model.product.ProductType}.
 *
 * @author Edgar Rico (edgar.martinez.rico@gmail.com)
 */
@Entity
@Table(name = "package", uniqueConstraints = {
        @UniqueConstraint(name = "packageTenantPK", columnNames = {"idTenant", "idReference"}),
        @UniqueConstraint(name = "packageUK", columnNames = {"idTenant", "name"})})
public class Package extends AbstractTenantModel<Long> {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "idPackage")
    private Long id;

    @Column(name = "idReference", nullable = false)
    private Integer referenceId;

    @Column(name = "name", nullable = false)
    private String name;

    @Override
    public Long getId() {
        return id;
    }

    @Override
    public void setId(Long id) {
        this.id = Preconditions.checkNotNull(id);
    }

    public Integer getReferenceId() {
        return referenceId;
    }

    public void setReferenceId(Integer referenceId) {
        this.referenceId = Preconditions.checkNotNull(referenceId);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = Preconditions.checkNotNull(name);
    }
}
