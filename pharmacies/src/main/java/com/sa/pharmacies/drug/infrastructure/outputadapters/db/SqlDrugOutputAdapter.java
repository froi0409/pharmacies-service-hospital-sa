package com.sa.pharmacies.drug.infrastructure.outputadapters.db;

import com.sa.pharmacies.common.annotation.OutputAdapter;
import com.sa.pharmacies.drug.domain.Drug;
import com.sa.pharmacies.drug.infrastructure.outputports.db.FindDrugByCodeOutputPort;
import com.sa.pharmacies.drug.infrastructure.outputports.db.FindDrugByNameOutputPort;
import com.sa.pharmacies.drug.infrastructure.outputports.db.SaveDrugOutputPort;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Optional;

@OutputAdapter
public class SqlDrugOutputAdapter implements SaveDrugOutputPort, FindDrugByNameOutputPort, FindDrugByCodeOutputPort {
    private final JpaDrugDbRepository jpaRepository;

    @Autowired
    public SqlDrugOutputAdapter(JpaDrugDbRepository jpaRepository) {
        this.jpaRepository = jpaRepository;
    }


    @Override
    public Drug save(Drug drug) {
        DrugDbEntity drugDbEntity = DrugDbEntity.from(drug);
        drugDbEntity = jpaRepository.save(drugDbEntity);
        return drugDbEntity.toDomain();
    }

    @Override
    public Optional<Drug> findByCode(String code) {
        return jpaRepository.findById(code)
                .map(DrugDbEntity::toDomain);
    }

    @Override
    public Optional<Drug> findByName(String name) {
        return jpaRepository.findByName(name)
                .map(DrugDbEntity::toDomain);
    }
}
