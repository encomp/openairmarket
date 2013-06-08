// Copyright 2013 Structure Eng Inc.

package com.structureeng.persistence.model.history;

import org.eclipse.persistence.annotations.Multitenant;
import org.eclipse.persistence.annotations.MultitenantType;
import org.eclipse.persistence.annotations.TenantDiscriminatorColumn;

import java.io.Serializable;

import javax.persistence.DiscriminatorType;
import javax.persistence.MappedSuperclass;

/**
 * Specifies the behavior of the history of the entities ({@code CatalogModel}) that needs to be
 * {@code Tenant} aware.
 *
 * @author Edgar Rico (edgar.martinez.rico@gmail.com)
 * @param <RID> specifies the {@link Class} of the referenceId for the
 *        {@link javax.persistence.Entity}.
 */
@Multitenant(MultitenantType.SINGLE_TABLE)
@TenantDiscriminatorColumn(name = "idTenant", discriminatorType = DiscriminatorType.INTEGER)
@MappedSuperclass
public abstract class AbstractHistoryCatalogTenantModel<RID extends Serializable>
        extends AbstractHistoryCatalogModel<RID> implements HistoryTenantModel<Long> {
}
