// Copyright 2013 Structure Eng Inc.

package com.structureeng.persistence.model.stock;

import com.structureeng.persistence.model.AbstractCatalogTenantModel;
import com.structureeng.persistence.model.business.Store;

import com.google.common.base.Preconditions;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
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
public class Warehouse extends AbstractCatalogTenantModel<Long, Integer> {

    @Id
    @Column(name = "idWarehouse")
    private Long id;
    
    @JoinColumn(name = "idStore", referencedColumnName = "idStore", nullable = false)
    @ManyToOne(cascade = CascadeType.REFRESH, fetch = FetchType.LAZY)
    private Store store;

    @Override
    public Long getId() {
        return id;
    }

    @Override
    public void setId(Long id) {
        this.id = checkPositive(id);
    }

    public Store getStore() {
        return store;
    }

    public void setStore(Store store) {
        this.store = Preconditions.checkNotNull(store);
    }
}
