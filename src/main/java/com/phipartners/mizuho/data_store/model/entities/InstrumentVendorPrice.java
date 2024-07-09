package com.phipartners.mizuho.data_store.model.entities;

import jakarta.persistence.*;
import lombok.Data;

import java.math.BigDecimal;
import java.time.Instant;

@Data
@Entity
public class InstrumentVendorPrice {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, updatable = false)
    private BigDecimal price;

    @Column(nullable = false, updatable = false)
    private Instant priceDate;

    @ManyToOne(optional = false)
    @JoinColumn(name = "instrument_name", referencedColumnName = "name")
    private Instrument instrument;

    @ManyToOne(optional = false)
    @JoinColumn(name = "vendor_name", referencedColumnName = "name")
    private Vendor vendor;
}
