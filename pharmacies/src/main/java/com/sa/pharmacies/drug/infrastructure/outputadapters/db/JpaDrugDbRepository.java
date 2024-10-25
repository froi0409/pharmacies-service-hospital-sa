package com.sa.pharmacies.drug.infrastructure.outputadapters.db;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface JpaDrugDbRepository extends JpaRepository<DrugDbEntity, String> {
    @Override
    Optional<DrugDbEntity> findById(String s);

    Optional<DrugDbEntity> findByName(String name);
}
