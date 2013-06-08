// Copyright 2013 Structure Eng Inc.

package com.structureeng.persistence.model.business;

import static com.google.common.base.Preconditions.checkNotNull;

import com.structureeng.persistence.model.AbstractCatalogTenantModel;

import java.math.BigDecimal;

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
 * Defines the attributes of a {@code Rule}.
 *
 * @author Edgar Rico (edgar.martinez.rico@gmail.com)
 */
@Entity
@Table(name = "ruleOrganizationDetail", uniqueConstraints = {
        @UniqueConstraint(name = "ruleDetailTenantPK",
                columnNames = {"idTenant", "idRuleOrganization", "idReference"})})
public class RuleOrganizationDetail extends AbstractCatalogTenantModel<Long, String> {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "idRuleOrganizationDetail")
    private Long id;

    @JoinColumn(name = "idRuleOrganization", referencedColumnName = "idRuleOrganization", 
            nullable = false)
    @ManyToOne(fetch = FetchType.LAZY)
    private RuleOrganization ruleOrganization;

    @JoinColumn(name = "idParentRuleOrganizationDetail", 
            referencedColumnName = "idRuleOrganizationDetail")
    @ManyToOne(fetch = FetchType.LAZY)
    private RuleOrganizationDetail ruleOrganizationDetail;

    @Override
    public Long getId() {
        return id;
    }

    @Override
    public void setId(Long id) {
        this.id = checkNotNull(id);
    }

    public RuleOrganization getRuleOrganization() {
        return ruleOrganization;
    }

    public void setRuleOrganization(RuleOrganization ruleOrganization) {
        this.ruleOrganization = checkNotNull(ruleOrganization);
    }

    public RuleOrganizationDetail RuleOrganizationDetail() {
        return ruleOrganizationDetail;
    }

    public void RuleOrganizationDetail(RuleOrganizationDetail ruleOrganizationDetail) {
        this.ruleOrganizationDetail = ruleOrganizationDetail;
    }

    public BigDecimal getBigDecimalValue() {
        return new BigDecimal(getName());
    }

    public void setValue(BigDecimal value) {
        setName(checkPositive(value).toPlainString());
    }
}
