// Copyright 2013 Structure Eng Inc.

package com.structureeng.persistence.dao.impl.product;

import com.structureeng.persistence.dao.CatalogDAO;
import com.structureeng.persistence.dao.PackageDAO;
import com.structureeng.persistence.dao.impl.AbstractCatalogDAOImplTest;
import com.structureeng.persistence.model.Model;
import com.structureeng.persistence.model.history.product.PackageHistory;
import com.structureeng.persistence.model.history.product.PackageHistory_;
import com.structureeng.persistence.model.product.Package;

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
public class PackageDAOImplTest extends AbstractCatalogDAOImplTest<Long, Integer, Package, 
        PackageHistory> {
    
    private PackageDAO packageDAO;

    public PackageDAOImplTest() {
        super(Package.class);
    }
    
    @Override
    public void deleteHistory(Model model) {
        Package paquete = Package.class.cast(model);
        CriteriaBuilder cb = getEntityManager().getCriteriaBuilder();
        CriteriaQuery<PackageHistory> cq = cb.createQuery(PackageHistory.class);
        Root<PackageHistory> root = cq.from(PackageHistory.class);
        root.fetch(PackageHistory_.historyTenant, JoinType.INNER);
        root.fetch(PackageHistory_.aPackage, JoinType.INNER);
        cq.where(cb.equal(root.get(PackageHistory_.aPackage), paquete));
        List<PackageHistory> histories = getEntityManager().createQuery(cq).getResultList();
        deleteTenantHistories(paquete, histories);
    }

    @Override
    public Package build(Integer referenceId, String name) {
        return Package.newBuilder().setReferenceId(referenceId).setName(name).build();
    }
    
    @Override
    public Integer toReferenceId(String referenceId) {
        return new Integer(referenceId);
    }

    @Override
    public CatalogDAO<Package, Long, Integer> getCatalogDAO() {
        if (packageDAO == null) {
            packageDAO = getApplicationContext().getBean(PackageDAO.class);
        }
        return packageDAO;
    }
}
