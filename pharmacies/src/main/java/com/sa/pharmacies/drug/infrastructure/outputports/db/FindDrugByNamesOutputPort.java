package com.sa.pharmacies.drug.infrastructure.outputports.db;

import com.sa.pharmacies.drug.domain.Drug;

import java.util.List;

public interface FindDrugByNamesOutputPort {
    List<Drug> findByNames(List<String> names);
}
