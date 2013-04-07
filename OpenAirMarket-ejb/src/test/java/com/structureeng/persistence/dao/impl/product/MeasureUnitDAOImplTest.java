// Copyright 2013 Structure Eng Inc.

package com.structureeng.persistence.dao.impl.product;

import com.structureeng.persistence.dao.CatalogDAO;
import com.structureeng.persistence.dao.MeasureUnitDAO;
import com.structureeng.persistence.dao.impl.AbstractCatalogDAOImplTest;
import com.structureeng.persistence.model.Model;
import com.structureeng.persistence.model.history.product.MeasureUnitHistory;
import com.structureeng.persistence.model.history.product.MeasureUnitHistory_;
import com.structureeng.persistence.model.product.MeasureUnit;

import java.util.List;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.JoinType;
import javax.persistence.criteria.Root;

/**
 * Test for {@code PackageDAOImpl}.
 *
 * @author Edgar Rico (edgar.martinez.rico@gmail.com)
 */
public class MeasureUnitDAOImplTest extends AbstractCatalogDAOImplTest<Long, Integer, MeasureUnit, 
        MeasureUnitHistory> {
    
    private MeasureUnitDAO packageDAO;

    public MeasureUnitDAOImplTest() {
        super(MeasureUnit.class);
    }
    
    @Override
    public void deleteHistory(Model model) {
        MeasureUnit measureUnit = MeasureUnit.class.cast(model);
        CriteriaBuilder cb = getEntityManager().getCriteriaBuilder();
        CriteriaQuery<MeasureUnitHistory> cq = cb.createQuery(MeasureUnitHistory.class);
        Root<MeasureUnitHistory> root = cq.from(MeasureUnitHistory.class);
        root.fetch(MeasureUnitHistory_.audit, JoinType.INNER);
        root.fetch(MeasureUnitHistory_.measureUnit, JoinType.INNER);
        cq.where(cb.equal(root.get(MeasureUnitHistory_.measureUnit), measureUnit));
        List<MeasureUnitHistory> histories = getEntityManager().createQuery(cq).getResultList();
        deleteTenantHistories(measureUnit, histories);
    }

    @Override
    public MeasureUnit build(Integer referenceId, String name) {
        return MeasureUnit.newBuilder().setReferenceId(referenceId).setName(name)
                .setCountable(Boolean.TRUE).setExpire(Boolean.FALSE).build();
    }
    
    @Override
    public Integer toReferenceId(String referenceId) {
        return new Integer(referenceId);
    }

    @Override
    public CatalogDAO<MeasureUnit, Long, Integer> getCatalogDAO() {
        if (packageDAO == null) {
            packageDAO = getApplicationContext().getBean(MeasureUnitDAO.class);
        }
        return packageDAO;
    }
}
