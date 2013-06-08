// Copyright 2013 Structure Eng Inc.

package com.structureeng.persistence.model.history.business;

import static com.google.common.base.Preconditions.checkNotNull;

import com.structureeng.persistence.history.HistoryEntityBuilder;
import com.structureeng.persistence.model.business.Organization;
import com.structureeng.persistence.model.history.AbstractHistoryCatalogTenantModel;

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
 * Define the revision for the {@code Organization} entities.
 *
 * @author Edgar Rico (edgar.martinez.rico@gmail.com)
 */
@Entity
@Table(name = "organizationHistory", uniqueConstraints = {
    @UniqueConstraint(name = "organizationHistoryUK", columnNames = {"idOrganization", "idAudit"})})
public class OrganizationHistory extends AbstractHistoryCatalogTenantModel<Integer> {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "idOrganizationHistory")
    private Long id;

    @JoinColumn(name = "idOrganization", referencedColumnName = "idOrganization", nullable = false)
    @ManyToOne(fetch = FetchType.LAZY)
    private Organization organization;

    @Override
    public Long getId() {
        return id;
    }

    @Override
    public void setId(Long id) {
        this.id = checkPositive(id);
    }

    public Organization getOrganization() {
        return organization;
    }

    public void setOrganization(Organization organization) {
        this.organization = checkNotNull(organization);
    }

    /**
     * Factory class for the {@code OrganizationHistory} entities.
     *
     * @author Edgar Rico (edgar.martinez.rico@gmail.com)
     */
    public static class Builder extends HistoryEntityBuilder<Organization, OrganizationHistory> {

        /**
         * Create an instance of {@code OrganizationHistory}.
         *
         * @param organization the instance that will be used to create a new {@code Organization}.
         * @return a new instance
         */
        @Override
        public OrganizationHistory build(Organization organization) {
            OrganizationHistory organizationHistory = new OrganizationHistory();
            organizationHistory.setOrganization(organization);
            organizationHistory.setReferenceId(organization.getReferenceId());
            organizationHistory.setName(organization.getName());
            organizationHistory.setActive(organization.getActive());
            organizationHistory.setVersion(organization.getVersion());
            return organizationHistory;
        }
    }
}
