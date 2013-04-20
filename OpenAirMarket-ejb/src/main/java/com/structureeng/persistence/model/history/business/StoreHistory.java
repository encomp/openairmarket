// Copyright 2013 Structure Eng Inc.

package com.structureeng.persistence.model.history.business;

import com.structureeng.persistence.history.HistoryEntityBuilder;
import com.structureeng.persistence.model.business.Store;
import com.structureeng.persistence.model.history.AbstractHistoryTenantModel;

import com.google.common.base.Preconditions;
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

import static com.structureeng.persistence.model.AbstractModel.checkNotEmpty;
import static com.structureeng.persistence.model.AbstractModel.checkPositive;

/**
 * Define the stores that a {@code com.structureeng.persistence.model.tenant.Tenant} owns.
 *
 * @author Edgar Rico (edgar.martinez.rico@gmail.com)
 */
@Entity
@Table(name = "storeHistory", uniqueConstraints = {
    @UniqueConstraint(name = "storeHistoryUK", columnNames = {"idStore", "idAudit"})})
public class StoreHistory extends AbstractHistoryTenantModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "idStoreHistory")
    private Long id;

    @JoinColumn(name = "idStore", referencedColumnName = "idStore", nullable = false)
    @ManyToOne(fetch = FetchType.LAZY)
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
        this.id = checkPositive(id);
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

    /**
     * Factory class for the {@code StoreHistory} entities.
     *
     * @author Edgar Rico (edgar.martinez.rico@gmail.com)
     */
    public static class Builder extends HistoryEntityBuilder<Store, StoreHistory> {

        /**
         * Create an instance of {@code StoreHistory}.
         *
         * @param store the instance that will be used to create a new {@code Store}.
         * @return a new instance
         */
        @Override
        public StoreHistory build(Store store) {
            StoreHistory storeHistory = new StoreHistory();
            storeHistory.setStore(store);
            storeHistory.setReferenceId(store.getReferenceId());
            storeHistory.setName(store.getName());
            storeHistory.setActive(store.getActive());
            storeHistory.setVersion(store.getVersion());
            return storeHistory;
        }
    }
}
