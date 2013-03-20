// Copyright 2013 Structure Eng Inc.

package com.structureeng.persistence.dao.inject;

import com.structureeng.persistence.dao.CompanyDAO;
import com.structureeng.persistence.dao.DivisionDAO;
import com.structureeng.persistence.dao.PackageDAO;
import com.structureeng.persistence.dao.TenantDAO;
import com.structureeng.persistence.dao.impl.product.CompanyDAOImpl;
import com.structureeng.persistence.dao.impl.product.DivisionDAOImpl;
import com.structureeng.persistence.dao.impl.product.PackageDAOImpl;
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
    @Named("companyDAO")
    public CompanyDAO providesCompanyDAO() {
        return new CompanyDAOImpl();
    }
    
    @Bean
    @Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
    @Named("divisionDAO")
    public DivisionDAO providesDivisionDAO() {
        return new DivisionDAOImpl();
    }
    
    @Bean
    @Named("packageDAO")
    public PackageDAO providesPackageDAO() {
        return new PackageDAOImpl();
    }
    
    @Bean
    @Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
    @Named("tenantDAO")
    public TenantDAO providesTenantDAO() {
        return new TenantDAOImpl();
    }
}
