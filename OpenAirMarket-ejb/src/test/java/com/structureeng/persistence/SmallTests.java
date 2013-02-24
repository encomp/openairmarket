// Copyright 2013 Structure Eng Inc.

package com.structureeng.persistence;

import com.structureeng.tenancy.context.ThreadLocalTenancyContextHolderTest;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;

/**
 * Specifies the behavior for small tests.
 * 
 * @author Edgar Rico (edgar.martinez.rico@gmail.com)
 */
@RunWith(Suite.class)
@Suite.SuiteClasses({ThreadLocalTenancyContextHolderTest.class})
public class SmallTests {
}
