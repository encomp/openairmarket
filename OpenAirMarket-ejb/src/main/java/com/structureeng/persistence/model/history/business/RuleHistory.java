// Copyright 2013 Structure Eng Inc.

package com.structureeng.persistence.model.history.business;

import com.structureeng.persistence.model.business.Rule;
import com.structureeng.persistence.model.history.AbstractTenantHistoryModel;

import com.google.common.base.Preconditions;

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
 * Define the revision for the {@code Rule} entities.
 *
 * @author Edgar Rico (edgar.martinez.rico@gmail.com)
 */
@Entity
@Table(name = "ruleHistory", uniqueConstraints = {
        @UniqueConstraint(name = "rulePK", columnNames = {"idRule", "idHistoryTenant"})})
public class RuleHistory extends AbstractTenantHistoryModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "idRuleHistory")
    private Long id;

    @JoinColumn(name = "idRule", referencedColumnName = "idRule", nullable = false)
    @ManyToOne(cascade = CascadeType.REFRESH)
    private Rule rule;

    @JoinColumn(name = "idParentRule", referencedColumnName = "idRule")
    @ManyToOne(cascade = CascadeType.REFRESH)
    private Rule parentRule;

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

    public Rule getRule() {
        return rule;
    }

    public void setRule(Rule rule) {
        this.rule = Preconditions.checkNotNull(rule);
    }

    public Rule getParentRule() {
        return parentRule;
    }

    public void setParentRule(Rule parentRule) {
        this.parentRule = parentRule;
    }

    public Integer getReferenceId() {
        return referenceId;
    }

    public void setReferenceId(Integer referenceId) {
        this.referenceId = Preconditions.checkNotNull(referenceId);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = Preconditions.checkNotNull(name);
    }
}
