package com.phipartners.mizuho.data_store.model.mappers;

import com.phipartners.mizuho.data_store.model.business.InstrumentPrice;
import com.phipartners.mizuho.data_store.model.dto.PriceData;
import com.phipartners.mizuho.data_store.model.entities.Instrument;
import com.phipartners.mizuho.data_store.model.entities.InstrumentVendorPrice;
import com.phipartners.mizuho.data_store.model.entities.Vendor;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.Instant;

import static org.junit.jupiter.api.Assertions.*;

public class PriceDataMapperTest {

    @Test
    void mapInstrumentPriceToEntityTest() {
        Instant instant = Instant.now();

        Vendor vendor = new Vendor();
        vendor.setName("vendor");

        Instrument instrument = new Instrument();
        instrument.setName("instrument");

        InstrumentPrice instrumentPrice = InstrumentPrice.builder()
                .vendorName("vendor")
                .instrumentName("instrument")
                .timestamp(instant)
                .price(BigDecimal.valueOf(123))
                .build();


        InstrumentVendorPrice mapped = PriceDataMapper.INSTANCE.mapInstrumentPriceToEntity(instrumentPrice, vendor, instrument);

        assertNotNull(mapped);
        assertEquals(BigDecimal.valueOf(123), mapped.getPrice());
        assertEquals(instant, mapped.getPriceDate());
    }

    @Test
    void mapEntityToInstrumentPriceTest() {
        Instant instant = Instant.now();

        Vendor vendor = new Vendor();
        vendor.setName("vendor");

        Instrument instrument = new Instrument();
        instrument.setName("instrument");

        InstrumentVendorPrice instrumentVendorPrice = new InstrumentVendorPrice();
        instrumentVendorPrice.setVendor(vendor);
        instrumentVendorPrice.setInstrument(instrument);
        instrumentVendorPrice.setId(1L);
        instrumentVendorPrice.setPriceDate(instant);
        instrumentVendorPrice.setPrice(BigDecimal.valueOf(12));

        InstrumentPrice mapped = PriceDataMapper.INSTANCE.mapEntityToInstrumentPrice(instrumentVendorPrice);

        assertEquals("vendor", mapped.getVendorName());
        assertEquals(instant, mapped.getTimestamp());
        assertEquals(BigDecimal.valueOf(12), mapped.getPrice());
    }

    @Test
    void mapInstrumentPriceToPriceDataTest() {
        Instant instant = Instant.now();

        InstrumentPrice instrumentPrice = InstrumentPrice.builder()
                .vendorName("vendor")
                .instrumentName("instrument")
                .timestamp(instant)
                .price(BigDecimal.valueOf(123))
                .build();

        PriceData mapped = PriceDataMapper.INSTANCE.mapInstrumentPriceToPriceData(instrumentPrice);

        assertEquals("vendor", mapped.getVendorName());
        assertEquals(instant, mapped.getTimestamp());
        assertEquals(BigDecimal.valueOf(123), mapped.getPrice());
    }

    @Test
    void mapPriceDataToInstrumentPriceTest() {
        Instant instant = Instant.now();

        PriceData priceData = PriceData.builder()
                .vendorName("vendor")
                .instrumentName("instrument")
                .timestamp(instant)
                .price(BigDecimal.valueOf(1234))
                .build();

        InstrumentPrice mapped = PriceDataMapper.INSTANCE.mapPriceDataToInstrumentPrice(priceData);

        assertEquals("vendor", mapped.getVendorName());
        assertEquals(instant, mapped.getTimestamp());
        assertEquals(BigDecimal.valueOf(1234), mapped.getPrice());
    }
}
