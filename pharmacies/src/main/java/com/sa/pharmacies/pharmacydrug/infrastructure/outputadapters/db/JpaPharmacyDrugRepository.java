package com.sa.pharmacies.pharmacydrug.infrastructure.outputadapters.db;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface JpaPharmacyDrugRepository extends JpaRepository<PharmacyDrugDbEntity, String> {
    Optional<PharmacyDrugDbEntity> findByIdPharmacyAndIdDrug(String idPharmacy, String codeDrug);
}
