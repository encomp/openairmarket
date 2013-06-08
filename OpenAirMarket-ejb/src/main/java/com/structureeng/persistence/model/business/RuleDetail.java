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
@Table(name = "ruleDetail", uniqueConstraints = {
        @UniqueConstraint(name = "ruleDetailTenantPK",
                columnNames = {"idTenant", "idRule", "idReference"})})
public class RuleDetail extends AbstractCatalogTenantModel<Long, String> {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "idRuleDetail")
    private Long id;

    @JoinColumn(name = "idRule", referencedColumnName = "idRule", nullable = false)
    @ManyToOne(fetch = FetchType.LAZY)
    private Rule rule;

    @JoinColumn(name = "idParentRuleDetail", referencedColumnName = "idRuleDetail")
    @ManyToOne(fetch = FetchType.LAZY)
    private RuleDetail ruleDetail;

    @Override
    public Long getId() {
        return id;
    }

    @Override
    public void setId(Long id) {
        this.id = checkNotNull(id);
    }

    public Rule getRule() {
        return rule;
    }

    public void setRule(Rule rule) {
        this.rule = checkNotNull(rule);
    }

    public RuleDetail getRuleDetail() {
        return ruleDetail;
    }

    public void setRuleDetail(RuleDetail ruleDetail) {
        this.ruleDetail = ruleDetail;
    }

    public BigDecimal getBigDecimalValue() {
        return new BigDecimal(getName());
    }

    public void setValue(BigDecimal value) {
        setName(checkPositive(value).toPlainString());
    }
}
