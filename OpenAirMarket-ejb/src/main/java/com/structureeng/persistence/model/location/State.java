// Copyright 2013 Structure Eng Inc.

package com.structureeng.persistence.model.location;

import static com.google.common.base.Preconditions.checkNotNull;

import com.structureeng.persistence.model.AbstractModel;

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
 * Defines the states for a {@code Country}.
 *
 * @author Edgar Rico (edgar.martinez.rico@gmail.com)
 */
@Entity
@Table(name = "state", uniqueConstraints = {
    @UniqueConstraint(name = "stateUK", columnNames = {"code"})})
public class State extends AbstractModel<Long> {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "idState")
    private Long id;

    @JoinColumn(name = "idCountry", referencedColumnName = "idCountry", nullable = false)
    @ManyToOne(fetch = FetchType.LAZY)
    private Country country;

    @Column(name = "code", length = 10, nullable = false)
    private String code;

    @Column(name = "display", length = 255, nullable = false)
    private String display;

    @Override
    public Long getId() {
        return id;
    }

    @Override
    public void setId(Long id) {
        this.id = checkPositive(id);
    }

    public Country getCountry() {
        return country;
    }

    public void setCountry(Country country) {
        this.country = checkNotNull(country);
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = checkNotEmpty(code);
    }

    public String getDisplay() {
        return display;
    }

    public void setDisplay(String display) {
        this.display = checkNotEmpty(display);
    }
}
