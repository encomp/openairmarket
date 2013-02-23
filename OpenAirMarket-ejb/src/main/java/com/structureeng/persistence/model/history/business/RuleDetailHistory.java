// Copyright 2013 Structure Eng Inc.

package com.structureeng.persistence.model.history.business;

import com.structureeng.persistence.model.business.Rule;
import com.structureeng.persistence.model.business.RuleDetail;
import com.structureeng.persistence.model.history.AbstractTenantHistoryModel;

import com.google.common.base.Preconditions;

import java.math.BigDecimal;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

/**
 * Define the revision for the {@code RuleDetail} entities.
 *
 * @author Edgar Rico (edgar.martinez.rico@gmail.com)
 */
@Entity
@Table(name = "ruleDetailHistory", uniqueConstraints = {
        @UniqueConstraint(name = "rulePK", columnNames = {"idRuleDetail", "idHistoryTenant"})})
public class RuleDetailHistory extends AbstractTenantHistoryModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "idRuleDetailHistory")
    private Long id;

    @JoinColumn(name = "idRuleDetail", referencedColumnName = "idRuleDetail", nullable = false)
    @ManyToOne(cascade = CascadeType.REFRESH)
    private RuleDetail ruleDetail;

    @JoinColumn(name = "idRule", referencedColumnName = "idRule", nullable = false)
    @ManyToOne(cascade = CascadeType.REFRESH)
    private Rule rule;

    @JoinColumn(name = "idParentRuleDetail", referencedColumnName = "idRuleDetail",
            nullable = false)
    @ManyToOne(cascade = CascadeType.REFRESH)
    private RuleDetail ruleDetailParent;

    @Column(name = "code", nullable = false)
    private String code;

    @Column(name = "value", nullable = false)
    private String value;

    @Override
    public Long getId() {
        return id;
    }

    @Override
    public void setId(Long id) {
        this.id = Preconditions.checkNotNull(id);
    }

    public RuleDetail getRuleDetail() {
        return ruleDetail;
    }

    public void setRuleDetail(RuleDetail ruleDetail) {
        this.ruleDetail = Preconditions.checkNotNull(ruleDetail);
    }

    public Rule getRule() {
        return rule;
    }

    public void setRule(Rule rule) {
        this.rule = Preconditions.checkNotNull(rule);
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = Preconditions.checkNotNull(code);
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = Preconditions.checkNotNull(value);
    }

    public BigDecimal getBigDecimalValue() {
        return new BigDecimal(value);
    }

    public void setValue(BigDecimal value) {
        this.value = Preconditions.checkNotNull(value).toPlainString();
    }

    public RuleDetail getRuleDetailParent() {
        return ruleDetailParent;
    }

    public void setRuleDetailParent(RuleDetail ruleDetailParent) {
        this.ruleDetailParent = ruleDetailParent;
    }
}
