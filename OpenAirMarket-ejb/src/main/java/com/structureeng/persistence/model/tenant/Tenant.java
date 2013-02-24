// Copyright 2013 Structure Eng Inc.

package com.structureeng.persistence.model.tenant;

import com.structureeng.persistence.history.HistoryListener;
import com.structureeng.persistence.history.Revision;
import com.structureeng.persistence.model.AbstractActiveModel;
import com.structureeng.persistence.model.history.tenant.TenantHistory;

import com.google.common.base.Preconditions;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

/**
 * Define the characteristics of a tenant.
 *
 * @author Edgar Rico (edgar.martinez.rico@gmail.com)
 */
@EntityListeners(value = {HistoryListener.class})
@Revision(builder = TenantHistory.Builder.class)
@Entity
@Table(name = "tenant", uniqueConstraints = {
        @UniqueConstraint(name = "tenantPK", columnNames = {"idReference"}),
        @UniqueConstraint(name = "tenantUK", columnNames = {"name"})})
public class Tenant extends AbstractActiveModel<Long> {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "idTenant")
    private Long id;

    @Column(name = "idReference", nullable = false)
    private Integer referenceId;

    @Column(name = "name", nullable = false)
    private String name;

    @OneToMany(mappedBy = "tenant", cascade = CascadeType.ALL)
    private List<TenantHistory> tenantHistories;

    public Tenant() {        
    }
    
    @Override
    public Long getId() {
        return id;
    }

    @Override
    public void setId(Long id) {
        this.id = Preconditions.checkNotNull(id);
    }

    public Integer getReferenceId() {
        return referenceId;
    }

    public void setReferenceId(Integer referenceId) {
        this.referenceId = checkPositive(referenceId);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {        
        this.name = checkNotEmpty(name);
    }

    public List<TenantHistory> getTenantHistories() {
        return tenantHistories;
    }

    public void setTenantHistories(List<TenantHistory> tenantHistories) {
        this.tenantHistories = tenantHistories;
    }
    
    @Override
    public String toString() {
        return "Tenant {" + "id=" + id + '}';
    }
    
    /**
     * Creates a new {@code Builder} instance.
     * 
     * @return - new instance
     */
    public static Buider newBuilder () {
        return new Buider();
    }
    
    /**
     * Builder class that creates instances of {@code Tenant}.
     * 
     * @author Edgar Rico (edgar.martinez.rico@gmail.com)
     */
    public static class Buider {
        
        private Integer referenceId;
        private String name;

        public Buider setReferenceId(Integer referenceId) {
            this.referenceId = Preconditions.checkNotNull(referenceId);
            return this;
        }

        public Buider setName(String name) {
            this.name = Preconditions.checkNotNull(name);
            return this;
        }
        
        /**
         * Creates a new instance of {@code Tenant}.
         * 
         * @return - new instance
         */
        public Tenant build() {
            Tenant tenant = new Tenant();
            tenant.setReferenceId(referenceId);
            tenant.setName(name);
            return tenant;
        }      
    }
}
