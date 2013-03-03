// Copyright 2013 Structure Eng Inc.

package com.structureeng.persistence.history;

import com.structureeng.persistence.model.AbstractModel;

import com.google.common.base.Preconditions;
import com.google.common.cache.Cache;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.transaction.support.TransactionSynchronizationManager;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.GregorianCalendar;

import javax.persistence.PostPersist;
import javax.persistence.PostRemove;
import javax.persistence.PostUpdate;

/**
 * Creates a revision for an entity as a result of events that occurs inside the
 * persistence mechanism.
 *
 * @author Edgar Rico (edgar.martinez.rico@gmail.com)
 */
public class HistoryListener {

    private final Logger logger = LoggerFactory.getLogger(getClass());
    private static Cache<Long, RevisionInfo> revisionHolder;
    private static ApplicationContext applicationContext;

    /**
     * Creates a revision entity with the {@code HistoryType.CREATE}.
     *
     * @param entity - the instance that will be used to create the revision.
     */
    @PostPersist
    public void postPersist(AbstractModel entity) {
        createHistoryEntity(entity, HistoryType.CREATE);
    }

    /**
     * Creates a revision entity with the {@code HistoryType.UPDATE}.
     *
     * @param entity - the instance that will be used to create the revision.
     */
    @PostUpdate
    public void postUpdate(AbstractModel entity) {
        createHistoryEntity(entity, HistoryType.UPDATE);
    }

    /**
     * Creates a revision entity with the {@code HistoryType.DELETE}.
     *
     * @param entity - the instance that will be used to create the revision.
     */
    @PostRemove
    public void postRemove(AbstractModel entity) {
        createHistoryEntity(entity, HistoryType.DELETE);
    }

    private void createHistoryEntity(AbstractModel entity, HistoryType historyType) {
        HistoryEntityBuilder builder = getHistoryEntityBuilder(entity);
        RevisionInfo revisionInfo = getRevisionInfo();
        History history = getHistory(builder, historyType, revisionInfo);
        HistoryEntity<History> historyEntity = (HistoryEntity<History>) (builder.build(entity));
        historyEntity.setHistoryType(historyType);
        historyEntity.setHistory(history);
        revisionInfo.add(historyEntity);
    }

    private HistoryEntityBuilder getHistoryEntityBuilder(AbstractModel entity) {
        try {
            Revision historyAnnotation = entity.getClass().getAnnotation(Revision.class);
            Class historyBuilderClass = historyAnnotation.builder();
            return (HistoryEntityBuilder) historyBuilderClass.newInstance();
        } catch (InstantiationException ex) {
            throw new IllegalStateException(ex);
        } catch (IllegalAccessException ex) {
            throw new IllegalStateException(ex);
        }
    }

    private RevisionInfo getRevisionInfo() {
        RevisionInfo revisionInfo = getCurrentRevisionInfo();
        if (revisionInfo == null) {
            revisionInfo = new RevisionInfo();
            HistoryTransactionSynchronization transactionSynchronization = applicationContext
                    .getBean(HistoryTransactionSynchronization.class);
            TransactionSynchronizationManager.registerSynchronization(transactionSynchronization);
            revisionHolder.put(Thread.currentThread().getId(), revisionInfo);
        }
        return revisionInfo;
    }

    private History getHistory(HistoryEntityBuilder builder, HistoryType historyType,
            RevisionInfo revisionInfo) {
        ParameterizedType parameterizedType = (ParameterizedType) builder.getClass()
                .getGenericSuperclass();
        Class clase = (Class) parameterizedType.getActualTypeArguments()[1];
        clase = findHistoryEntityInterface(clase);
        History history = revisionInfo.getHistory(clase, historyType);
        if (history == null) {
            history = createHistory(clase);
            revisionInfo.setHistory(clase, historyType, history);
            return history;
        } else {
            return history;
        }
    }

    private Class findHistoryEntityInterface(Class clase) {
        Type[] types = clase.getGenericInterfaces();
        if (types != null && types.length > 0 && types[0] instanceof ParameterizedType) {
            for (Type type : types) {
                if (type instanceof ParameterizedType) {
                    ParameterizedType parameterizedType = (ParameterizedType) type;
                    if (parameterizedType.getRawType() instanceof Class
                            && HistoryEntity.class.equals((Class) parameterizedType.getRawType())) {
                        return (Class) parameterizedType.getActualTypeArguments()[0];
                    }
                }
            }
        }
        if (!Object.class.equals(clase.getClass())) {
            return findHistoryEntityInterface(clase.getSuperclass());
        } else {
            return null;
        }
    }

    private History createHistory(Class clase) {
        try {
            History history = (History) clase.newInstance();
            history.setCreatedDate(new GregorianCalendar().getTime());
            return history;
        } catch (InstantiationException e) {
            logger.error(e.getMessage(), e);
        } catch (IllegalAccessException e) {
            logger.error(e.getMessage(), e);
        }
        return null;
    }

    /**
     * Returns the {@code Cache} that is being used to keep track of the revisions.
     *
     * @return the cache that is being used.
     */
    public static Cache<Long, RevisionInfo> getRevisionHolder() {
        return revisionHolder;
    }

    /**
     * Specifies the {@code Cache} that will be used to keep track of the revisions.
     *
     * @param revisionCache the cache that will be used.
     */
    public static void setRevisionHolder(Cache<Long, RevisionInfo> revisionCache) {
        HistoryListener.revisionHolder = Preconditions.checkNotNull(revisionCache);
    }

    /**
     * Provides the current {@code RevisionInfo} of a particular thread.
     *
     * @return the associate instance of a {@code Thread}
     */
    public static RevisionInfo getCurrentRevisionInfo() {
        Thread thread = Thread.currentThread();
        return revisionHolder.asMap().get(thread.getId());
    }

    /**
     * Removes the current {@code RevisionInfo} of a particular thread.
     *
     * @return the associate instance of a {@code Thread}
     */
    public static RevisionInfo removeCurrentRevisionInfo() {
        Thread thread = Thread.currentThread();
        return revisionHolder.asMap().remove(thread.getId());
    }

    /**
     * Returns the central interface to provide configuration of the application.
     *
     * @return the instance of the Bean factory that is used to create
     *          {@code HistoryTransactionSynchronization}.
     */
    public static ApplicationContext getApplicationContext() {
        return applicationContext;
    }

    /**
     * Specifies the central interface to create instances of
     * {@code HistoryTransactionSynchronization}.
     *
     * @param applicationContext the instance that will be use to create instances of
     *          {@code HistoryTransactionSynchronization}.
     */
    public static void setApplicationContext(ApplicationContext applicationContext) {
        HistoryListener.applicationContext = Preconditions.checkNotNull(applicationContext);
    }
}
