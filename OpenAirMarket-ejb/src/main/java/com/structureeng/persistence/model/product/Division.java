// Copyright 2013 Structure Eng Inc.

package com.structureeng.persistence.model.product;

import com.structureeng.persistence.history.HistoryListener;
import com.structureeng.persistence.history.Revision;
import com.structureeng.persistence.model.AbstractCatalogModel;
import com.structureeng.persistence.model.history.product.DivisionHistory;

import com.google.common.base.Preconditions;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

/**
 * Define the different categories in which a
 * {@code com.structureeng.persistence.model.product.ProductType} can be assigned.
 *
 * @author Edgar Rico (edgar.martinez.rico@gmail.com)
 */
@EntityListeners(value = {HistoryListener.class})
@Revision(builder = DivisionHistory.Builder.class)
@Entity
@Table(name = "division", uniqueConstraints = {
        @UniqueConstraint(name = "divisionTenantPK", columnNames = {"idTenant", "idReference"}),
        @UniqueConstraint(name = "divisionUK", columnNames = {"idTenant", "name"})})
public class Division extends AbstractCatalogModel<Long> {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "idDivision")
    private Long id;

    @ManyToOne(cascade = CascadeType.REFRESH)
    @JoinColumn(name = "idParentDivision", referencedColumnName = "idDivision", nullable = true)
    private Division parentDivision;

    @Column(name = "name", nullable = false)
    private String name;

    @OneToMany(mappedBy = "division", cascade = CascadeType.ALL)
    private List<DivisionHistory> divisionHistories;

    @Override
    public Long getId() {
        return id;
    }

    @Override
    public void setId(Long id) {
        this.id = checkPositive(id);
    }

    public Division getParentDivision() {
        return parentDivision;
    }

    public void setParentDivision(Division parentDivision) {
        this.parentDivision = parentDivision;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = checkNotEmpty(name);
    }

    /**
     * Creates a new {@code Builder} instance.
     *
     * @return - new instance
     */
    public static Division.Buider newBuilder() {
        return new Division.Buider();
    }

    /**
     * Builder class that creates instances of {@code Division}.
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
         * Creates a new instance of {@code Division}.
         *
         * @return - new instance
         */
        public Division build() {
            Division division = new Division();
            division.setReferenceId(referenceId);
            division.setName(name);
            return division;
        }
    }
}
