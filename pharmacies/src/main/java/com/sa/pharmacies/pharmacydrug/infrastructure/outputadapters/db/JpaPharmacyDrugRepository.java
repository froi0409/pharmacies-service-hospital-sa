package com.sa.pharmacies.pharmacydrug.infrastructure.outputadapters.db;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface JpaPharmacyDrugRepository extends JpaRepository<PharmacyDrugDbEntity, String> {

}
