// Copyright 2013 Structure Eng Inc.

package com.structureeng.persistence;

import com.structureeng.persistence.history.NonTenantHistoryListenerTest;
import com.structureeng.persistence.model.history.tenant.TenantHistory;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;

/**
 * Specifies the behavior for the tests that require interaction with the database.
 *  
 * @author Edgar Rico (edgar.martinez.rico@gmail.com)
 */
@RunWith(Suite.class)
@Suite.SuiteClasses({NonTenantHistoryListenerTest.class, TenantHistory.class})
public class PersistenceTests {
}
