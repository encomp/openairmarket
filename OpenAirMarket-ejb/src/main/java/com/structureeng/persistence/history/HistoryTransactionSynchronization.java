// Copyright 2013 Structure Eng Inc.

package com.structureeng.persistence.history;

import static com.structureeng.persistence.history.HistoryListener.RevisionInfo;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.support.TransactionSynchronization;

import java.util.List;

import javax.inject.Provider;
import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.persistence.PersistenceContext;

/**
 * Transaction synchronization call backs that persist the revision entities once a
 * transaction has been committed.
 *
 * @author Edgar Rico (edgar.martinez.rico@gmail.com)
 */
public class HistoryTransactionSynchronization implements TransactionSynchronization {

    private final Logger logger = LoggerFactory.getLogger(getClass());
    private final Provider<EntityManager> entityManagerProvider;
    
    @PersistenceContext
    private EntityManager entityManager;
    
    public HistoryTransactionSynchronization(Provider<EntityManager> entityManagerProvider) {
        this.entityManagerProvider = entityManagerProvider;
    }
       
    @Override
    public void suspend() {
        logger.debug("suspend is not implemented.");
    }

    @Override
    public void resume() {
        logger.debug("resume is not implemented.");
    }

    @Override
    public void flush() {
        logger.debug("flush is not implemented.");
    }

    @Override
    public void beforeCommit(boolean readOnly) {
        logger.debug("before commit is not implemented.");
        createRevisionsInSameTx();
    }

    @Override
    public void beforeCompletion() {
        logger.debug("before completion is not implemented.");
    }

    @Override
    public void afterCommit() {
        logger.debug("after commit completion is not implemented.");
        createRevisionsInNewTx();
    }

    @Override
    public void afterCompletion(int i) {
        switch (i) {
            case TransactionSynchronization.STATUS_COMMITTED:
                committed();
                return;
            case TransactionSynchronization.STATUS_ROLLED_BACK:
                rolledback();
                return;
            case TransactionSynchronization.STATUS_UNKNOWN:
            default:
                completed();
        }
    }

    private void createRevisionsInSameTx() {
        final RevisionInfo revisionInfo = HistoryListener.removeCurrentRevisionInfo();
        if (revisionInfo != null) {            
            createRevisions(entityManager, revisionInfo.getHistoryEntity());
        }
    }
    
    private void createRevisionsInNewTx() {        
        final RevisionInfo revisionInfo = HistoryListener.removeCurrentRevisionInfo();
        if (revisionInfo != null) {
            final List<HistoryEntity> historyEntities = revisionInfo.getHistoryEntity();
            if (!historyEntities.isEmpty()) {
                EntityManager entityManager = entityManagerProvider.get();
                EntityTransaction tx = entityManager.getTransaction();
                tx.begin();
                createRevisions(entityManager, historyEntities);
                entityManager.flush();
                tx.commit();                
            }
        }
    }
   
    private void createRevisions(final EntityManager entityManager, 
            final List<HistoryEntity> historyEntities) {
        if (!historyEntities.isEmpty()) {
            logger.debug("About to store the revision entities.");
            for (HistoryEntity he : historyEntities) {
                entityManager.persist(he);
            }            
            logger.debug("The revision entities has been persisted.");
        } else {
            logger.warn("The revision entities were empty.");
        }        
    }
    
    private void committed() {
        logger.debug("The revision entities has been persisted and committed succesfully.");
        removeRevisions();
    }

    private void rolledback() {
        logger.debug("The revision entities will not be persisted because the transaction was "
                + "rolledback.");
        removeRevisions();
    }

    private void completed() {
        logger.warn("The revision entities will not be persisted because the transaction "
                + "demarcation was not found.");
        removeRevisions();
    }
    
    private void removeRevisions() {
        HistoryListener.removeCurrentRevisionInfo();
    }
}
