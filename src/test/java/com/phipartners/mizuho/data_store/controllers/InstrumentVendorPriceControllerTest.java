package com.phipartners.mizuho.data_store.controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.phipartners.mizuho.data_store.model.business.InstrumentPrice;
import com.phipartners.mizuho.data_store.model.dto.PriceData;
import com.phipartners.mizuho.data_store.model.entities.Instrument;
import com.phipartners.mizuho.data_store.model.entities.Vendor;
import com.phipartners.mizuho.data_store.repositories.InstrumentRepository;
import com.phipartners.mizuho.data_store.repositories.VendorRepository;
import com.phipartners.mizuho.data_store.services.CachingServiceInterface;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;


@WebMvcTest(controllers = InstrumentVendorPriceController.class)
public class InstrumentVendorPriceControllerTest {

    @MockBean
    private VendorRepository vendorRepository;

    @MockBean
    InstrumentRepository instrumentRepository;

    @MockBean
    CachingServiceInterface cachingService;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void publishPriceTest() throws Exception {
        Instant timestamp = Instant.now();
        PriceData priceData = PriceData.builder()
                .vendorName("vendor")
                .instrumentName("instrument")
                .timestamp(timestamp)
                .price(BigDecimal.valueOf(123))
                .build();
        InstrumentPrice instrumentPrice = InstrumentPrice.builder()
                .price(BigDecimal.valueOf(123))
                .timestamp(timestamp)
                .vendorName("vendor")
                .instrumentName("instrument")
                .build();
        String request = objectMapper.writeValueAsString(priceData);
        when(cachingService.addPrice(any())).thenReturn(instrumentPrice);
        mockMvc.perform(
                post("/instrumentprice")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(request))
                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect(MockMvcResultMatchers.content().json(request));
    }

    @Test
    void getVendorPricesTest() throws Exception {
        Vendor vendor = new Vendor();
        vendor.setName("vendor");

        List<PriceData> prices = mockPrices();

        when(vendorRepository.findByName("vendor")).thenReturn(Optional.of(vendor));
        when(cachingService.getVendorPrices("vendor")).thenReturn(prices);

        String response = objectMapper.writeValueAsString(prices);

        mockMvc.perform(get("/instrumentprice/vendors/vendor"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().json(response));
    }

    @Test
    void getInstrumentPricesTest() throws Exception {
        Instrument instrument = new Instrument();
        instrument.setName("instrument");

        List<PriceData> prices = mockPrices();

        when(instrumentRepository.findByName("instrument")).thenReturn(Optional.of(instrument));
        when(cachingService.getInstrumentPrices("instrument")).thenReturn(prices);

        String response = objectMapper.writeValueAsString(prices);

        mockMvc.perform(get("/instrumentprice/instruments/instrument"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().json(response));
    }

    private List<PriceData> mockPrices() {
        PriceData priceData = PriceData.builder()
                .instrumentName("i1")
                .vendorName("vendor")
                .timestamp(Instant.now())
                .price(BigDecimal.valueOf(1))
                .build();
        PriceData priceData2 = PriceData.builder()
                .instrumentName("i2")
                .vendorName("vendor")
                .timestamp(Instant.now())
                .price(BigDecimal.valueOf(2))
                .build();
        List<PriceData> prices = new ArrayList<>();
        prices.add(priceData);
        prices.add(priceData2);
        return prices;
    }

}
