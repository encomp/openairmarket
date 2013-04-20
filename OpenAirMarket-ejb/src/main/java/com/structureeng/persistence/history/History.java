// Copyright 2013 Structure Eng Inc.

package com.structureeng.persistence.history;

import com.structureeng.persistence.model.security.SystemUser;

import java.util.Date;

/**
 * Specifies the contract for a revision entity.
 *
 * @author Edgar Rico (edgar.martinez.rico@gmail.com)
 */
public interface History {

    public Long getId();

    public void setId(Long id);

    public Date getCreatedDate();

    public void setCreatedDate(Date createdDate);

    public SystemUser getSystemUser();

    public void setSystemUser(SystemUser systemUser);
}
