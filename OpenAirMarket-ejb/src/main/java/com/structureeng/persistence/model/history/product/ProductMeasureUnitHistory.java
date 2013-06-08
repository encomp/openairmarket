// Copyright 2013 Structure Eng Inc.

package com.structureeng.persistence.model.history.product;

import com.structureeng.persistence.history.HistoryEntityBuilder;
import com.structureeng.persistence.model.history.AbstractHistoryCatalogTenantModel;
import com.structureeng.persistence.model.history.AbstractHistoryTenantModel;
import com.structureeng.persistence.model.product.ProductMeasureUnit;

import com.google.common.base.Preconditions;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

/**
 * Define the revision for the {@code ProductMeasureUnit} entities.
 *
 * @author Edgar Rico (edgar.martinez.rico@gmail.com)
 */
@Entity
@Table(name = "productMeasureUnitHistory", uniqueConstraints = {
        @UniqueConstraint(name = "productMeasureUnitHistoryUK",
            columnNames = {"idProductMeasureUnit", "idAudit"})})
public class ProductMeasureUnitHistory extends AbstractHistoryCatalogTenantModel<Integer> {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "idProductMeasureUnitHistory")
    private Long id;

    @JoinColumn(name = "idProductMeasureUnit", referencedColumnName = "idProductMeasureUnit",
            nullable = false)
    @ManyToOne(fetch = FetchType.LAZY)
    private ProductMeasureUnit productMeasureUnit;

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

    public ProductMeasureUnit getProductMeasureUnit() {
        return productMeasureUnit;
    }

    public void setProductMeasureUnit(ProductMeasureUnit productMeasureUnit) {
        this.productMeasureUnit = Preconditions.checkNotNull(productMeasureUnit);
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
     * Factory class for the {@code ProductMeasureUnitHistory} entities.
     *
     * @author Edgar Rico (edgar.martinez.rico@gmail.com)
     */
    public static class Builder extends HistoryEntityBuilder<ProductMeasureUnit,
            ProductMeasureUnitHistory> {

        /**
         * Create an instance of {@code ProductMeasureUnitHistory}.
         *
         * @param productMeasureUnit the instance that will be used to create a new
         *                           {@code ProductMeasureUnit}.
         * @return a new instance
         */
        @Override
        public ProductMeasureUnitHistory build(ProductMeasureUnit productMeasureUnit) {
            ProductMeasureUnitHistory measureUnitHistory = new ProductMeasureUnitHistory();
            measureUnitHistory.setProductMeasureUnit(productMeasureUnit);
            measureUnitHistory.setReferenceId(productMeasureUnit.getReferenceId());
            measureUnitHistory.setName(productMeasureUnit.getName());
            measureUnitHistory.setCountable(productMeasureUnit.getCountable());
            measureUnitHistory.setExpire(productMeasureUnit.getExpire());
            measureUnitHistory.setActive(productMeasureUnit.getActive());
            measureUnitHistory.setVersion(productMeasureUnit.getVersion());
            return measureUnitHistory;
        }
    }
}
