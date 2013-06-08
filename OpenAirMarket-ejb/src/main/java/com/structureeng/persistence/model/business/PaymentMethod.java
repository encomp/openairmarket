// Copyright 2013 Structure Eng Inc.

package com.structureeng.persistence.model.business;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

/**
 * Define the different types of payment methods. This drives the business rules for a particular
 * payment method.
 *
 * @author Edgar Rico (edgar.martinez.rico@gmail.com)
 */
@Entity
@DiscriminatorValue("PAYMENT_METHOD")
public class PaymentMethod extends RuleOrganization {    
}
