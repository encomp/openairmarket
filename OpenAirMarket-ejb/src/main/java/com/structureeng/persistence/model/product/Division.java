// Copyright 2013 Structure Eng Inc.

package com.structureeng.persistence.model.product;

import com.structureeng.persistence.model.AbstractTenantModel;

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
 * Define the different categories in which a
 * {@code com.structureeng.persistence.model.product.ProductType} can be assigned.
 *
 * @author Edgar Rico (edgar.martinez.rico@gmail.com)
 */
@Entity
@Table(name = "division", uniqueConstraints = {
        @UniqueConstraint(name = "divisionTenantPK", columnNames = {"idTenant", "idReference"}),
        @UniqueConstraint(name = "divisionUK", columnNames = {"idTenant", "name"})})
public class Division extends AbstractTenantModel<Long> {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "idDivision")
    private Long id;

    @Column(name = "idReference", nullable = false)
    private Integer referenceId;

    @ManyToOne(cascade = CascadeType.REFRESH)
    @JoinColumn(name = "idParentDivision", referencedColumnName = "idDivision", nullable = true)
    private Division parentDivision;

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

    public Division getParentDivision() {
        return parentDivision;
    }

    public void setParentDivision(Division parentDivision) {
        this.parentDivision = parentDivision;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = Preconditions.checkNotNull(name);
    }
}
