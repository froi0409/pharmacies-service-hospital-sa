package com.sa.pharmacies.pharmacydrug.infrastructure.outputadapters.db;

import io.lettuce.core.dynamic.annotation.Param;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface JpaPharmacyDrugRepository extends JpaRepository<PharmacyDrugDbEntity, String> {
    Optional<PharmacyDrugDbEntity> findByIdPharmacyAndIdDrug(String idPharmacy, String codeDrug);

    @Query(value = "SELECT * FROM pharmacies.pharmacy_drug pd WHERE pd.id_pharmacy = :idPharmacy", nativeQuery = true)
    List<PharmacyDrugDbEntity> findByPharmacyId(@Param("idPharmacy") String idPharmacy);

    @Query(value = "SELECT pd.id_drug FROM pharmacies.pharmacy_drug pd WHERE pd.id_pharmacy = :idPharmacy", nativeQuery = true)
    List<String> findDrugCodesByPharmacyId(@Param("idPharmacy") String idPharmacy);

}
