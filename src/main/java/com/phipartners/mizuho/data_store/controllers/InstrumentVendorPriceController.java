package com.phipartners.mizuho.data_store.controllers;

import com.phipartners.mizuho.data_store.model.business.InstrumentPrice;
import com.phipartners.mizuho.data_store.model.dto.PriceData;
import com.phipartners.mizuho.data_store.model.entities.Instrument;
import com.phipartners.mizuho.data_store.model.entities.Vendor;
import com.phipartners.mizuho.data_store.model.exceptions.NotFoundException;
import com.phipartners.mizuho.data_store.model.mappers.PriceDataMapper;
import com.phipartners.mizuho.data_store.repositories.InstrumentRepository;
import com.phipartners.mizuho.data_store.repositories.VendorRepository;
import com.phipartners.mizuho.data_store.services.CachingServiceInterface;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.antlr.v4.runtime.misc.NotNull;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/instrumentprice")
@RequiredArgsConstructor
public class InstrumentVendorPriceController {

    private final CachingServiceInterface cachingServiceInterface;
    private final VendorRepository vendorRepository;
    private final InstrumentRepository instrumentRepository;

    @PostMapping
    public ResponseEntity<PriceData> publishPrice(@NotNull @Valid @RequestBody PriceData priceData) {
        InstrumentPrice instrumentPrice = PriceDataMapper.INSTANCE.mapPriceDataToInstrumentPrice(priceData);
        instrumentPrice = cachingServiceInterface.addPrice(instrumentPrice);
        PriceData response = PriceDataMapper.INSTANCE.mapInstrumentPriceToPriceData(instrumentPrice);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @GetMapping(value = "/vendors/{vendorName}")
    public List<PriceData> getVendorPrices(@PathVariable String vendorName) {
        String foundVendorName = vendorRepository.findByName(vendorName).map(Vendor::getName)
                .orElseThrow(() -> new NotFoundException("No vendor named " + vendorName));
        return cachingServiceInterface.getVendorPrices(foundVendorName);
    }

    @GetMapping(value = "/instruments/{instrumentName}")
    public List<PriceData> getInstrumentPrices(@PathVariable String instrumentName) {
        String foundInstrumentName = instrumentRepository.findByName(instrumentName).map(Instrument::getName)
                .orElseThrow(() -> new NotFoundException("No instruments named " + instrumentName));
        return cachingServiceInterface.getInstrumentPrices(foundInstrumentName);
    }
}
