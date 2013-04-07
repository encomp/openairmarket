// Copyright 2013 Structure Eng Inc.

package com.structureeng.persistence.model.business;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

/**
 * Define the different types of taxes.
 *
 * @author Edgar Rico (edgar.martinez.rico@gmail.com)
 */
@Entity
@DiscriminatorValue("TAX_TYPE")
public class TaxType extends Rule {
}
