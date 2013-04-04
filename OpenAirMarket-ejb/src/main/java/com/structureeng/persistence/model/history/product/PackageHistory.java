// Copyright 2013 Structure Eng Inc.

package com.structureeng.persistence.model.history.product;

import com.structureeng.persistence.history.HistoryEntityBuilder;
import com.structureeng.persistence.model.history.AbstractHistoryModel;
import com.structureeng.persistence.model.product.Package;

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

/**
 * Define the revision for the {@code Package} entities.
 *
 * @author Edgar Rico (edgar.martinez.rico@gmail.com)
 */
@Entity
@Table(name = "packageHistory", uniqueConstraints = {
        @UniqueConstraint(name = "packageHistoryUK", 
            columnNames = {"idPackage", "idAudit"})})
public class PackageHistory extends AbstractHistoryModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "idPackageHistory")
    private Long id;

    @JoinColumn(name = "idPackage", referencedColumnName = "idPackage", nullable = false)
    @ManyToOne(fetch = FetchType.LAZY)
    private Package aPackage;

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

    public Package getPackage() {
        return aPackage;
    }

    public void setPackage(Package aPackage) {
        this.aPackage = Preconditions.checkNotNull(aPackage);
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
     * Factory class for the {@code CompanyHistory} entities.
     *
     * @author Edgar Rico (edgar.martinez.rico@gmail.com)
     */
    public static class Builder extends HistoryEntityBuilder<Package, PackageHistory> {

        /**
         * Create an instance of {@code PackageHistory}.
         *
         * @param aPackage the instance that will be used to create a new {@code Company}.
         * @return a new instance
         */
        @Override
        public PackageHistory build(Package aPackage) {
            PackageHistory companyHistory = new PackageHistory();
            companyHistory.setPackage(aPackage);
            companyHistory.setReferenceId(aPackage.getReferenceId());
            companyHistory.setName(aPackage.getName());
            companyHistory.setActive(aPackage.getActive());
            companyHistory.setVersion(aPackage.getVersion());
            return companyHistory;
        }
    }
}
