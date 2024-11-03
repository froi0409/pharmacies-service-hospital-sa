package com.sa.pharmacies.pharmacy.infrastructure.outputports.db;

import com.sa.pharmacies.pharmacy.domain.Pharmacy;

import java.util.Optional;

public interface FindPharmacyByIdOutputPort {
    Optional<Pharmacy> findById(String id);
}
