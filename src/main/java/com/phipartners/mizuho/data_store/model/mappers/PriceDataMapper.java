package com.phipartners.mizuho.data_store.model.mappers;

import com.phipartners.mizuho.data_store.model.business.InstrumentPrice;
import com.phipartners.mizuho.data_store.model.dto.PriceData;
import com.phipartners.mizuho.data_store.model.entities.Instrument;
import com.phipartners.mizuho.data_store.model.entities.InstrumentVendorPrice;
import com.phipartners.mizuho.data_store.model.entities.Vendor;

public class PriceDataMapper {

    public static final PriceDataMapper INSTANCE = new PriceDataMapper();


    public InstrumentVendorPrice mapInstrumentPriceToEntity(InstrumentPrice instrumentPrice, Vendor vendor, Instrument instrument) {
        InstrumentVendorPrice instrumentVendorPrice = new InstrumentVendorPrice();
        instrumentVendorPrice.setPrice(instrumentPrice.getPrice());
        instrumentVendorPrice.setPriceDate(instrumentPrice.getTimestamp());
        instrumentVendorPrice.setVendor(vendor);
        instrumentVendorPrice.setInstrument(instrument);
        return instrumentVendorPrice;
    }

    public InstrumentPrice mapEntityToInstrumentPrice(InstrumentVendorPrice instrumentVendorPrice) {

        return InstrumentPrice.builder()
                .instrumentName(instrumentVendorPrice.getInstrument().getName())
                .vendorName(instrumentVendorPrice.getVendor().getName())
                .price(instrumentVendorPrice.getPrice())
                .timestamp(instrumentVendorPrice.getPriceDate())
                .build();
    }

    public PriceData mapInstrumentPriceToPriceData(InstrumentPrice instrumentPrice) {
        return PriceData.builder()
                .price(instrumentPrice.getPrice())
                .vendorName(instrumentPrice.getVendorName())
                .timestamp(instrumentPrice.getTimestamp())
                .instrumentName(instrumentPrice.getInstrumentName())
                .build();
    }

    public InstrumentPrice mapPriceDataToInstrumentPrice(PriceData priceData) {
        return InstrumentPrice.builder()
                .price(priceData.getPrice())
                .timestamp(priceData.getTimestamp())
                .instrumentName(priceData.getInstrumentName())
                .vendorName(priceData.getVendorName())
                .build();
    }
}
