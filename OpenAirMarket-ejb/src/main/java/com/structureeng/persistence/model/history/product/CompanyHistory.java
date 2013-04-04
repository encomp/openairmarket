// Copyright 2013 Structure Eng Inc.

package com.structureeng.persistence.model.history.product;

import com.structureeng.persistence.history.HistoryEntityBuilder;
import com.structureeng.persistence.model.history.AbstractHistoryModel;
import com.structureeng.persistence.model.product.Company;

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
 * Define the revision for the {@code Company} entities.
 *
 * @author Edgar Rico (edgar.martinez.rico@gmail.com)
 */
@Entity
@Table(name = "companyHistory", uniqueConstraints = {
    @UniqueConstraint(name = "companyHistoryUK",
    columnNames = {"idCompany", "idAudit"})})
public class CompanyHistory extends AbstractHistoryModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "idCompanyHistory")
    private Long id;
    
    @JoinColumn(name = "idCompany", referencedColumnName = "idCompany", nullable = false)
    @ManyToOne(fetch = FetchType.LAZY)
    private Company company;
    
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

    public Company getCompany() {
        return company;
    }

    public void setCompany(Company company) {
        this.company = Preconditions.checkNotNull(company);
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
    public static class Builder extends HistoryEntityBuilder<Company, CompanyHistory> {

        /**
         * Create an instance of {@code CompanyHistory}.
         *
         * @param company the instance that will be used to create a new {@code Company}.
         * @return a new instance
         */
        @Override
        public CompanyHistory build(Company company) {
            CompanyHistory companyHistory = new CompanyHistory();
            companyHistory.setCompany(company);
            companyHistory.setReferenceId(company.getReferenceId());
            companyHistory.setName(company.getName());
            companyHistory.setActive(company.getActive());
            companyHistory.setVersion(company.getVersion());
            return companyHistory;
        }
    }
}
