package com.phipartners.mizuho.data_store.configurations;

import com.phipartners.mizuho.data_store.model.business.InstrumentPrice;
import com.phipartners.mizuho.data_store.model.mappers.PriceDataMapper;
import com.phipartners.mizuho.data_store.repositories.InstrumentRepository;
import com.phipartners.mizuho.data_store.repositories.InstrumentVendorPriceRepository;
import com.phipartners.mizuho.data_store.repositories.VendorRepository;
import com.phipartners.mizuho.data_store.services.CachingService;
import com.phipartners.mizuho.data_store.services.CachingServiceInterface;
import com.phipartners.mizuho.data_store.utils.CacheUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.sql.init.dependency.DependsOnDatabaseInitialization;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Configuration
public class DataStoreConfiguration {

    @Value("${priceExpirationTime}")
    private int priceExpirationTime;

    @Bean
    @DependsOnDatabaseInitialization
    public CachingServiceInterface cachingServiceInterface(
            Map<String, List<InstrumentPrice>> vendorPrices,
            Map<String, List<InstrumentPrice>> instrumentPrices,
            VendorRepository vendorRepository,
            InstrumentRepository instrumentRepository,
            InstrumentVendorPriceRepository instrumentVendorPriceRepository
    ) {
        return new CachingService(
                vendorPrices, instrumentPrices, vendorRepository,
                instrumentRepository, instrumentVendorPriceRepository, priceExpirationTime);
    }

    @Bean
    @DependsOnDatabaseInitialization
    public Map<String, List<InstrumentPrice>> vendorPrices(InstrumentVendorPriceRepository instrumentVendorPriceRepository) {
        List<InstrumentPrice> instrumentPrices = instrumentVendorPriceRepository
                .findAllByPriceDateIsAfter(Instant.now().minus(priceExpirationTime, ChronoUnit.SECONDS))
                .stream()
                .map(PriceDataMapper.INSTANCE::mapEntityToInstrumentPrice)
                .toList();
        Map<String, List<InstrumentPrice>> vendorPricesMap = new ConcurrentHashMap<>();
        for (InstrumentPrice instrumentPrice : instrumentPrices) {
            CacheUtils.addPrice(vendorPricesMap, instrumentPrice, instrumentPrice.getVendorName(), Instant.now().minus(priceExpirationTime, ChronoUnit.SECONDS));
        }

        return vendorPricesMap;
    }

    @Bean
    @DependsOnDatabaseInitialization
    public Map<String, List<InstrumentPrice>> instrumentPrices(InstrumentVendorPriceRepository instrumentVendorPriceRepository) {
        List<InstrumentPrice> instrumentPrices = instrumentVendorPriceRepository
                .findAllByPriceDateIsAfter(Instant.now().minus(priceExpirationTime, ChronoUnit.SECONDS))
                .stream()
                .map(PriceDataMapper.INSTANCE::mapEntityToInstrumentPrice)
                .toList();
        Map<String, List<InstrumentPrice>> instrumentPricesMap = new ConcurrentHashMap<>();
        for (InstrumentPrice instrumentPrice : instrumentPrices) {
            CacheUtils.addPrice(instrumentPricesMap, instrumentPrice, instrumentPrice.getInstrumentName(), Instant.now().minus(priceExpirationTime, ChronoUnit.SECONDS));
        }
        return instrumentPricesMap;
    }
}
