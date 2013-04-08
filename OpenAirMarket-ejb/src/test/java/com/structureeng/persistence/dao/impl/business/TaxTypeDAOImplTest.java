// Copyright 2013 Structure Eng Inc.

package com.structureeng.persistence.dao.impl.business;

import com.structureeng.persistence.dao.CatalogDAO;
import com.structureeng.persistence.dao.TaxTypeDAO;
import com.structureeng.persistence.dao.impl.AbstractCatalogDAOImplTest;
import com.structureeng.persistence.model.Model;
import com.structureeng.persistence.model.business.TaxType;
import com.structureeng.persistence.model.history.business.TaxTypeHistory;
import com.structureeng.persistence.model.history.business.TaxTypeHistory_;

import java.util.List;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.JoinType;
import javax.persistence.criteria.Root;

/**
 * Test for {@code TaxTypeDAOImpl}.
 *
 * @author Edgar Rico (edgar.martinez.rico@gmail.com)
 */
public class TaxTypeDAOImplTest extends AbstractCatalogDAOImplTest<Long, Integer, TaxType,
        TaxTypeHistory> {

    private TaxTypeDAO taxTypeDAO;

    public TaxTypeDAOImplTest() {
        super(TaxType.class);
    }

    @Override
    public void deleteHistory(Model model) {
        TaxType productType = TaxType.class.cast(model);
        CriteriaBuilder cb = getEntityManager().getCriteriaBuilder();
        CriteriaQuery<TaxTypeHistory> cq = cb.createQuery(TaxTypeHistory.class);
        Root<TaxTypeHistory> root = cq.from(TaxTypeHistory.class);
        root.fetch(TaxTypeHistory_.audit, JoinType.INNER);
        root.fetch(TaxTypeHistory_.rule, JoinType.INNER);
        cq.where(cb.equal(root.get(TaxTypeHistory_.rule), productType));
        List<TaxTypeHistory> histories = getEntityManager().createQuery(cq).getResultList();
        deleteTenantHistories(productType, histories);
    }

    @Override
    public TaxType build(Integer referenceId, String name) {
        return TaxType.newBuilder().setReferenceId(referenceId).setName(name)
                .setDescription("test").build();
    }

    @Override
    public Integer toReferenceId(String referenceId) {
        return new Integer(referenceId);
    }

    @Override
    public CatalogDAO<TaxType, Long, Integer> getCatalogDAO() {
        if (taxTypeDAO == null) {
            taxTypeDAO = getApplicationContext().getBean(TaxTypeDAO.class);
        }
        return taxTypeDAO;
    }
}
