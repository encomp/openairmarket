// Copyright 2013 Structure Eng Inc.

package com.structureeng.persistence.model.history;

import com.structureeng.common.DateUtil;
import com.structureeng.persistence.history.History;
import com.structureeng.persistence.model.AbstractModel;
import com.structureeng.persistence.model.security.SystemUser;

import com.google.common.base.Preconditions;

import java.util.Date;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 * Specifies the revision for an {@link javax.persistence.Entity} that is required to keep tenancy.
 *
 * @author Edgar Rico (edgar.martinez.rico@gmail.com)
 */
@Entity
@Table(name = "audit")
public class Audit extends AbstractModel<Long> implements History {

    @Id
    @Column(name = "idAudit")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "createDate", nullable = false)
    private Date createdDate;

    @JoinColumn(name = "idSystemUser", referencedColumnName = "idSystemUser")
    @ManyToOne(cascade = CascadeType.REFRESH)
    private SystemUser systemUser;

    @Override
    public Long getId() {
        return id;
    }

    @Override
    public void setId(Long id) {
        this.id = Preconditions.checkNotNull(id);
    }

    @Override
    public Date getCreatedDate() {
        return DateUtil.clone(createdDate);
    }

    @Override
    public void setCreatedDate(Date createdDate) {
        this.createdDate = DateUtil.clone((Date) Preconditions.checkNotNull(createdDate.clone()));
    }

    @Override
    public SystemUser getSystemUser() {
        return systemUser;
    }

    @Override
    public void setSystemUser(SystemUser systemUser) {
        this.systemUser = Preconditions.checkNotNull(systemUser);
    }
}
