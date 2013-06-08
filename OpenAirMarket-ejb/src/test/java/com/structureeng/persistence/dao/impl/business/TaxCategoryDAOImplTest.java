// Copyright 2013 Structure Eng Inc.

package com.structureeng.persistence.dao.impl.business;

import com.structureeng.persistence.dao.CatalogDAO;
import com.structureeng.persistence.dao.TaxCategoryDAO;
import com.structureeng.persistence.dao.impl.AbstractCatalogDAOImplTest;
import com.structureeng.persistence.model.Model;
import com.structureeng.persistence.model.business.TaxCategory;
import com.structureeng.persistence.model.history.business.TaxCategoryHistory;
import com.structureeng.persistence.model.history.business.TaxCategoryHistory_;

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
public class TaxCategoryDAOImplTest extends AbstractCatalogDAOImplTest<Long, String, TaxCategory,
        TaxCategoryHistory> {

    private TaxCategoryDAO taxTypeDAO;

    public TaxCategoryDAOImplTest() {
        super(TaxCategory.class);
    }

    @Override
    public void deleteHistory(Model model) {
        TaxCategory productType = TaxCategory.class.cast(model);
        CriteriaBuilder cb = getEntityManager().getCriteriaBuilder();
        CriteriaQuery<TaxCategoryHistory> cq = cb.createQuery(TaxCategoryHistory.class);
        Root<TaxCategoryHistory> root = cq.from(TaxCategoryHistory.class);
        root.fetch(TaxCategoryHistory_.audit, JoinType.INNER);
        root.fetch(TaxCategoryHistory_.ruleOrganization, JoinType.INNER);
        cq.where(cb.equal(root.get(TaxCategoryHistory_.ruleOrganization), productType));
        List<TaxCategoryHistory> histories = getEntityManager().createQuery(cq).getResultList();
        deleteTenantHistories(productType, histories);
    }

    @Override
    public TaxCategory build(String referenceId, String name) {
        return TaxCategory.newBuilder().setReferenceId(referenceId).setName(name)
                .build();
    }

    @Override
    public String toReferenceId(String referenceId) {
        return referenceId;
    }

    @Override
    public CatalogDAO<TaxCategory, Long, String> getCatalogDAO() {
        if (taxTypeDAO == null) {
            taxTypeDAO = getApplicationContext().getBean(TaxCategoryDAO.class);
        }
        return taxTypeDAO;
    }
}
