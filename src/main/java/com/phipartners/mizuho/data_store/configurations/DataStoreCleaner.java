package com.phipartners.mizuho.data_store.configurations;

import com.phipartners.mizuho.data_store.services.CachingServiceInterface;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

@Configuration
@EnableScheduling
@Slf4j
public class DataStoreCleaner {

    private CachingServiceInterface cachingService;

    @Value("${priceExpirationTime}")
    private int priceExpirationTime;

    public DataStoreCleaner(CachingServiceInterface cachingService) {
        this.cachingService = cachingService;
    }

    @Scheduled(fixedDelayString = "${cleanInterval}")
    public void removeOldData() {
        log.debug("------------------CLEANING OLD DATA------------------");
        this.cachingService.removeOldPrices(priceExpirationTime);
    }
}
