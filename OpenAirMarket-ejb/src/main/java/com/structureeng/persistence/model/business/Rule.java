// Copyright 2013 Structure Eng Inc.

package com.structureeng.persistence.model.business;

import com.structureeng.persistence.model.AbstractTenantModel;

import com.google.common.base.Preconditions;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.DiscriminatorColumn;
import javax.persistence.DiscriminatorType;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

/**
 * Defines the different types of business rules.
 *
 * @author Edgar Rico (edgar.martinez.rico@gmail.com)
 */
@Entity
@Table(name = "rule", uniqueConstraints = {
        @UniqueConstraint(name = "ruleTenantPK", columnNames = {"idTenant", "idReference"}),
        @UniqueConstraint(name = "ruleUK", columnNames = {"idTenant", "code"})})
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "ruleType", discriminatorType = DiscriminatorType.STRING, length = 50)
public abstract class Rule extends AbstractTenantModel<Long> {

    @Id
    @Column(name = "idRule")
    private Long id;

    @JoinColumn(name = "idParentRule", referencedColumnName = "idRule")
    @ManyToOne(cascade = CascadeType.REFRESH)
    private Rule parentRule;

    @Column(name = "idReference", nullable = false)
    private Integer referenceId;

    @Column(name = "code", nullable = false)
    private String code;

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

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = Preconditions.checkNotNull(code);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = Preconditions.checkNotNull(name);
    }
}
