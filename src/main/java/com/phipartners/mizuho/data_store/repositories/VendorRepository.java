package com.phipartners.mizuho.data_store.repositories;

import com.phipartners.mizuho.data_store.model.entities.Vendor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface VendorRepository extends JpaRepository<Vendor, String> {

    Optional<Vendor> findByName(String name);
}
