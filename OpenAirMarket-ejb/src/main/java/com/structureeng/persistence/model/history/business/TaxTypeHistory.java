// Copyright 2013 Structure Eng Inc.

package com.structureeng.persistence.model.history.business;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

/**
 * Define the revision for the {@code TaxType} entities.
 *
 * @author Edgar Rico (edgar.martinez.rico@gmail.com)
 */
@Entity
@DiscriminatorValue("TAX_TYPE")
public class TaxTypeHistory extends RuleHistory {    
}
