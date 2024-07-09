package com.phipartners.mizuho.data_store.configurations;

import com.phipartners.mizuho.data_store.model.business.InstrumentPrice;
import com.phipartners.mizuho.data_store.model.entities.Instrument;
import com.phipartners.mizuho.data_store.model.entities.InstrumentVendorPrice;
import com.phipartners.mizuho.data_store.model.entities.Vendor;
import com.phipartners.mizuho.data_store.repositories.InstrumentVendorPriceRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.*;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

public class DataStoreConfigurationTest {

    private InstrumentVendorPriceRepository instrumentVendorPriceRepository;

    @BeforeEach
    void setUp() {
        instrumentVendorPriceRepository = Mockito.mock(InstrumentVendorPriceRepository.class);
    }

    @Test
    void vendorPricesTest() {
        Instant instant = Instant.now().plus(120, ChronoUnit.SECONDS);

        Vendor vendor = new Vendor();
        vendor.setName("vendor");

        Instrument instrument = new Instrument();
        instrument.setName("instrument1");

        InstrumentVendorPrice instrumentVendorPrice = new InstrumentVendorPrice();
        instrumentVendorPrice.setVendor(vendor);
        instrumentVendorPrice.setInstrument(instrument);
        instrumentVendorPrice.setPriceDate(instant);
        instrumentVendorPrice.setPrice(BigDecimal.valueOf(1));

        Instrument instrument2 = new Instrument();
        instrument2.setName("instrument2");

        InstrumentVendorPrice instrumentVendorPrice2 = new InstrumentVendorPrice();
        instrumentVendorPrice2.setVendor(vendor);
        instrumentVendorPrice2.setInstrument(instrument2);
        instrumentVendorPrice2.setPriceDate(instant);
        instrumentVendorPrice2.setPrice(BigDecimal.valueOf(2));

        List<InstrumentVendorPrice> list = new ArrayList<>();
        list.add(instrumentVendorPrice);
        list.add(instrumentVendorPrice2);

        when(instrumentVendorPriceRepository.findAllByPriceDateIsAfter(any())).thenReturn(list);

        List<InstrumentPrice> instrumentPrices = new ArrayList<>();
        instrumentPrices.add(InstrumentPrice.builder()
                .vendorName("vendor")
                .instrumentName("instrument1")
                .timestamp(instant)
                .price(BigDecimal.valueOf(1))
                .build());
        instrumentPrices.add(InstrumentPrice.builder()
                .vendorName("vendor")
                .instrumentName("instrument2")
                .timestamp(instant)
                .price(BigDecimal.valueOf(2))
                .build());

        Map<String, List<InstrumentPrice>> result = new HashMap<>();
        result.put("vendor", instrumentPrices);

        DataStoreConfiguration configuration = new DataStoreConfiguration();
        Map<String, List<InstrumentPrice>> dataStore = configuration.vendorPrices(instrumentVendorPriceRepository);
        assertEquals(result, dataStore);
    }

    @Test
    void vendorPricesNullTest() {
        when(instrumentVendorPriceRepository.findAll()).thenReturn(Collections.emptyList());

        HashMap<String, List<InstrumentPrice>> result = new HashMap<>();
        DataStoreConfiguration configuration = new DataStoreConfiguration();
        Map<String, List<InstrumentPrice>> dataStore = configuration.vendorPrices(instrumentVendorPriceRepository);

        assertEquals(result, dataStore);
    }

    @Test
    void instrumentPricesTest() {
        Instant instant = Instant.now().plus(120, ChronoUnit.SECONDS);

        Vendor vendor = new Vendor();
        vendor.setName("vendor1");

        Instrument instrument = new Instrument();
        instrument.setName("instrument");

        InstrumentVendorPrice instrumentVendorPrice = new InstrumentVendorPrice();
        instrumentVendorPrice.setVendor(vendor);
        instrumentVendorPrice.setInstrument(instrument);
        instrumentVendorPrice.setPriceDate(instant);
        instrumentVendorPrice.setPrice(BigDecimal.valueOf(1));

        Vendor vendor2 = new Vendor();
        vendor2.setName("vendor2");

        InstrumentVendorPrice instrumentVendorPrice2 = new InstrumentVendorPrice();
        instrumentVendorPrice2.setVendor(vendor2);
        instrumentVendorPrice2.setInstrument(instrument);
        instrumentVendorPrice2.setPriceDate(instant);
        instrumentVendorPrice2.setPrice(BigDecimal.valueOf(2));

        List<InstrumentVendorPrice> list = new ArrayList<>();
        list.add(instrumentVendorPrice);
        list.add(instrumentVendorPrice2);

        when(instrumentVendorPriceRepository.findAllByPriceDateIsAfter(any())).thenReturn(list);

        List<InstrumentPrice> instrumentPrices = new ArrayList<>();
        instrumentPrices.add(InstrumentPrice.builder()
                .vendorName("vendor1")
                .instrumentName("instrument")
                .timestamp(instant)
                .price(BigDecimal.valueOf(1))
                .build());
        instrumentPrices.add(InstrumentPrice.builder()
                .vendorName("vendor2")
                .instrumentName("instrument")
                .timestamp(instant)
                .price(BigDecimal.valueOf(2))
                .build());

        Map<String, List<InstrumentPrice>> result = new HashMap<>();
        result.put("instrument", instrumentPrices);

        DataStoreConfiguration configuration = new DataStoreConfiguration();
        Map<String, List<InstrumentPrice>> dataStore = configuration.instrumentPrices(instrumentVendorPriceRepository);
        assertEquals(result, dataStore);
    }

    @Test
    void instrumentPricesNullTest() {
        when(instrumentVendorPriceRepository.findAll()).thenReturn(Collections.emptyList());

        HashMap<String, List<InstrumentPrice>> result = new HashMap<>();
        DataStoreConfiguration configuration = new DataStoreConfiguration();
        Map<String, List<InstrumentPrice>> dataStore = configuration.instrumentPrices(instrumentVendorPriceRepository);

        assertEquals(result, dataStore);
    }
}
