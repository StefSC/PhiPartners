package com.phipartners.mizuho.data_store.model.dto;

import lombok.Builder;
import lombok.Value;
import lombok.extern.jackson.Jacksonized;

@Jacksonized
@Builder
@Value
public class ErrorData {
    String info;
}
