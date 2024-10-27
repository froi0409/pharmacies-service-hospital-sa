package com.sa.pharmacies.pharmacy.application.existspharmacy;

import com.sa.pharmacies.common.annotation.UseCase;
import com.sa.pharmacies.pharmacy.domain.Pharmacy;
import com.sa.pharmacies.pharmacy.infrastructure.inputports.restapi.ExistsPharmacyInputPort;
import com.sa.pharmacies.pharmacy.infrastructure.outputports.db.FindPharmacyByIdOutputPort;
import com.sa.pharmacies.pharmacydrug.infrastructure.outputports.db.FindPharmacyDrugOutputPort;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Optional;

@Transactional
@UseCase
public class ExistsPharmacyUseCase implements ExistsPharmacyInputPort {
    private final FindPharmacyByIdOutputPort findPharmacyByIdOutputPort;

    @Autowired
    public ExistsPharmacyUseCase(FindPharmacyByIdOutputPort findPharmacyByIdOutputPort) {
        this.findPharmacyByIdOutputPort = findPharmacyByIdOutputPort;
    }

    @Override
    public Optional<Pharmacy> getPharmacyById(String id) {
        return findPharmacyByIdOutputPort.findById(id);
    }
}
