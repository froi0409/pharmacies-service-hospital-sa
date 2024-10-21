package com.sa.pharmacies.pharmacy.infrastructure.outputadapter.db;

import com.sa.pharmacies.common.annotation.OutputAdapter;
import com.sa.pharmacies.pharmacy.domain.Pharmacy;
import com.sa.pharmacies.pharmacy.infrastructure.outputports.db.FindPharmacyByIdOutputPort;
import com.sa.pharmacies.pharmacy.infrastructure.outputports.db.SavePharmacyOutputPort;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Optional;

@OutputAdapter
public class SqlPharmacyOutputAdapter implements SavePharmacyOutputPort, FindPharmacyByIdOutputPort {
    private final JpaPharmacyDbRepository jpaPharmacyDbRepository;

    @Autowired
    public SqlPharmacyOutputAdapter(JpaPharmacyDbRepository jpaPharmacyDbRepository) {
        this.jpaPharmacyDbRepository = jpaPharmacyDbRepository;
    }

    @Override
    public void save(Pharmacy pharmacy) {
        PharmacyDbEntity pharmacyDbEntity = PharmacyDbEntity.fromDomain(pharmacy);
        jpaPharmacyDbRepository.save(pharmacyDbEntity);
    }

    @Override
    public Optional<Pharmacy> findById(String id) {
        return jpaPharmacyDbRepository.findById(id)
                .map(PharmacyDbEntity::toDomain);
    }
}
