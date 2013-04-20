// Copyright 2013 Structure Eng Inc.

package com.structureeng.persistence.model.history.product;

import com.structureeng.persistence.history.HistoryEntityBuilder;
import com.structureeng.persistence.model.history.AbstractHistoryTenantModel;
import com.structureeng.persistence.model.product.MeasureUnit;

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
 * Define the revision for the {@code Package} entities.
 *
 * @author Edgar Rico (edgar.martinez.rico@gmail.com)
 */
@Entity
@Table(name = "measureUnitHistory", uniqueConstraints = {
        @UniqueConstraint(name = "measureUnitHistoryUK",
            columnNames = {"idMeasureUnit", "idAudit"})})
public class MeasureUnitHistory extends AbstractHistoryTenantModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "idMeasureUnitHistory")
    private Long id;

    @JoinColumn(name = "idMeasureUnit", referencedColumnName = "idMeasureUnit", nullable = false)
    @ManyToOne(fetch = FetchType.LAZY)
    private MeasureUnit measureUnit;

    @Column(name = "idReference", nullable = false)
    private Integer referenceId;

    @Column(name = "name", nullable = false)
    private String name;

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

    public MeasureUnit getMeasureUnit() {
        return measureUnit;
    }

    public void setMeasureUnit(MeasureUnit measureUnit) {
        this.measureUnit = Preconditions.checkNotNull(measureUnit);
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
     * Factory class for the {@code MeasureUnitHistory} entities.
     *
     * @author Edgar Rico (edgar.martinez.rico@gmail.com)
     */
    public static class Builder extends HistoryEntityBuilder<MeasureUnit, MeasureUnitHistory> {

        /**
         * Create an instance of {@code MeasureUnitHistory}.
         *
         * @param measureUnit the instance that will be used to create a new
         *                    {@code MeasureUnitHistory}.
         * @return a new instance
         */
        @Override
        public MeasureUnitHistory build(MeasureUnit measureUnit) {
            MeasureUnitHistory measureUnitHistory = new MeasureUnitHistory();
            measureUnitHistory.setMeasureUnit(measureUnit);
            measureUnitHistory.setReferenceId(measureUnit.getReferenceId());
            measureUnitHistory.setName(measureUnit.getName());
            measureUnitHistory.setCountable(measureUnit.getCountable());
            measureUnitHistory.setExpire(measureUnit.getExpire());
            measureUnitHistory.setActive(measureUnit.getActive());
            measureUnitHistory.setVersion(measureUnit.getVersion());
            return measureUnitHistory;
        }
    }
}
