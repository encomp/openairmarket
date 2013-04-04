// Copyright 2013 Structure Eng Inc.

package com.structureeng.persistence.dao.impl.product;

import com.structureeng.persistence.dao.CatalogDAO;
import com.structureeng.persistence.dao.DivisionDAO;
import com.structureeng.persistence.dao.impl.AbstractCatalogDAOImplTest;
import com.structureeng.persistence.model.Model;
import com.structureeng.persistence.model.history.product.DivisionHistory;
import com.structureeng.persistence.model.history.product.DivisionHistory_;
import com.structureeng.persistence.model.product.Division;

import java.util.List;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.JoinType;
import javax.persistence.criteria.Root;

/**
 * Test for {@code DivisionDAOImpl}.
 *
 * @author Edgar Rico (edgar.martinez.rico@gmail.com)
 */
public class DivisionDAOImplTest extends AbstractCatalogDAOImplTest<Long, Integer, Division, 
        DivisionHistory> {

    private DivisionDAO divisionDAO;

    public DivisionDAOImplTest() {
        super(Division.class);
    }

    @Override
    public void deleteHistory(Model model) {
        Division division = Division.class.cast(model);
        CriteriaBuilder cb = getEntityManager().getCriteriaBuilder();
        CriteriaQuery<DivisionHistory> cq = cb.createQuery(DivisionHistory.class);
        Root<DivisionHistory> root = cq.from(DivisionHistory.class);
        root.fetch(DivisionHistory_.audit, JoinType.INNER);
        root.fetch(DivisionHistory_.division, JoinType.INNER);
        cq.where(cb.equal(root.get(DivisionHistory_.division), division));
        List<DivisionHistory> histories = getEntityManager().createQuery(cq).getResultList();
        deleteTenantHistories(division, histories);
    }

    @Override
    public Division build(Integer referenceId, String name) {
        return Division.newBuilder().setReferenceId(referenceId).setName(name).build();
    }
    
    @Override
    public Integer toReferenceId(String referenceId) {
        return new Integer(referenceId);
    }

    @Override
    public CatalogDAO<Division, Long, Integer> getCatalogDAO() {
        if (divisionDAO == null) {
            divisionDAO = getApplicationContext().getBean(DivisionDAO.class);
        }
        return divisionDAO;
    }
}
