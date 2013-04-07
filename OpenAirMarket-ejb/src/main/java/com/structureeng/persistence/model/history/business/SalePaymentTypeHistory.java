// Copyright 2013 Structure Eng Inc.

package com.structureeng.persistence.model.history.business;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

/**
 * Define the revision for the {@code SalePaymentType} entities.
 *
 * @author Edgar Rico (edgar.martinez.rico@gmail.com)
 */
@Entity
@DiscriminatorValue("SALE_PAYMENT_TYPE")
public class SalePaymentTypeHistory extends RuleHistory {
}
