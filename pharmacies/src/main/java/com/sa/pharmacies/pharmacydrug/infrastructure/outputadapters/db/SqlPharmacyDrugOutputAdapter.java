package com.sa.pharmacies.pharmacydrug.infrastructure.outputadapters.db;

import com.sa.pharmacies.common.annotation.OutputAdapter;
import com.sa.pharmacies.drug.domain.Drug;
import com.sa.pharmacies.pharmacy.domain.Pharmacy;
import com.sa.pharmacies.pharmacydrug.domain.PharmacyDrug;
import com.sa.pharmacies.pharmacydrug.infrastructure.outputports.db.FindPharmacyDrugOutputPort;
import com.sa.pharmacies.pharmacydrug.infrastructure.outputports.db.SavePharmacyDrugOutputPort;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Optional;

@OutputAdapter
public class SqlPharmacyDrugOutputAdapter implements SavePharmacyDrugOutputPort, FindPharmacyDrugOutputPort {

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

    @Override
    public Optional<PharmacyDrug> findByIdAndCode(String idPharmacy, String codeDrug) {
        return jpaPharmacyDrugRepository.findByIdPharmacyAndIdDrug(idPharmacy, codeDrug)
                .map(pharmacyDrugDbEntity -> pharmacyDrugDbEntity.toDomain(Pharmacy.builder().build(), Drug.builder().build()));
    }
}
