// Copyright 2013 Structure Eng Inc.

package com.structureeng.persistence.model.stock;

import static com.structureeng.persistence.model.AbstractModel.checkNotEmpty;
import static com.structureeng.persistence.model.AbstractModel.checkPositive;

import com.structureeng.persistence.history.HistoryListener;
import com.structureeng.persistence.history.Revision;
import com.structureeng.persistence.model.AbstractCatalogTenantModel;
import com.structureeng.persistence.model.business.Store;
import com.structureeng.persistence.model.history.stock.WarehouseHistory;

import com.google.common.base.Preconditions;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
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
@EntityListeners(value = {HistoryListener.class})
@Revision(builder = WarehouseHistory.Builder.class)
@Entity
@Table(name = "warehouse", uniqueConstraints = {
        @UniqueConstraint(name = "warehouseTenantPK", columnNames = {"idTenant", "idReference"}),
        @UniqueConstraint(name = "warehouseUK", columnNames = {"idTenant", "name"})})
public class Warehouse extends AbstractCatalogTenantModel<Long, Integer> {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
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
    
    /**
     * Creates a new {@code Builder} instance.
     *
     * @return - new instance
     */
    public static Warehouse.Buider newBuilder() {
        return new Warehouse.Buider();
    }
    
     /**
     * Builder class that creates instances of {@code Warehouse}.
     *
     * @author Edgar Rico (edgar.martinez.rico@gmail.com)
     */
    public static class Buider {

        private Integer referenceId;
        private String name;
        private Store store;

        public Warehouse.Buider setReferenceId(Integer referenceId) {
            this.referenceId = checkPositive(referenceId);
            return this;
        }

        public Warehouse.Buider setName(String name) {
            this.name = checkNotEmpty(name);
            return this;
        }
        
        public Warehouse.Buider setStore(Store store) {
            this.store = Preconditions.checkNotNull(store);
            return this;
        }

        /**
         * Creates a new instance of {@code Warehouse}.
         *
         * @return - new instance
         */
        public Warehouse build() {
            Warehouse warehouse = new Warehouse();
            warehouse.setReferenceId(referenceId);
            warehouse.setName(name);
            warehouse.setStore(store);
            return warehouse;
        }
    }
}
