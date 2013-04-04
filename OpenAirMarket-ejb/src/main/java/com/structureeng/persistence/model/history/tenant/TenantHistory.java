// Copyright 2013 Structure Eng Inc.

package com.structureeng.persistence.model.history.tenant;

import com.structureeng.persistence.history.HistoryEntityBuilder;
import com.structureeng.persistence.model.history.AbstractHistoryModel;
import com.structureeng.persistence.model.tenant.Tenant;

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
 * Define the revision for the {@code Tenant} entities.
 *
 * @author Edgar Rico (edgar.martinez.rico@gmail.com)
 */
@Entity
@Table(name = "tenantHistory", uniqueConstraints = {
        @UniqueConstraint(name = "tenantHistoryUK", 
            columnNames = {"idTenant", "idAudit"})})
public class TenantHistory extends AbstractHistoryModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "idTenantHistory")
    private Long id;

    @JoinColumn(name = "idTenant", referencedColumnName = "idTenant", nullable = false)
    @ManyToOne(fetch = FetchType.LAZY)
    private Tenant tenant;

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
        this.id = Preconditions.checkNotNull(id);
    }

    public Tenant getTenant() {
        return tenant;
    }

    public void setTenant(Tenant tenant) {
        this.tenant = Preconditions.checkNotNull(tenant);
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
     * Factory class for the {@code TenantHistory} entities.
     *
     * @author Edgar Rico (edgar.martinez.rico@gmail.com)
     */
    public static class Builder extends HistoryEntityBuilder<Tenant, TenantHistory> {

        /**
         * Create an instance of {@code TenantHistory}.
         *
         * @param tenant the instance that will be used to create a new {@code Tenant}.
         * @return a new instance
         */
        @Override
        public TenantHistory build(Tenant tenant) {            
            TenantHistory tenantHistory = new TenantHistory();            
            tenantHistory.setTenant(tenant);
            tenantHistory.setReferenceId(tenant.getReferenceId());
            tenantHistory.setName(tenant.getName());
            tenantHistory.setActive(tenant.getActive());
            tenantHistory.setVersion(tenant.getVersion());
            return tenantHistory;
        }
    }
}
