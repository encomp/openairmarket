// Copyright 2013 Structure Eng Inc.

package com.structureeng.persistence.model.history.security;

import com.structureeng.persistence.model.history.AbstractHistoryModel;
import com.structureeng.persistence.model.security.SystemUser;

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
 * Define the revision for the {@code SystemUser} entities.
 *
 * @author Edgar Rico (edgar.martinez.rico@gmail.com)
 */

@Entity
@Table(name = "systemUserHistory", uniqueConstraints = {
        @UniqueConstraint(name = "systemUserHistoryUK",
                columnNames = {"idSystemUser", "idAudit"})})
public class SystemUserHistory extends AbstractHistoryModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "idSystemUserHistory")
    private Long id;

    @JoinColumn(name = "idSystemUser", referencedColumnName = "idSystemUser", nullable = false)
    @ManyToOne(cascade = CascadeType.REFRESH)
    private SystemUser systemUser;

    @Column(name = "email", length = 500, nullable = false)
    private String email;

    @Override
    public Long getId() {
        return id;
    }

    @Override
    public void setId(Long id) {
        this.id = Preconditions.checkNotNull(id);
    }

    public SystemUser getSystemUser() {
        return systemUser;
    }

    public void setSystemUser(SystemUser systemUser) {
        this.systemUser = Preconditions.checkNotNull(systemUser);
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = Preconditions.checkNotNull(email);
    }
}
