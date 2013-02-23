// Copyright 2013 Structure Eng Inc.

package com.structureeng.persistence.model.stock;

import com.structureeng.persistence.model.AbstractTenantModel;

import com.google.common.base.Preconditions;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

/**
 * Define the different warehouses in which a
 * {@code com.structureeng.persistence.model.product.ProductType} can be stored.
 *
 * @author Edgar Rico (edgar.martinez.rico@gmail.com)
 */
@Entity
@Table(name = "warehouse", uniqueConstraints = {
        @UniqueConstraint(name = "warehouseTenantPK", columnNames = {"idTenant", "idReference"}),
        @UniqueConstraint(name = "warehouseUK", columnNames = {"idTenant", "name"})})
public class Warehouse extends AbstractTenantModel<Long> {

    @Id
    @Column(name = "idWarehouse")
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
