// Copyright 2013 Structure Eng Inc.

package com.structureeng.persistence.model.product;

import com.structureeng.persistence.history.HistoryListener;
import com.structureeng.persistence.history.Revision;
import com.structureeng.persistence.model.AbstractCatalogTenantModel;
import com.structureeng.persistence.model.history.product.ProductMeasureUnitHistory;

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
 * Define the different measure units of a
 * {@code com.structureeng.persistence.model.product.ProductType}.
 *
 * @author Edgar Rico (edgar.martinez.rico@gmail.com)
 */
@EntityListeners(value = {HistoryListener.class})
@Revision(builder = ProductMeasureUnitHistory.Builder.class)
@Entity
@Table(name = "productMeasureUnit", uniqueConstraints = {
       @UniqueConstraint(name = "productMeasureUnitPK", columnNames = {"idTenant", "idReference"}),
       @UniqueConstraint(name = "productMeasureUnitUK", columnNames = {"idTenant", "name"})})
public class ProductMeasureUnit extends AbstractCatalogTenantModel<Long, Integer> {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "idProductMeasureUnit")
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
    public static ProductMeasureUnit.Buider newBuilder() {
        return new ProductMeasureUnit.Buider();
    }

    /**
     * Builder class that creates instances of {@code ProductMeasureUnit}.
     *
     * @author Edgar Rico (edgar.martinez.rico@gmail.com)
     */
    public static class Buider {

        private Integer referenceId;
        private String name;
        private Boolean countable;
        private Boolean expire;

        public ProductMeasureUnit.Buider setReferenceId(Integer referenceId) {
            this.referenceId = checkPositive(referenceId);
            return this;
        }

        public ProductMeasureUnit.Buider setName(String name) {
            this.name = checkNotEmpty(name);
            return this;
        }

        public ProductMeasureUnit.Buider setCountable(Boolean countable) {
            this.countable = Preconditions.checkNotNull(countable);
            return this;
        }

        public ProductMeasureUnit.Buider setExpire(Boolean expire) {
            this.expire = Preconditions.checkNotNull(expire);
            return this;
        }

        /**
         * Creates a new instance of {@code ProductMeasureUnit}.
         *
         * @return - new instance
         */
        public ProductMeasureUnit build() {
            ProductMeasureUnit paquete = new ProductMeasureUnit();
            paquete.setReferenceId(referenceId);
            paquete.setName(name);
            paquete.setCountable(countable);
            paquete.setExpire(expire);
            return paquete;
        }
    }
}
