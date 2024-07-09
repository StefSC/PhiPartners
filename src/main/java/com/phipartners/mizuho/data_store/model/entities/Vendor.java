package com.phipartners.mizuho.data_store.model.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import lombok.Data;

import java.util.List;

@Entity
@Data
public class Vendor {

    @Id
    private String name;

    @OneToMany(mappedBy = "vendor")
    private List<InstrumentVendorPrice> instrumentsPrice;
}
