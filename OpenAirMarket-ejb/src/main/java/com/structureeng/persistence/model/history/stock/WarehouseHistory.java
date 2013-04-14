// Copyright 2013 Structure Eng Inc.

package com.structureeng.persistence.model.history.stock;

import com.structureeng.persistence.model.business.Store;
import com.structureeng.persistence.model.history.AbstractHistoryTenantModel;
import com.structureeng.persistence.model.stock.Warehouse;

import com.google.common.base.Preconditions;

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
 * Define the revision for the {@code Warehouse} entities.
 *
 * @author Edgar Rico (edgar.martinez.rico@gmail.com)
 */
@Entity
@Table(name = "warehouseHistory", uniqueConstraints = {
        @UniqueConstraint(name = "warehouseHistoryUK", 
            columnNames = {"idWarehouse", "idAudit"})})
public class WarehouseHistory extends AbstractHistoryTenantModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "idWarehouseHistory")
    private Long id;

    @JoinColumn(name = "idWarehouse", referencedColumnName = "idWarehouse", nullable = false)
    @ManyToOne(cascade = CascadeType.REFRESH)
    private Warehouse warehouse;
    
    @JoinColumn(name = "idStore", referencedColumnName = "idStore", nullable = false)
    @ManyToOne(cascade = CascadeType.REFRESH, fetch = FetchType.LAZY)
    private Store store;

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

    public Warehouse getWarehouse() {
        return warehouse;
    }

    public void setWarehouse(Warehouse warehouse) {
        this.warehouse = Preconditions.checkNotNull(warehouse);
    }

    public Store getStore() {
        return store;
    }

    public void setStore(Store store) {
        this.store = Preconditions.checkNotNull(store);
    }

    public Integer getReferenceId() {
        return referenceId;
    }

    public void setReferenceId(Integer referenceId) {
        this.referenceId = checkPositive(referenceId);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = checkNotEmpty(name);
    }
}
