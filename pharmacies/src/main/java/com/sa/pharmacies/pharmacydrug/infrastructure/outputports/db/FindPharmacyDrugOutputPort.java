package com.sa.pharmacies.pharmacydrug.infrastructure.outputports.db;

import com.sa.pharmacies.pharmacydrug.domain.PharmacyDrug;

import java.util.Optional;

public interface FindPharmacyDrugOutputPort {
    Optional<PharmacyDrug> findByIdAndCode(String idPharmacy, String codeDrug);
}
