package com.sa.pharmacies.pharmacydrug.infrastructure.outputports.db;

import com.sa.pharmacies.pharmacydrug.domain.PharmacyDrug;

public interface UpdatePharmacyDrugOutputPort {
    void updatePharmacyDrug(PharmacyDrug pharmacyDrug);
}
