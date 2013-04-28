// Copyright 2013 Structure Eng Inc.

package com.structureeng.persistence.dao.impl.stock;

import com.structureeng.persistence.dao.CatalogDAO;
import com.structureeng.persistence.dao.WarehouseDAO;
import com.structureeng.persistence.dao.impl.AbstractCatalogDAOImplTest;
import com.structureeng.persistence.model.Model;
import com.structureeng.persistence.model.business.Organization;
import com.structureeng.persistence.model.history.stock.WarehouseHistory;
import com.structureeng.persistence.model.history.stock.WarehouseHistory_;
import com.structureeng.persistence.model.stock.Warehouse;

import java.util.List;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.JoinType;
import javax.persistence.criteria.Root;

/**
 * Test for {@code WarehouseDAOImpl}.
 *
 * @author Edgar Rico (edgar.martinez.rico@gmail.com)
 */
public class WarehouseDAOImplTest extends
        AbstractCatalogDAOImplTest<Long, Integer, Warehouse, WarehouseHistory> {
    
    private WarehouseDAO warehouseDAO;

    public WarehouseDAOImplTest() {
        super(Warehouse.class);
    }

    @Override
    public void deleteHistory(Model model) {
        Warehouse warehouse = Warehouse.class.cast(model);
        CriteriaBuilder cb = getEntityManager().getCriteriaBuilder();
        CriteriaQuery<WarehouseHistory> cq = cb.createQuery(WarehouseHistory.class);
        Root<WarehouseHistory> root = cq.from(WarehouseHistory.class);
        root.fetch(WarehouseHistory_.audit, JoinType.INNER);
        root.fetch(WarehouseHistory_.warehouse, JoinType.INNER);
        cq.where(cb.equal(root.get(WarehouseHistory_.warehouse), warehouse));
        List<WarehouseHistory> histories = getEntityManager().createQuery(cq).getResultList();
        deleteTenantHistories(warehouse, histories);
    }

    @Override
    public Warehouse build(Integer referenceId, String name) {
        Organization organization = new Organization();
        organization.setId(1L);
        return Warehouse.newBuilder().setReferenceId(referenceId).setName(name).setOrganization(organization)
                .build();
    }

    @Override
    public Integer toReferenceId(String referenceId) {
        return Integer.valueOf(referenceId);
    }

    @Override
    public CatalogDAO<Warehouse, Long, Integer> getCatalogDAO() {
        if (warehouseDAO == null) {
            warehouseDAO = getApplicationContext().getBean(WarehouseDAO.class);
        }
        return warehouseDAO;
    }   
}
