// Copyright 2013 Structure Eng Inc.

package com.structureeng.persistence.model;//Structure Eng 2013 Copyright

import com.structureeng.persistence.history.HistoryListener;
import com.structureeng.persistence.inject.PersistenceTestModule;

import org.junit.Before;
import org.junit.runner.RunWith;
import org.springframework.context.ApplicationContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.AnnotationConfigContextLoader;

import javax.inject.Inject;

/**
 * Specifies the behavior of Persistence Tests.
 *
 * @author Edgar Rico (edgar.martinez.rico@gmail.com)
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {PersistenceTestModule.class},
        loader = AnnotationConfigContextLoader.class)
public abstract class AbstractPersistenceTest {
    
    @Inject
    private ApplicationContext applicationContext;
    
    @Before
    public void setup() {        
        HistoryListener.setApplicationContext(applicationContext);        
    }
}
