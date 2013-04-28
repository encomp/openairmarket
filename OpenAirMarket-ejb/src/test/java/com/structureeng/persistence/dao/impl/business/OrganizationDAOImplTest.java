// Copyright 2013 Structure Eng Inc.

package com.structureeng.persistence.dao.impl.business;

import com.structureeng.persistence.dao.CatalogDAO;
import com.structureeng.persistence.dao.OrganizationDAO;
import com.structureeng.persistence.dao.impl.AbstractCatalogDAOImplTest;
import com.structureeng.persistence.model.Model;
import com.structureeng.persistence.model.business.Organization;
import com.structureeng.persistence.model.history.business.OrganizationHistory;
import com.structureeng.persistence.model.history.business.OrganizationHistory_;

import java.util.List;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.JoinType;
import javax.persistence.criteria.Root;

/**
 * Test for {@code OrganizationDAOImpl}.
 *
 * @author Edgar Rico (edgar.martinez.rico@gmail.com)
 */
public class OrganizationDAOImplTest extends
        AbstractCatalogDAOImplTest<Long, Integer, Organization, OrganizationHistory> {

    private OrganizationDAO organizationDAO;

    public OrganizationDAOImplTest() {
        super(Organization.class);
    }

    @Override
    public void deleteHistory(Model model) {
        Organization organization = Organization.class.cast(model);
        CriteriaBuilder cb = getEntityManager().getCriteriaBuilder();
        CriteriaQuery<OrganizationHistory> cq = cb.createQuery(OrganizationHistory.class);
        Root<OrganizationHistory> root = cq.from(OrganizationHistory.class);
        root.fetch(OrganizationHistory_.audit, JoinType.INNER);
        root.fetch(OrganizationHistory_.organization, JoinType.INNER);
        cq.where(cb.equal(root.get(OrganizationHistory_.organization), organization));
        List<OrganizationHistory> histories = getEntityManager().createQuery(cq).getResultList();
        deleteTenantHistories(organization, histories);
    }

    @Override
    public Organization build(Integer referenceId, String name) {
        return Organization.newBuilder().setReferenceId(referenceId).setName(name).build();
    }

    @Override
    public Integer toReferenceId(String referenceId) {
        return new Integer(referenceId);
    }

    @Override
    public CatalogDAO<Organization, Long, Integer> getCatalogDAO() {
        if (organizationDAO == null) {
            organizationDAO = getApplicationContext().getBean(OrganizationDAO.class);
        }
        return organizationDAO;
    }
}
