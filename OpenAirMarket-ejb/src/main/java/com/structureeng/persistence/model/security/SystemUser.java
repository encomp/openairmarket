// Copyright 2013 Structure Eng Inc.

package com.structureeng.persistence.model.security;

import com.structureeng.persistence.model.AbstractTenantModel;

import com.google.common.base.Preconditions;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

/**
 * Defines a system user.
 *
 * @author Edgar Rico (edgar.martinez.rico@gmail.com)
 */
@Entity
@Table(name = "systemUser", uniqueConstraints = {
        @UniqueConstraint(name = "systemUserTenantPK", columnNames = {"idTenant", "email"})})
public class SystemUser extends AbstractTenantModel<Long> {

    @Id
    @Column(name = "idSystemUser")
    private Long id;

    @Column(name = "email", length = 500, nullable = false)
    private String email;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = Preconditions.checkNotNull(id);
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = Preconditions.checkNotNull(email);
    }
}
