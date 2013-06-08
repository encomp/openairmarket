// Copyright 2013 Structure Eng Inc.

package com.structureeng.persistence.model.history.business;

import com.structureeng.persistence.model.business.Organization;
import com.structureeng.persistence.model.business.RuleOrganization;
import com.structureeng.persistence.model.history.AbstractHistoryCatalogTenantModel;
import com.structureeng.persistence.model.history.AbstractHistoryTenantModel;

import com.google.common.base.Preconditions;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.DiscriminatorColumn;
import javax.persistence.DiscriminatorType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Define the revision for the {@code Rule} entities.
 *
 * @author Edgar Rico (edgar.martinez.rico@gmail.com)
 */
@Entity
@Table(name = "ruleOrganizationHistory", uniqueConstraints = {
        @UniqueConstraint(name = "ruleOrganizationHistoryPK", 
            columnNames = {"idRuleOrganization", "idAudit"})})
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "ruleType", discriminatorType = DiscriminatorType.STRING, length = 100)
public abstract class RuleOrganizationHistory extends AbstractHistoryCatalogTenantModel<String> {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "idRuleOrganizationHistory")
    private Long id;

    @JoinColumn(name = "idRuleOrganization", referencedColumnName = "idRuleOrganization", 
            nullable = false)
    @ManyToOne(cascade = CascadeType.REFRESH)
    private RuleOrganization ruleOrganization;
    
    @JoinColumn(name = "idOrganization", referencedColumnName = "idOrganization", nullable = false)
    @ManyToOne(cascade = CascadeType.REFRESH, fetch = FetchType.LAZY)
    private Organization organization;

    @JoinColumn(name = "idParentRuleOrganization", referencedColumnName = "idRuleOrganization")
    @ManyToOne(cascade = CascadeType.REFRESH)
    private RuleOrganization parentRule;
    
    @Column(name = "default")
    private Boolean defaulted;

    @Override
    public Long getId() {
        return id;
    }

    @Override
    public void setId(Long id) {
        this.id = Preconditions.checkNotNull(id);
    }

    public RuleOrganization getRuleOrganization() {
        return ruleOrganization;
    }

    public void setRuleOrganization(RuleOrganization ruleOrganization) {
        this.ruleOrganization = Preconditions.checkNotNull(ruleOrganization);
    }
    
    public Organization getOrganization() {
        return organization;
    }

    public void setOrganization(Organization organization) {
        this.organization = checkNotNull(organization);
    }

    public RuleOrganization getParentRule() {
        return parentRule;
    }

    public void setParentRule(RuleOrganization parentRule) {
        this.parentRule = parentRule;
    }

    public Boolean getDefaulted() {
        return defaulted;
    }

    public void setDefaulted(Boolean defaulted) {
        this.defaulted = defaulted;
    }
}
