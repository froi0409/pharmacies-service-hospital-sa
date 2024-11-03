package com.sa.pharmacies.drug.infrastructure.inputports.restapi;

import com.sa.pharmacies.drug.domain.Drug;

import java.util.Optional;

public interface ExistsDrugInputPort {
    Optional<Drug> findByCode(String code);
}
