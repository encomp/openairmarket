// Copyright 2013 Structure Eng Inc.

package com.structureeng.persistence.model.business;

import com.structureeng.persistence.history.HistoryListener;
import com.structureeng.persistence.history.Revision;
import com.structureeng.persistence.model.AbstractCatalogTenantModel;
import com.structureeng.persistence.model.history.business.OrganizationHistory;

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
 * Define the organizations that a {@code com.structureeng.persistence.model.tenant.Tenant} owns.
 *
 * @author Edgar Rico (edgar.martinez.rico@gmail.com)
 */
@EntityListeners(value = {HistoryListener.class})
@Revision(builder = OrganizationHistory.Builder.class)
@Entity
@Table(name = "organization", uniqueConstraints = {
    @UniqueConstraint(name = "organizationPK", columnNames = {"idTenant", "idReference"}),
    @UniqueConstraint(name = "organizationUK", columnNames = {"idTenant", "name"})})
public class Organization extends AbstractCatalogTenantModel<Long, Integer> {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "idOrganization")
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
    public static Organization.Buider newBuilder() {
        return new Organization.Buider();
    }

    /**
     * Builder class that creates instances of {@code Organization}.
     *
     * @author Edgar Rico (edgar.martinez.rico@gmail.com)
     */
    public static class Buider {

        private Integer referenceId;
        private String name;

        public Organization.Buider setReferenceId(Integer referenceId) {
            this.referenceId = checkPositive(referenceId);
            return this;
        }

        public Organization.Buider setName(String name) {
            this.name = checkNotEmpty(name);
            return this;
        }

        /**
         * Creates a new instance of {@code Organization}.
         *
         * @return - new instance
         */
        public Organization build() {
            Organization store = new Organization();
            store.setReferenceId(referenceId);
            store.setName(name);
            return store;
        }
    }
}
