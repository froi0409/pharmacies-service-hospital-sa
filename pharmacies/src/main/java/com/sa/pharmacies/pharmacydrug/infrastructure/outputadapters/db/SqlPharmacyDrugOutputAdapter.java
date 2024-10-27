package com.sa.pharmacies.pharmacydrug.infrastructure.outputadapters.db;

import com.sa.pharmacies.common.annotation.OutputAdapter;
import com.sa.pharmacies.pharmacydrug.domain.PharmacyDrug;
import com.sa.pharmacies.pharmacydrug.infrastructure.outputports.db.SavePharmacyDrugOutputPort;
import org.springframework.beans.factory.annotation.Autowired;

@OutputAdapter
public class SqlPharmacyDrugOutputAdapter implements SavePharmacyDrugOutputPort {

    private final JpaPharmacyDrugRepository jpaPharmacyDrugRepository;

    @Autowired
    public SqlPharmacyDrugOutputAdapter(JpaPharmacyDrugRepository jpaPharmacyDrugRepository) {
        this.jpaPharmacyDrugRepository = jpaPharmacyDrugRepository;
    }

    @Override
    public PharmacyDrug save(PharmacyDrug pharmacyDrug) {
        PharmacyDrugDbEntity pharmacyDrugDbEntity = PharmacyDrugDbEntity.from(pharmacyDrug);
        pharmacyDrugDbEntity = jpaPharmacyDrugRepository.save(pharmacyDrugDbEntity);
        return pharmacyDrugDbEntity.toDomain(pharmacyDrug.getPharmacy(), pharmacyDrug.getDrug() );
    }
}
