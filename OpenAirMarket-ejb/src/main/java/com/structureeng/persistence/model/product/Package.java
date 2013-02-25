// Copyright 2013 Structure Eng Inc.

package com.structureeng.persistence.model.product;

import com.structureeng.persistence.history.HistoryListener;
import com.structureeng.persistence.history.Revision;
import com.structureeng.persistence.model.AbstractCatalogModel;
import com.structureeng.persistence.model.history.product.PackageHistory;

import com.google.common.base.Preconditions;

import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

/**
 * Define the different packages of a
 * {@code com.structureeng.persistence.model.product.ProductType}.
 *
 * @author Edgar Rico (edgar.martinez.rico@gmail.com)
 */
@EntityListeners(value = {HistoryListener.class})
@Revision(builder = PackageHistory.Builder.class)
@Entity
@Table(name = "package", uniqueConstraints = {
        @UniqueConstraint(name = "packageTenantPK", columnNames = {"idTenant", "idReference"}),
        @UniqueConstraint(name = "packageUK", columnNames = {"idTenant", "name"})})
public class Package extends AbstractCatalogModel<Long> {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "idPackage")
    private Long id;

    @Column(name = "name", nullable = false)
    private String name;
    
    @OneToMany(mappedBy = "aPackage", cascade = CascadeType.ALL)
    private Set<PackageHistory> packageHistories;

    @Override
    public Long getId() {
        return id;
    }

    @Override
    public void setId(Long id) {
        this.id = checkPositive(id);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = checkNotEmpty(name);
    }

    public Set<PackageHistory> getPackageHistories() {
        return packageHistories;
    }

    public void setPackageHistories(Set<PackageHistory> packageHistories) {
        this.packageHistories = Preconditions.checkNotNull(packageHistories);
    }
            
    /**
     * Creates a new {@code Builder} instance.
     *
     * @return - new instance
     */
    public static Package.Buider newBuilder() {
        return new Package.Buider();
    }

    /**
     * Builder class that creates instances of {@code Package}.
     *
     * @author Edgar Rico (edgar.martinez.rico@gmail.com)
     */
    public static class Buider {

        private Integer referenceId;
        private String name;

        public Package.Buider setReferenceId(Integer referenceId) {
            this.referenceId = Preconditions.checkNotNull(referenceId);
            return this;
        }

        public Package.Buider setName(String name) {
            this.name = Preconditions.checkNotNull(name);
            return this;
        }

        /**
         * Creates a new instance of {@code Package}.
         *
         * @return - new instance
         */
        public Package build() {
            Package paquete = new Package();
            paquete.setReferenceId(referenceId);
            paquete.setName(name);
            return paquete;
        }
    }
}
