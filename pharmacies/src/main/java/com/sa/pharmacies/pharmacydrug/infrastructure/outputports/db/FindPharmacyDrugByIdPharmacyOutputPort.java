package com.sa.pharmacies.pharmacydrug.infrastructure.outputports.db;

import com.sa.pharmacies.pharmacydrug.domain.PharmacyDrug;
import com.sa.pharmacies.pharmacydrug.infrastructure.outputadapters.db.PharmacyDrugDbEntity;

import java.util.List;

public interface FindPharmacyDrugByIdPharmacyOutputPort {
    List<PharmacyDrug> findByPharmacyId(String idPharmacy);
}
