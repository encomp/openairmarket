// Copyright 2013 Structure Eng Inc.

package com.structureeng.persistence.model.history.business;

import com.structureeng.persistence.history.HistoryEntityBuilder;
import com.structureeng.persistence.model.business.Organization;
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
 * Define the revision for the {@code Organization} entities.
 *
 * @author Edgar Rico (edgar.martinez.rico@gmail.com)
 */
@Entity
@Table(name = "organizationHistory", uniqueConstraints = {
    @UniqueConstraint(name = "organizationHistoryUK", columnNames = {"idOrganization", "idAudit"})})
public class OrganizationHistory extends AbstractHistoryTenantModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "idOrganizationHistory")
    private Long id;

    @JoinColumn(name = "idOrganization", referencedColumnName = "idOrganization", nullable = false)
    @ManyToOne(fetch = FetchType.LAZY)
    private Organization organization;

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

    public Organization getOrganization() {
        return organization;
    }

    public void setOrganization(Organization organization) {
        this.organization = Preconditions.checkNotNull(organization);
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
     * Factory class for the {@code OrganizationHistory} entities.
     *
     * @author Edgar Rico (edgar.martinez.rico@gmail.com)
     */
    public static class Builder extends HistoryEntityBuilder<Organization, OrganizationHistory> {

        /**
         * Create an instance of {@code OrganizationHistory}.
         *
         * @param store the instance that will be used to create a new {@code Organization}.
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
