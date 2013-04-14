// Copyright 2013 Structure Eng Inc.

package com.structureeng.persistence.model.business;

import com.structureeng.persistence.history.HistoryListener;
import com.structureeng.persistence.history.Revision;
import com.structureeng.persistence.model.AbstractCatalogTenantModel;
import com.structureeng.persistence.model.history.business.StoreHistory;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import static com.structureeng.persistence.model.AbstractModel.checkNotEmpty;
import static com.structureeng.persistence.model.AbstractModel.checkPositive;

/**
 * Define the revision for the {@code Store} entities.
 *
 * @author Edgar Rico (edgar.martinez.rico@gmail.com)
 */
@EntityListeners(value = {HistoryListener.class})
@Revision(builder = StoreHistory.Builder.class)
@Entity
@Table(name = "store", uniqueConstraints = {
    @UniqueConstraint(name = "storeTenantPK", columnNames = {"idTenant", "idReference"}),
    @UniqueConstraint(name = "storeUK", columnNames = {"idTenant", "name"})})
public class Store extends AbstractCatalogTenantModel<Long, Integer> {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "idStore")
    private Long id;
        
    @Override
    public Long getId() {
        return id;
    }

    @Override
    public void setId(Long id) {
        this.id = checkPositive(id);
    }
    
    /**
     * Creates a new {@code Builder} instance.
     *
     * @return - new instance
     */
    public static Store.Buider newBuilder() {
        return new Store.Buider();
    }

    /**
     * Builder class that creates instances of {@code Store}.
     *
     * @author Edgar Rico (edgar.martinez.rico@gmail.com)
     */
    public static class Buider {

        private Integer referenceId;
        private String name;

        public Store.Buider setReferenceId(Integer referenceId) {
            this.referenceId = checkPositive(referenceId);
            return this;
        }

        public Store.Buider setName(String name) {
            this.name = checkNotEmpty(name);
            return this;
        }

        /**
         * Creates a new instance of {@code Store}.
         *
         * @return - new instance
         */
        public Store build() {
            Store store = new Store();
            store.setReferenceId(referenceId);
            store.setName(name);
            return store;
        }
    }
}
