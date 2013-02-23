// Copyright 2013 Structure Eng Inc.

package com.structureeng.persistence.model.history.product;

import com.structureeng.persistence.model.history.AbstractTenantHistoryModel;
import com.structureeng.persistence.model.product.Division;

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
 * Define the revision for the {@code Division} entities.
 *
 * @author Edgar Rico (edgar.martinez.rico@gmail.com)
 */
@Entity
@Table(name = "divisionHistory", uniqueConstraints = {
        @UniqueConstraint(name = "divsionHistoryUK", 
            columnNames = {"idDivision", "idHistoryTenant"})})
public class DivisionHistory extends AbstractTenantHistoryModel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "idDivisionHistory")
    private Long id;

    @JoinColumn(name = "idDivision", referencedColumnName = "idDivision", nullable = false)
    @ManyToOne
    private Division division;

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

    public Division getDivision() {
        return division;
    }

    public void setDivision(Division division) {
        this.division = Preconditions.checkNotNull(division);
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
        this.parentDivision = Preconditions.checkNotNull(parentDivision);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = Preconditions.checkNotNull(name);
    }
}
