package com.sa.pharmacies.drug.infrastructure.outputadapters.db;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface JpaDrugDbRepository extends JpaRepository<DrugDbEntity, String> {
    @Override
    Optional<DrugDbEntity> findById(String s);

    Optional<DrugDbEntity> findByName(String name);

    @Override
    List<DrugDbEntity> findAll();

    @Query(value = "SELECT * FROM pharmacies.drug d WHERE d.code IN :codes", nativeQuery = true)
    List<DrugDbEntity> findByCodes(@Param("codes") List<String> codes);

}
