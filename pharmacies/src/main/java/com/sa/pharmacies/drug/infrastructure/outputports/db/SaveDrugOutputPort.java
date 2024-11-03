package com.sa.pharmacies.drug.infrastructure.outputports.db;

import com.sa.pharmacies.drug.domain.Drug;

public interface SaveDrugOutputPort {
    Drug save(Drug drug);
}
