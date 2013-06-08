// Copyright 2013 Structure Eng Inc.

package com.structureeng.persistence.model.price;

import static com.google.common.base.Preconditions.checkNotNull;

import com.structureeng.persistence.model.AbstractSimpleCatalogTenantModel;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.DiscriminatorColumn;
import javax.persistence.DiscriminatorType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.UniqueConstraint;

/**
 * The price list version is a specific version of an individual price list. Working with price list
 * versions enables you to create even more specific price lists, for example for specific
 * customers, or for a limited time period such as an end-of-season sale. Price list versions are
 * time-bound so remain valid only within the dates you specify.
 *
 * Price lists are automatically created based on Product Purchasing Information and the Vendor
 * Category Discount. The other alternative is to copy them from existing price lists and then
 * re-calculate them.
 *
 * @author Edgar Rico (edgar.martinez.rico@gmail.com)
 */
@Entity
@Table(name = "priceListVersion", uniqueConstraints = {
        @UniqueConstraint(name = "priceListVersionPK", columnNames = {"idTenant", "idReference"}),
        @UniqueConstraint(name = "priceListVersionUK",
                columnNames = {"idTenant", "idPriceSchema", "idPriceList", "seqnsao"})})
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "type", discriminatorType = DiscriminatorType.STRING, length = 50)
public abstract class PriceListVersion extends
        AbstractSimpleCatalogTenantModel<Long, String> {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "idPriceListVersion")
    private Long id;

    @JoinColumn(name = "idPriceSchema", referencedColumnName = "idPriceSchema", nullable = false)
    @ManyToOne(fetch = FetchType.LAZY)
    private PriceSchema priceSchema;

    @Column(name = "seqnsao", nullable = false)
    private Integer seqno;

    @Temporal(TemporalType.DATE)
    @Column(name = "validFrom", nullable = false)
    private Date validFrom;

    @Column(name = "description", length = 255)
    private String description;

    @Override
    public Long getId() {
        return id;
    }

    @Override
    public void setId(Long id) {
        this.id = checkPositive(id);
    }

    public PriceSchema getPriceSchema() {
        return priceSchema;
    }

    public void setPriceSchema(PriceSchema priceSchema) {
        this.priceSchema = checkNotNull(priceSchema);
    }

    public Integer getSeqno() {
        return seqno;
    }

    public void setSeqno(Integer seqno) {
        this.seqno = checkPositive(seqno);
    }

    public Date getValidFrom() {
        return validFrom;
    }

    public void setValidFrom(Date validFrom) {
        this.validFrom = checkNotNull(validFrom);
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = checkNillable(description);
    }
}
