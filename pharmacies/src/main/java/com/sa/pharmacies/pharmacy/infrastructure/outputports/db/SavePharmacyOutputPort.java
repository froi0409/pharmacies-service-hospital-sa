package com.sa.pharmacies.pharmacy.infrastructure.outputports.db;

import com.sa.pharmacies.pharmacy.domain.Pharmacy;

public interface SavePharmacyOutputPort {
    void save(Pharmacy pharmacy);
}
