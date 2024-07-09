package com.phipartners.mizuho.data_store.model.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.NotNull;
import lombok.extern.jackson.Jacksonized;
import lombok.Builder;
import lombok.Value;

import java.math.BigDecimal;
import java.time.Instant;

@Jacksonized
@Builder
@Value
public class PriceData {

    @NotNull(message = "vendorName has to be provided")
    String vendorName;

    @NotNull(message = "instrumentName has to be provided")
    String instrumentName;

    @NotNull(message = "price has to be provided")
    BigDecimal price;

    @NotNull(message = "timestamp has to be provided")
    @JsonFormat(pattern = "yyy-MM-dd HH:mm:ss.SSS", timezone = "UTC")
    Instant timestamp;
}
