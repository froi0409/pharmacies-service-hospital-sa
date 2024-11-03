package com.sa.pharmacies.pharmacy.application.createpharmacy;

import com.sa.pharmacies.common.annotation.UseCase;
import com.sa.pharmacies.common.exceptions.EntityAlreadyExistsException;
import com.sa.pharmacies.pharmacy.domain.Pharmacy;
import com.sa.pharmacies.pharmacy.infrastructure.inputports.restapi.CreatePharmacyByEventInputPort;
import com.sa.pharmacies.pharmacy.infrastructure.outputports.db.FindPharmacyByIdOutputPort;
import com.sa.pharmacies.pharmacy.infrastructure.outputports.db.SavePharmacyOutputPort;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Optional;

@Transactional
@UseCase
public class CreatePharmacyUseCase implements CreatePharmacyByEventInputPort {
    private final SavePharmacyOutputPort savePharmacyOutputPort;
    private final FindPharmacyByIdOutputPort findPharmacyByIdOutputPort;

    @Autowired
    public CreatePharmacyUseCase(SavePharmacyOutputPort savePharmacyOutputPort, FindPharmacyByIdOutputPort findPharmacyByIdOutputPort) {
        this.savePharmacyOutputPort = savePharmacyOutputPort;
        this.findPharmacyByIdOutputPort = findPharmacyByIdOutputPort;
    }

    @Override
    public void createPharmacyByEvent(String id) throws EntityAlreadyExistsException, IllegalArgumentException {
        Optional<Pharmacy> isPharmacy = findPharmacyByIdOutputPort.findById(id);
        if (isPharmacy.isPresent()) {
            throw new EntityAlreadyExistsException(id);
        }
        Pharmacy pharmacy = Pharmacy.builder().idArea(id).build();
        savePharmacyOutputPort.save(pharmacy);
    }
}
