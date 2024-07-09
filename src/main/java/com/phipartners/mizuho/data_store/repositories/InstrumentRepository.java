package com.phipartners.mizuho.data_store.repositories;

import com.phipartners.mizuho.data_store.model.entities.Instrument;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface InstrumentRepository extends JpaRepository<Instrument, String> {

    Optional<Instrument> findByName(String name);
}
