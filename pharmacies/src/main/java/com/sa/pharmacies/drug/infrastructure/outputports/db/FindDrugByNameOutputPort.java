package com.sa.pharmacies.drug.infrastructure.outputports.db;

import com.sa.pharmacies.drug.domain.Drug;

import java.util.Optional;

public interface FindDrugByNameOutputPort {
    Optional<Drug> findByName(String name);
}
