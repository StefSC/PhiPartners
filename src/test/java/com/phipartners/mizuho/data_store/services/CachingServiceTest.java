package com.phipartners.mizuho.data_store.services;

import com.phipartners.mizuho.data_store.model.business.InstrumentPrice;
import com.phipartners.mizuho.data_store.model.dto.PriceData;
import com.phipartners.mizuho.data_store.model.entities.Instrument;
import com.phipartners.mizuho.data_store.model.entities.Vendor;
import com.phipartners.mizuho.data_store.repositories.InstrumentRepository;
import com.phipartners.mizuho.data_store.repositories.InstrumentVendorPriceRepository;
import com.phipartners.mizuho.data_store.repositories.VendorRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

public class CachingServiceTest {

    VendorRepository vendorRepository;
    InstrumentRepository instrumentRepository;
    InstrumentVendorPriceRepository instrumentVendorPriceRepository;

    @BeforeEach
    void setUpMocks() {
        vendorRepository = Mockito.mock(VendorRepository.class);
        instrumentRepository = Mockito.mock(InstrumentRepository.class);
        instrumentVendorPriceRepository = Mockito.mock(InstrumentVendorPriceRepository.class);
    }

    @Test
    void addPriceTest() {
        Vendor vendor = new Vendor();
        vendor.setName("ING");
        when(vendorRepository.findByName("ING")).thenReturn(Optional.of(vendor));

        Instrument instrument = new Instrument();
        instrument.setName("TSLA");
        when(instrumentRepository.findByName("TSLA")).thenReturn(Optional.of(instrument));


        CachingService cachingService = new CachingService(createVendorTestData(), createInstrumentTestData(),
                vendorRepository, instrumentRepository, instrumentVendorPriceRepository, 120);
        InstrumentPrice price = InstrumentPrice.builder()
                .instrumentName("TSLA")
                .vendorName("ING")
                .timestamp(Instant.now())
                .price(BigDecimal.valueOf(789))
                .build();
        cachingService.addPrice(price);
        assertEquals(1, cachingService.getVendorPrices("ING").size());
        assertEquals(1, cachingService.getInstrumentPrices("TSLA").size());
    }

    @Test
    void addPriceWithNewVendorAndInstrumentTest() {

        when(vendorRepository.findByName("TEST")).thenReturn(Optional.empty());
        when(instrumentRepository.findByName("TEST")).thenReturn(Optional.empty());
        when(instrumentRepository.save(any())).thenReturn(null);

        when(vendorRepository.save(any())).thenReturn(null);

        CachingService cachingService = new CachingService(createVendorTestData(), createInstrumentTestData(),
                vendorRepository, instrumentRepository, instrumentVendorPriceRepository, 120);

        InstrumentPrice price = InstrumentPrice.builder()
                .instrumentName("TEST")
                .vendorName("TEST")
                .timestamp(Instant.now())
                .price(BigDecimal.valueOf(789))
                .build();
        cachingService.addPrice(price);

        assertEquals(1, cachingService.getVendorPrices("TEST").size());
        assertEquals(1, cachingService.getInstrumentPrices("TEST").size());
    }

    @Test
    void getPricesFromNullCacheTest() {
        CachingService cachingService = new CachingService(null, null,
                vendorRepository, instrumentRepository, instrumentVendorPriceRepository, 0);

        List<PriceData> priceList = cachingService.getVendorPrices("test");
        assertNotNull(priceList);
        assertEquals(0, priceList.size());

        priceList = cachingService.getInstrumentPrices("test");
        assertNotNull(priceList);
        assertEquals(0, priceList.size());
    }

    @Test
    void getPricesWithInvalidVendorOrInstrumentNameTest() {
        CachingService cachingService = new CachingService(createVendorTestData(), createInstrumentTestData(),
                vendorRepository, instrumentRepository, instrumentVendorPriceRepository, 0);

        List<PriceData> vendorPriceList = cachingService.getVendorPrices("test");
        List<PriceData> instrumentPriceList = cachingService.getInstrumentPrices("test");

        assertEquals(0, vendorPriceList.size());
        assertEquals(0, instrumentPriceList.size());
    }

    @Test
    void getVendorPricesTest() {
        CachingService cachingService = new CachingService(createVendorTestData(), null,
                vendorRepository, instrumentRepository, instrumentVendorPriceRepository, 0);

        List<PriceData> vendorPriceList = cachingService.getVendorPrices("Mizuho");

        assertEquals(2, vendorPriceList.size());
        assertEquals("Mizuho", vendorPriceList.get(0).getVendorName());
        assertEquals("SNP500", vendorPriceList.get(0).getInstrumentName());
    }

    @Test
    void getInstrumentPricesTest() {
        CachingService cachingService = new CachingService(null, createInstrumentTestData(),
                vendorRepository, instrumentRepository, instrumentVendorPriceRepository, 0);

        List<PriceData> instrumentPriceList = cachingService.getInstrumentPrices("SNP500");

        assertEquals(2, instrumentPriceList.size());
        assertEquals("Citi", instrumentPriceList.get(1).getVendorName());
        assertEquals("SNP500", instrumentPriceList.get(1).getInstrumentName());
    }

    @Test
    void removeOldPricesFromNullCacheTest() {
        CachingService cachingService = new CachingService(null, null,
                vendorRepository, instrumentRepository, instrumentVendorPriceRepository, 0);
        try {
            cachingService.removeOldPrices(0);
        } catch (Exception e) {
            fail("There should not be any exceptions thrown!");
        }
    }

    @Test
    void removeOldPricesTest() {
        CachingService cachingService = new CachingService(createVendorTestData(), createInstrumentTestData(),
                vendorRepository, instrumentRepository, instrumentVendorPriceRepository, 0);
        assertEquals(2, cachingService.getVendorPrices("Mizuho").size());
        cachingService.removeOldPrices(-10);
        assertEquals(0, cachingService.getVendorPrices("Mizuho").size());
    }

    private Map<String, List<InstrumentPrice>> createVendorTestData() {
        List<InstrumentPrice> priceList = new ArrayList<>();
        InstrumentPrice instrumentPrice1 = InstrumentPrice.builder()
                .instrumentName("SNP500")
                .vendorName("Mizuho")
                .timestamp(Instant.now())
                .price(BigDecimal.valueOf(100))
                .build();
        InstrumentPrice instrumentPrice2 = InstrumentPrice.builder()
                .instrumentName("NVDA")
                .vendorName("Mizuho")
                .timestamp(Instant.now())
                .price(BigDecimal.valueOf(456))
                .build();
        priceList.add(instrumentPrice1);
        priceList.add(instrumentPrice2);

        Map<String, List<InstrumentPrice>> vendorPrices = new ConcurrentHashMap<>();
        vendorPrices.put("Mizuho", priceList);
        return vendorPrices;
    }

    private Map<String, List<InstrumentPrice>> createInstrumentTestData() {
        List<InstrumentPrice> priceList = new ArrayList<>();
        InstrumentPrice instrumentPrice1 = InstrumentPrice.builder()
                .instrumentName("SNP500")
                .vendorName("Mizuho")
                .timestamp(Instant.now())
                .price(BigDecimal.valueOf(100))
                .build();
        InstrumentPrice instrumentPrice2 = InstrumentPrice.builder()
                .instrumentName("SNP500")
                .vendorName("Citi")
                .timestamp(Instant.now())
                .price(BigDecimal.valueOf(100.5))
                .build();
        priceList.add(instrumentPrice1);
        priceList.add(instrumentPrice2);

        Map<String, List<InstrumentPrice>> vendorPrices = new ConcurrentHashMap<>();
        vendorPrices.put("SNP500", priceList);
        return vendorPrices;
    }
}
