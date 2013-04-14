// Copyright 2013 Structure Eng Inc.

package com.structureeng.persistence.dao.impl.business;

import com.structureeng.persistence.dao.CatalogDAO;
import com.structureeng.persistence.dao.StoreDAO;
import com.structureeng.persistence.dao.impl.AbstractCatalogDAOImplTest;
import com.structureeng.persistence.model.Model;
import com.structureeng.persistence.model.business.Store;
import com.structureeng.persistence.model.history.business.StoreHistory;
import com.structureeng.persistence.model.history.business.StoreHistory_;

import java.util.List;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.JoinType;
import javax.persistence.criteria.Root;

/**
 * Test for {@code StoreDAOImpl}.
 *
 * @author Edgar Rico (edgar.martinez.rico@gmail.com)
 */
public class StoreDAOImplTest extends 
        AbstractCatalogDAOImplTest<Long, Integer, Store, StoreHistory> {

    private StoreDAO storeDAO;

    public StoreDAOImplTest() {
        super(Store.class);
    }

    @Override
    public void deleteHistory(Model model) {
        Store store = Store.class.cast(model);
        CriteriaBuilder cb = getEntityManager().getCriteriaBuilder();
        CriteriaQuery<StoreHistory> cq = cb.createQuery(StoreHistory.class);
        Root<StoreHistory> root = cq.from(StoreHistory.class);
        root.fetch(StoreHistory_.audit, JoinType.INNER);
        root.fetch(StoreHistory_.store, JoinType.INNER);
        cq.where(cb.equal(root.get(StoreHistory_.store), store));
        List<StoreHistory> histories = getEntityManager().createQuery(cq).getResultList();
        deleteTenantHistories(store, histories);
    }

    @Override
    public Store build(Integer referenceId, String name) {
        return Store.newBuilder().setReferenceId(referenceId).setName(name).build();
    }

    @Override
    public Integer toReferenceId(String referenceId) {
        return new Integer(referenceId);
    }

    @Override
    public CatalogDAO<Store, Long, Integer> getCatalogDAO() {
        if (storeDAO == null) {
            storeDAO = getApplicationContext().getBean(StoreDAO.class);
        }
        return storeDAO;
    }
}
