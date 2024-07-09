package com.phipartners.mizuho.data_store.services;

import com.phipartners.mizuho.data_store.model.business.InstrumentPrice;
import com.phipartners.mizuho.data_store.model.dto.PriceData;
import com.phipartners.mizuho.data_store.model.entities.Instrument;
import com.phipartners.mizuho.data_store.model.entities.InstrumentVendorPrice;
import com.phipartners.mizuho.data_store.model.entities.Vendor;
import com.phipartners.mizuho.data_store.model.mappers.PriceDataMapper;
import com.phipartners.mizuho.data_store.repositories.InstrumentRepository;
import com.phipartners.mizuho.data_store.repositories.InstrumentVendorPriceRepository;
import com.phipartners.mizuho.data_store.repositories.VendorRepository;
import com.phipartners.mizuho.data_store.utils.CacheUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Slf4j
@RequiredArgsConstructor
public class CachingService implements CachingServiceInterface {

    private final Map<String, List<InstrumentPrice>> vendorPrices;
    private final Map<String, List<InstrumentPrice>> instrumentPrices;
    private final VendorRepository vendorRepository;
    private final InstrumentRepository instrumentRepository;
    private final InstrumentVendorPriceRepository instrumentVendorPriceRepository;
    private final int priceExpirationTime;

    @Override
    @Transactional
    public InstrumentPrice addPrice(InstrumentPrice instrumentPrice) {
        Vendor vendor = vendorRepository.findByName(instrumentPrice.getVendorName())
                .orElseGet(() -> {
                    Vendor newVendor = new Vendor();
                    newVendor.setName(instrumentPrice.getVendorName());
                    vendorRepository.save(newVendor);
                    return newVendor;
                });
        Instrument instrument = instrumentRepository.findByName(instrumentPrice.getInstrumentName())
                .orElseGet(() -> {
                    Instrument newInstrument = new Instrument();
                    newInstrument.setName(instrumentPrice.getInstrumentName());
                    instrumentRepository.save(newInstrument);
                    return newInstrument;
                });

        InstrumentVendorPrice entity = PriceDataMapper.INSTANCE.mapInstrumentPriceToEntity(instrumentPrice, vendor, instrument);
        instrumentVendorPriceRepository.save(entity);

        InstrumentPrice add = PriceDataMapper.INSTANCE.mapEntityToInstrumentPrice(entity);
        CacheUtils.addPrice(vendorPrices, add, add.getVendorName(), Instant.now().minus(priceExpirationTime, ChronoUnit.SECONDS));
        CacheUtils.addPrice(instrumentPrices, add, add.getInstrumentName(), Instant.now().minus(priceExpirationTime, ChronoUnit.SECONDS));

        return instrumentPrice;
    }

    @Override
    public List<PriceData> getVendorPrices(String foundVendorName) {
        return Optional.ofNullable(this.vendorPrices).map(optinal -> optinal.get(foundVendorName))
                .map(
                        list -> list.stream()
                                .map(PriceDataMapper.INSTANCE::mapInstrumentPriceToPriceData)
                                .toList()
                ).orElse(Collections.emptyList());
    }

    @Override
    public List<PriceData> getInstrumentPrices(String instrumentName) {
        return Optional.ofNullable(this.instrumentPrices).map(map -> map.get(instrumentName)).map(
                list -> list.stream()
                        .map(PriceDataMapper.INSTANCE::mapInstrumentPriceToPriceData)
                        .toList()
        ).orElse(Collections.emptyList());
    }

    @Override
    public void removeOldPrices(int priceExpirationTime) {
        Instant oldestDate = Instant.now().minus(priceExpirationTime, ChronoUnit.SECONDS);
        log.debug("------------------Deleting prices older than: " + oldestDate + "------------------");
        CacheUtils.removeOldData(this.vendorPrices, oldestDate);
        CacheUtils.removeOldData(this.instrumentPrices, oldestDate);
    }
}
