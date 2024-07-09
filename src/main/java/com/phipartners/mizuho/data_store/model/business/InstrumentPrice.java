package com.phipartners.mizuho.data_store.model.business;

import lombok.Builder;
import lombok.Value;

import java.math.BigDecimal;
import java.time.Instant;

@Value
@Builder
public class InstrumentPrice {

    String instrumentName;
    String vendorName;
    BigDecimal price;
    Instant timestamp;
}
