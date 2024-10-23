package com.sa.pharmacies.drug.infrastructure.outputadapters.db;

import com.sa.pharmacies.common.annotation.OutputAdapter;
import com.sa.pharmacies.drug.domain.Drug;
import com.sa.pharmacies.drug.infrastructure.outputports.db.SaveDrugOutputPort;
import org.springframework.beans.factory.annotation.Autowired;

@OutputAdapter
public class SqlDrugOutputAdapter implements SaveDrugOutputPort {
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
}
