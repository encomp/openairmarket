// Copyright 2013 Structure Eng Inc.

package com.structureeng.persistence.dao.inject;

import com.structureeng.persistence.dao.ProductManufacturerDAO;
import com.structureeng.persistence.dao.ProductCategoryDAO;
import com.structureeng.persistence.dao.ProductMeasureUnitDAO;
import com.structureeng.persistence.dao.ProductDAO;
import com.structureeng.persistence.dao.ProductDefinitionDAO;
import com.structureeng.persistence.dao.ProductTypeDAO;
import com.structureeng.persistence.dao.PurchasePriceDAO;
import com.structureeng.persistence.dao.SalePriceDAO;
import com.structureeng.persistence.dao.StockDAO;
import com.structureeng.persistence.dao.StoreDAO;
import com.structureeng.persistence.dao.TaxTypeDAO;
import com.structureeng.persistence.dao.TenantDAO;
import com.structureeng.persistence.dao.WarehouseDAO;
import com.structureeng.persistence.dao.impl.business.ProductTypeDAOImpl;
import com.structureeng.persistence.dao.impl.business.StoreDAOImpl;
import com.structureeng.persistence.dao.impl.business.TaxTypeDAOImpl;
import com.structureeng.persistence.dao.impl.product.ProductManufacturerDAOImpl;
import com.structureeng.persistence.dao.impl.product.ProductCategoryDAOImpl;
import com.structureeng.persistence.dao.impl.product.ProductMeasureUnitDAOImpl;
import com.structureeng.persistence.dao.impl.product.ProductDAOImpl;
import com.structureeng.persistence.dao.impl.product.ProductDefinitionDAOImpl;
import com.structureeng.persistence.dao.impl.product.PurchasePriceDAOImpl;
import com.structureeng.persistence.dao.impl.product.SalePriceDAOImpl;
import com.structureeng.persistence.dao.impl.stock.StockDAOImpl;
import com.structureeng.persistence.dao.impl.stock.WarehouseDAOImpl;
import com.structureeng.persistence.dao.impl.tenant.TenantDAOImpl;

import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;

import javax.inject.Named;

/**
 * Defines the binding of the data access layer module.
 *
 * @author Edgar Rico (edgar.martinez.rico@gmail.com)
 */
@Configuration
public class DAOModule {

    @Bean
    @Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
    @Named("productManufacturerDAO")
    public ProductManufacturerDAO providesProductManufacturerDAO() {
        return new ProductManufacturerDAOImpl();
    }

    @Bean
    @Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
    @Named("productCategoryDAO")
    public ProductCategoryDAO providesProductCategoryDAO() {
        return new ProductCategoryDAOImpl();
    }

    @Bean
    @Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
    @Named("productMeasureUnitDAO")
    public ProductMeasureUnitDAO providesProductMeasureUnitDAO() {
        return new ProductMeasureUnitDAOImpl();
    }

    @Bean
    @Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
    @Named("productDefinitionDAO")
    public ProductDefinitionDAO providesProductDefinitionDAO() {
        return new ProductDefinitionDAOImpl();
    }

    @Bean
    @Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
    @Named("productDAO")
    public ProductDAO providesProductDAO() {
        return new ProductDAOImpl();
    }

    @Bean
    @Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
    @Named("productTypeDAO")
    public ProductTypeDAO providesProductTypeDAO() {
        return new ProductTypeDAOImpl();
    }

    @Bean
    @Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
    @Named("purchasePriceDAO")
    public PurchasePriceDAO providesPurchasePriceDAO() {
        return new PurchasePriceDAOImpl();
    }

    @Bean
    @Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
    @Named("salePriceDAO")
    public SalePriceDAO providesSalePriceDAO() {
        return new SalePriceDAOImpl();
    }

    @Bean
    @Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
    @Named("storeDAO")
    public StoreDAO providesStoreDAO() {
        return new StoreDAOImpl();
    }

    @Bean
    @Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
    @Named("stockDAO")
    public StockDAO providesStockDAO() {
        return new StockDAOImpl();
    }

    @Bean
    @Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
    @Named("taxTypeDAO")
    public TaxTypeDAO providesTaxTypeDAO() {
        return new TaxTypeDAOImpl();
    }

    @Bean
    @Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
    @Named("tenantDAO")
    public TenantDAO providesTenantDAO() {
        return new TenantDAOImpl();
    }

    @Bean
    @Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
    @Named("warehouseDAO")
    public WarehouseDAO providesWarehouseDAO() {
        return new WarehouseDAOImpl();
    }
}
