package com.sa.pharmacies.pharmacy.infrastructure.inputports.restapi;

import com.sa.pharmacies.pharmacy.domain.Pharmacy;

import java.util.Optional;

public interface ExistsPharmacyInputPort {
    Optional<Pharmacy> getPharmacyById(String id);
}
