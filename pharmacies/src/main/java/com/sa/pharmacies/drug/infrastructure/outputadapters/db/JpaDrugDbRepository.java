package com.sa.pharmacies.drug.infrastructure.outputadapters.db;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface JpaDrugDbRepository extends JpaRepository<DrugDbEntity, String> {

}
