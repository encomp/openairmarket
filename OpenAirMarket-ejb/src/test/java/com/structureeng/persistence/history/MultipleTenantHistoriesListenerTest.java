//Structure Eng 2013 Copyright

package com.structureeng.persistence.history;

import com.structureeng.persistence.model.AbstractPersistenceTest;
import com.structureeng.persistence.model.history.Audit;
import com.structureeng.persistence.model.history.product.ProductManufacturerHistory;
import com.structureeng.persistence.model.history.product.ProductCategoryHistory;
import com.structureeng.persistence.model.history.product.ProductCategoryHistory_;
import com.structureeng.persistence.model.history.product.ProductManufacturerHistory_;
import com.structureeng.persistence.model.product.ProductManufacturer;
import com.structureeng.persistence.model.product.ProductCategory;
import com.structureeng.persistence.model.tenant.Tenant;
import com.structureeng.tenancy.context.TenancyContext;
import com.structureeng.tenancy.context.TenancyContextHolder;

import org.junit.Assert;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;
import org.springframework.context.ApplicationContext;
import org.springframework.test.context.transaction.TransactionConfiguration;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.JoinType;
import javax.persistence.criteria.Root;

/**
 * Tests multiple tenant entities being changed within the same transaction for 
 * {@code HistoryListener}.
 *
 * @author Edgar Rico (edgar.martinez.rico@gmail.com)
 */
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
@TransactionConfiguration(transactionManager = "transactionManager", defaultRollback = false)
public class MultipleTenantHistoriesListenerTest extends AbstractPersistenceTest {
    
    @PersistenceContext
    private EntityManager entityManager;

    @Inject
    private ApplicationContext applicationContext;

    private PlatformTransactionManager tx;
    
    @Test
    public void testPostPersist() {
        registerTenancyContext(createTenant(1));
        tx = applicationContext.getBean(PlatformTransactionManager.class);
        TransactionStatus transactionStatus = tx.getTransaction(null);
        ProductManufacturer company = createProductManufacturer(99, "testCompany 99");
        ProductCategory division = createProductCategory(99, "testDivision 99");
        entityManager.persist(company);
        entityManager.persist(division);
        tx.commit(transactionStatus);
    }

    @Test
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void testPostPersistValidation() {
        ProductManufacturerHistory companyHistory = 
                retrieveProductManufacturerHistory(99, JoinType.INNER);
        ProductCategoryHistory divisionHistory = retrieveProductCategoryHistory(99, JoinType.INNER);
        ProductManufacturer company = companyHistory.getProductManufacturer();
        ProductCategory division = divisionHistory.getProductCategory();
        Audit historyTenant = companyHistory.getHistory();
        assertProductManufacturerHistory(HistoryType.CREATE, companyHistory, historyTenant, company);
        assertProductCategoryHistory(HistoryType.CREATE, divisionHistory, historyTenant, division);
        deleteProductManufacturerHistory(company, companyHistory.getId());
        deleteProductCategoryHistory(division, divisionHistory.getId());
        deleteTenantHistory(historyTenant.getId());
    }
            
    private void assertProductManufacturerHistory(HistoryType historyType, 
            ProductManufacturerHistory pmHistory, Audit historyTenant, ProductManufacturer pm) {
        Assert.assertEquals(historyType, pmHistory.getHistoryType());
        Assert.assertEquals(pm.getReferenceId(), pmHistory.getReferenceId());
        Assert.assertEquals(pm.getName(), pmHistory.getName());
        Assert.assertEquals(pm.getVersion(), pmHistory.getVersion());
        Assert.assertNotNull(historyTenant);
        Assert.assertNotNull(historyTenant.getId());
        Assert.assertNotNull(historyTenant.getCreatedDate());
    }
    
    private void assertProductCategoryHistory(HistoryType historyType, 
            ProductCategoryHistory pcHistory, Audit historyTenant, ProductCategory pc) {
        Assert.assertEquals(historyType, pcHistory.getHistoryType());
        Assert.assertEquals(pc.getReferenceId(), pcHistory.getReferenceId());
        Assert.assertEquals(pc.getName(), pcHistory.getName());
        Assert.assertEquals(pc.getVersion(), pcHistory.getVersion());
        Assert.assertNotNull(historyTenant);
        Assert.assertNotNull(historyTenant.getId());
        Assert.assertNotNull(historyTenant.getCreatedDate());
    }
            
    private ProductManufacturerHistory retrieveProductManufacturerHistory(Integer referenceId, 
            JoinType tenatJoinType) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<ProductManufacturerHistory> cq = entityManager.getCriteriaBuilder()
                .createQuery(ProductManufacturerHistory.class);
        Root<ProductManufacturerHistory> root = cq.from(ProductManufacturerHistory.class);
        root.fetch(ProductManufacturerHistory_.audit, JoinType.INNER);
        root.fetch(ProductManufacturerHistory_.productManufacturer, tenatJoinType);
        cq.where(cb.equal(root.get(ProductManufacturerHistory_.referenceId), referenceId));
        return entityManager.createQuery(cq).getSingleResult();
    }
    
    private ProductCategoryHistory retrieveProductCategoryHistory(Integer referenceId, 
            JoinType tenatJoinType) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<ProductCategoryHistory> cq = entityManager.getCriteriaBuilder()
                .createQuery(ProductCategoryHistory.class);
        Root<ProductCategoryHistory> root = cq.from(ProductCategoryHistory.class);
        root.fetch(ProductCategoryHistory_.audit, JoinType.INNER);
        root.fetch(ProductCategoryHistory_.productCategory, tenatJoinType);
        cq.where(cb.equal(root.get(ProductCategoryHistory_.referenceId), referenceId));
        return entityManager.createQuery(cq).getSingleResult();
    }
    
    private void deleteProductCategoryHistory(ProductCategory division, Long... divisionHistory) {
        Query q = null;
        for (Long id : divisionHistory) {
            q = entityManager.createQuery("DELETE FROM ProductCategoryHistory c WHERE c.id = ?1");
            q.setParameter(1, id);
            Assert.assertEquals(1, q.executeUpdate());
        }        
        q = entityManager.createQuery("DELETE FROM ProductCategory c WHERE c.id = ?1");
        q.setParameter(1, division.getId());
        Assert.assertEquals(1, q.executeUpdate());
    }
    
    private void deleteProductManufacturerHistory(ProductManufacturer company, 
            Long... companyHistory) {
        Query q = null;
        for (Long id : companyHistory) {
            q = entityManager
                    .createQuery("DELETE FROM ProductManufacturerHistory c WHERE c.id = ?1");
            q.setParameter(1, id);
            Assert.assertEquals(1, q.executeUpdate());
        }        
        q = entityManager.createQuery("DELETE FROM ProductManufacturer c WHERE c.id = ?1");
        q.setParameter(1, company.getId());
        Assert.assertEquals(1, q.executeUpdate());
    }
    
    private void deleteTenantHistory(Long... historyTenant) {         
        for (Long id : historyTenant) {
            Query q = entityManager.createQuery("DELETE FROM Audit h WHERE h.id = ?1");
            q.setParameter(1, id);
            Assert.assertEquals(1, q.executeUpdate());
        }
    }
    
    private ProductCategory createProductCategory(int referenceId, String name) {
        return ProductCategory.newBuilder().setReferenceId(referenceId).setName(name).build();
    }
    
    private ProductManufacturer createProductManufacturer(int referenceId, String name) {
        return ProductManufacturer.newBuilder().setReferenceId(referenceId).setName(name).build();
    }

    private Tenant createTenant(int id) {
        Tenant tenant = new Tenant();
        tenant.setId(id);
        return tenant;
    }

    private void registerTenancyContext(Tenant tenant) {
        TenancyContextHolder.registerTenancyContext(new TenancyContext(tenant));
    }
}
