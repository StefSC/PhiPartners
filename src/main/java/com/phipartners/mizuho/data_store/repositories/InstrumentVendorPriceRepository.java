package com.phipartners.mizuho.data_store.repositories;

import com.phipartners.mizuho.data_store.model.entities.InstrumentVendorPrice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.Instant;
import java.util.List;

@Repository
public interface InstrumentVendorPriceRepository extends JpaRepository<InstrumentVendorPrice, Long> {

    List<InstrumentVendorPrice> findAllByPriceDateIsAfter(Instant oldestDate);
}
