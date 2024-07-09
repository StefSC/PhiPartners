package com.phipartners.mizuho.data_store.model.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import lombok.Data;

import java.util.List;

@Entity
@Data
public class Instrument {

    @Id
    private String name;

    @OneToMany(mappedBy = "instrument")
    private List<InstrumentVendorPrice> vendorsPrice;
}
