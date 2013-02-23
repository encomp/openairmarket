// Copyright 2013 Structure Eng Inc.

package com.structureeng.persistence.inject;

import com.structureeng.persistence.dao.TenantDAO;
import com.structureeng.persistence.dao.impl.TenantDAOImpl;
import com.structureeng.persistence.history.HistoryListener;
import com.structureeng.persistence.history.HistoryTransactionSynchronization;
import com.structureeng.persistence.history.RevisionInfo;
import com.structureeng.tenancy.context.TenancyContext;
import com.structureeng.tenancy.context.TenancyContextHolder;
import com.structureeng.tenancy.context.ThreadLocalTenancyContextHolder;
import com.structureeng.tenancy.integration.EntityManagerTenantAwareProvider;
import com.structureeng.tenancy.integration.JpaTransactionManagerTenantAware;
import com.structureeng.tenancy.integration.SharedEntityManagerTenancyAware;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.RemovalListener;
import com.google.common.cache.RemovalNotification;
import com.google.common.collect.ImmutableMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.Scope;
import org.springframework.instrument.classloading.InstrumentationLoadTimeWeaver;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.JpaVendorAdapter;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.support.SharedEntityManagerBean;
import org.springframework.orm.jpa.vendor.EclipseLinkJpaDialect;
import org.springframework.orm.jpa.vendor.EclipseLinkJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.transaction.annotation.TransactionManagementConfigurer;

import java.util.Map;
import java.util.ResourceBundle;
import java.util.concurrent.TimeUnit;

import javax.inject.Named;
import javax.inject.Provider;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;

/**
 * Defines the binding of the persistence module.
 *
 * @author Edgar Rico (edgar.martinez.rico@gmail.com)
 */
@Configuration
@ComponentScan(basePackages = {"com.structureeng.persistence"})
@EnableTransactionManagement
@Import(value = {Database.class})
//@ImportResource("classpath:**/persistenceTest.xml")
public class PersistenceTestModule implements TransactionManagementConfigurer {

    static {
        ThreadLocalTenancyContextHolder.setContextHolder(CacheBuilder.newBuilder()
                .expireAfterAccess(5, TimeUnit.MINUTES)
                .removalListener(
                new RemovalListener<Long, TenancyContext>() {
                    @Override
                    public void onRemoval(RemovalNotification<Long, TenancyContext> notification) {
                    }
                }).build());
        HistoryListener.setRevisionHolder(CacheBuilder.newBuilder()
                .expireAfterAccess(5, TimeUnit.MINUTES)
                .removalListener(
                new RemovalListener<Long, RevisionInfo>() {
                    @Override
                    public void onRemoval(RemovalNotification<Long, RevisionInfo> notification) {
                    }
                }).build());
        TenancyContextHolder.setStrategy(new ThreadLocalTenancyContextHolder());
    }
    
    @Autowired
    private EntityManagerFactory entityManagerFactory;
    
    @Autowired
    @Named("jpaProperties")
    private Map<String, Object> jpaProperties;
    
    @Bean
    @Named("providerEntityManager")
    public Provider<EntityManager> providesProviderEntityManager() {
        EntityManagerTenantAwareProvider provider 
                = new EntityManagerTenantAwareProvider(entityManagerFactory, jpaProperties);
        return provider;
    }
    
    @Bean
    @Named("entityManager")
    public SharedEntityManagerBean providesSharedEntityManagerBean() {
        SharedEntityManagerTenancyAware shared 
                = new SharedEntityManagerTenancyAware(providesProviderEntityManager());
        shared.setEntityManagerFactory(entityManagerFactory);
        shared.setJpaPropertyMap(jpaProperties);
        return shared;
    }

    @Bean
    @Named("transactionManager")
    public JpaTransactionManager providesJpaTransactionManager() {
        JpaTransactionManagerTenantAware jpa 
                = new JpaTransactionManagerTenantAware(providesProviderEntityManager());
        jpa.setEntityManagerFactory(entityManagerFactory);
        return jpa;
    }

    @Override
    public PlatformTransactionManager annotationDrivenTransactionManager() {
        return providesJpaTransactionManager();
    }

    @Bean
    @Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
    @Named("historyTransactionSynchronization")
    public HistoryTransactionSynchronization providesHistoryTxSynchronization() {
        return new HistoryTransactionSynchronization(providesProviderEntityManager());
    }

    @Bean
    @Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
    @Named("tenantDAO")
    public TenantDAO providesTenantDAO() {
        return new TenantDAOImpl();
    }

    @Bean
    @Named("daoResourceBundle")
    public ResourceBundle provideDAOResourceBundle() {
        return ResourceBundle.getBundle("com.structureeng.persistence.dao.DAOResourceBundle");
    }
}

@Configuration
class Database {

    @Bean
    @Named("jpaDialect")
    public EclipseLinkJpaDialect providesJpaDialect() {
        return new EclipseLinkJpaDialect();
    }

    @Bean
    @Named("jpaProperties")
    public Map<String, Object> providesJpaProperties() {
        ImmutableMap.Builder<String, Object> builder = ImmutableMap.builder();
        builder.put("eclipselink.id-validation", "NONE");
        builder.put("eclipselink.weaving", "false");
        builder.put("eclipselink.logging.level", "FINE");
        return builder.build();
    }

    @Bean
    @Named("dataSource")
    public DataSource providesDataSource() {
        DriverManagerDataSource dataSource = new DriverManagerDataSource();
        dataSource.setDriverClassName("com.mysql.jdbc.Driver");
        dataSource.setUrl("jdbc:mysql://127.0.0.1:3306/test");
        dataSource.setUsername("root");
        dataSource.setPassword("toor");
        return dataSource;
    }

    @Bean
    @Named("jpaVendorAdapter")
    public JpaVendorAdapter providesJpaVendorAdapter() {
        EclipseLinkJpaVendorAdapter jpaVendorAdapter = new EclipseLinkJpaVendorAdapter();
        jpaVendorAdapter.setGenerateDdl(false);
        jpaVendorAdapter.setShowSql(true);
        jpaVendorAdapter.setDatabasePlatform(
                "org.eclipse.persistence.platform.database.MySQLPlatform");
        return jpaVendorAdapter;
    }

    @Bean
    @Named("entityManagerFactory")
    public LocalContainerEntityManagerFactoryBean providesEntityManagerFactory() {
        LocalContainerEntityManagerFactoryBean factoryBean = new LocalContainerEntityManagerFactoryBean();
        factoryBean.setPersistenceUnitName("OpenAirMarket_PU");
        factoryBean.setDataSource(providesDataSource());
        factoryBean.setJpaDialect(providesJpaDialect());
        factoryBean.setJpaPropertyMap(providesJpaProperties());
        factoryBean.setJpaVendorAdapter(providesJpaVendorAdapter());
        factoryBean.setLoadTimeWeaver(new InstrumentationLoadTimeWeaver());
        return factoryBean;
    }
}
