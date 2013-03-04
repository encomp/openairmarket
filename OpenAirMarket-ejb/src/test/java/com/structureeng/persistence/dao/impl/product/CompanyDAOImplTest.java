// Copyright 2013 Structure Eng Inc.

package com.structureeng.persistence.dao.impl.product;

import com.structureeng.persistence.dao.CompanyDAO;
import com.structureeng.persistence.dao.impl.AbstractCatalogDAOImplTest;
import com.structureeng.persistence.model.Model;
import com.structureeng.persistence.model.history.AbstractTenantHistoryModel;
import com.structureeng.persistence.model.history.product.CompanyHistory;
import com.structureeng.persistence.model.history.product.CompanyHistory_;
import com.structureeng.persistence.model.product.Company;

import com.google.common.collect.Lists;

import java.util.List;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.JoinType;
import javax.persistence.criteria.Root;

/**
 * Test for {@code CompanyDAOImpl}.
 *
 * @author Edgar Rico (edgar.martinez.rico@gmail.com)
 */
public class CompanyDAOImplTest extends AbstractCatalogDAOImplTest<Long, Company> {

    private CompanyDAO companyDAO;
    
    public CompanyDAOImplTest() {
        super(Company.class);
    }
    
    @Override
    public void deleteHistory(Model model) {
        Company company = Company.class.cast(model);
        CriteriaBuilder cb = getEntityManager().getCriteriaBuilder();
        CriteriaQuery<CompanyHistory> cq = cb.createQuery(CompanyHistory.class);
        Root<CompanyHistory> root = cq.from(CompanyHistory.class);
        root.fetch(CompanyHistory_.historyTenant, JoinType.INNER);
        root.fetch(CompanyHistory_.company, JoinType.INNER);
        cq.where(cb.equal(root.get(CompanyHistory_.company), company));
        List<CompanyHistory> histories = getEntityManager().createQuery(cq).getResultList();
        List<AbstractTenantHistoryModel> test = Lists.newArrayList();
        test.addAll(histories);
        deleteTenantHistories(company, test);
    }

    @Override
    public Company build(Integer referenceId, String name) {
        return Company.newBuilder().setReferenceId(referenceId).setName(name).build();
    }

    @Override
    public CompanyDAO getCatalogDAO() {
        if (companyDAO == null) {
            companyDAO = getApplicationContext().getBean(CompanyDAO.class);
        }
        return companyDAO;
    }    
}
