package com.sa.pharmacies.pharmacy.infrastructure.outputadapter.db;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface JpaPharmacyDbRepository extends JpaRepository<PharmacyDbEntity, String> {

}
