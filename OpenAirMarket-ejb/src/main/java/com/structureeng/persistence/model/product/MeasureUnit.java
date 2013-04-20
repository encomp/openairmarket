// Copyright 2013 Structure Eng Inc.

package com.structureeng.persistence.model.product;

import com.structureeng.persistence.history.HistoryListener;
import com.structureeng.persistence.history.Revision;
import com.structureeng.persistence.model.AbstractCatalogTenantModel;
import com.structureeng.persistence.model.history.product.MeasureUnitHistory;

import com.google.common.base.Preconditions;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

/**
 * Define the different packages of a
 * {@code com.structureeng.persistence.model.product.ProductType}.
 *
 * @author Edgar Rico (edgar.martinez.rico@gmail.com)
 */
@EntityListeners(value = {HistoryListener.class})
@Revision(builder = MeasureUnitHistory.Builder.class)
@Entity
@Table(name = "measureUnit", uniqueConstraints = {
        @UniqueConstraint(name = "measureUnitTenantPK", columnNames = {"idTenant", "idReference"}),
        @UniqueConstraint(name = "measureUnitUK", columnNames = {"idTenant", "name"})})
public class MeasureUnit extends AbstractCatalogTenantModel<Long, Integer> {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "idMeasureUnit")
    private Long id;

    @Column(name = "countable", nullable = false)
    private Boolean countable;

    @Column(name = "expire", nullable = false)
    private Boolean expire;

    @Override
    public Long getId() {
        return id;
    }

    @Override
    public void setId(Long id) {
        this.id = checkPositive(id);
    }

    public Boolean getCountable() {
        return countable;
    }

    public void setCountable(Boolean countable) {
        this.countable = Preconditions.checkNotNull(countable);
    }

    public Boolean getExpire() {
        return expire;
    }

    public void setExpire(Boolean expire) {
        this.expire = Preconditions.checkNotNull(expire);
    }

    /**
     * Creates a new {@code Builder} instance.
     *
     * @return - new instance
     */
    public static MeasureUnit.Buider newBuilder() {
        return new MeasureUnit.Buider();
    }

    /**
     * Builder class that creates instances of {@code MeasureUnit}.
     *
     * @author Edgar Rico (edgar.martinez.rico@gmail.com)
     */
    public static class Buider {

        private Integer referenceId;
        private String name;
        private Boolean countable;
        private Boolean expire;

        public MeasureUnit.Buider setReferenceId(Integer referenceId) {
            this.referenceId = checkPositive(referenceId);
            return this;
        }

        public MeasureUnit.Buider setName(String name) {
            this.name = checkNotEmpty(name);
            return this;
        }

        public MeasureUnit.Buider setCountable(Boolean countable) {
            this.countable = Preconditions.checkNotNull(countable);
            return this;
        }

        public MeasureUnit.Buider setExpire(Boolean expire) {
            this.expire = Preconditions.checkNotNull(expire);
            return this;
        }

        /**
         * Creates a new instance of {@code MeasureUnit}.
         *
         * @return - new instance
         */
        public MeasureUnit build() {
            MeasureUnit paquete = new MeasureUnit();
            paquete.setReferenceId(referenceId);
            paquete.setName(name);
            paquete.setCountable(countable);
            paquete.setExpire(expire);
            return paquete;
        }
    }
}
