package com.sa.pharmacies.pharmacydrug.infrastructure.outputports.db;

import com.sa.pharmacies.pharmacydrug.domain.PharmacyDrug;

public interface SavePharmacyDrugOutputPort {
    PharmacyDrug save(PharmacyDrug pharmacyDrug);
}
