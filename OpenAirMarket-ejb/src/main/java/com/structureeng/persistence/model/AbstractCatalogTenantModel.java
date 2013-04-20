// Copyright 2013 Structure Eng Inc.

package com.structureeng.persistence.model;

import org.eclipse.persistence.annotations.Multitenant;
import org.eclipse.persistence.annotations.MultitenantType;
import org.eclipse.persistence.annotations.TenantDiscriminatorColumn;
import java.io.Serializable;

import javax.persistence.DiscriminatorType;
import javax.persistence.MappedSuperclass;

/**
 * Specifies the behavior of all {@code Tenant} the entities.
 *
 * @author Edgar Rico (edgar.martinez.rico@gmail.com)
 * @param <T> specifies the {@link Class} of the id for the {@link javax.persistence.Entity}.
 * @param <RID> specifies the {@link Class} of the referenceId for the
 *        {@link javax.persistence.Entity}.
 */
@Multitenant(MultitenantType.SINGLE_TABLE)
@TenantDiscriminatorColumn(name = "idTenant", discriminatorType = DiscriminatorType.INTEGER)
@MappedSuperclass
public abstract class AbstractCatalogTenantModel <T extends Serializable, RID extends Serializable>
    extends AbstractCatalogModel<T, RID> implements TenantModel<T> {
}
