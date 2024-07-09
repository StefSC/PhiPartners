package com.phipartners.mizuho.data_store.services;

import com.phipartners.mizuho.data_store.model.business.InstrumentPrice;
import com.phipartners.mizuho.data_store.model.dto.PriceData;

import java.util.List;

public interface CachingServiceInterface {

    InstrumentPrice addPrice(InstrumentPrice instrumentPrice);

    List<PriceData> getVendorPrices(String foundVendorName);

    List<PriceData> getInstrumentPrices(String instrumentName);

    void removeOldPrices(int priceExpirationTime);
}
