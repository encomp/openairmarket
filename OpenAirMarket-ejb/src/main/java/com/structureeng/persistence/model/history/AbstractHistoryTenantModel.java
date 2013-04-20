// Copyright 2013 Structure Eng Inc.

package com.structureeng.persistence.model.history;

import org.eclipse.persistence.annotations.Multitenant;
import org.eclipse.persistence.annotations.MultitenantType;
import org.eclipse.persistence.annotations.TenantDiscriminatorColumn;

import javax.persistence.DiscriminatorType;
import javax.persistence.MappedSuperclass;

/**
 * Specifies the behavior of the history of the entities ({@code HistoryTenant}) that
 * are required to keep tenancy.
 *
 * @author Edgar Rico (edgar.martinez.rico@gmail.com)
 */
@Multitenant(MultitenantType.SINGLE_TABLE)
@TenantDiscriminatorColumn(name = "idTenant", discriminatorType = DiscriminatorType.INTEGER)
@MappedSuperclass
public abstract class AbstractHistoryTenantModel extends AbstractHistoryModel {

}
