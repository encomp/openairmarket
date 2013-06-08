// Copyright 2013 Structure Eng Inc.

package com.structureeng.persistence.model.history.business;

import com.structureeng.persistence.model.business.Rule;
import com.structureeng.persistence.model.history.AbstractHistoryCatalogTenantModel;
import com.structureeng.persistence.model.history.AbstractHistoryTenantModel;

import com.google.common.base.Preconditions;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.DiscriminatorColumn;
import javax.persistence.DiscriminatorType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
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
        @UniqueConstraint(name = "rulePK", columnNames = {"idRule", "idAudit"})})
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "ruleType", discriminatorType = DiscriminatorType.STRING, length = 50)
public abstract class RuleHistory extends AbstractHistoryCatalogTenantModel<Integer> {

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

    @Column(name = "description", nullable = false)
    private String description;

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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = checkNotEmpty(description);
    }
}
