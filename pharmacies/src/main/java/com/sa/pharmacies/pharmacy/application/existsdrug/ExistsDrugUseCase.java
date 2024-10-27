package com.sa.pharmacies.pharmacy.application.existsdrug;

import com.sa.pharmacies.common.annotation.UseCase;
import com.sa.pharmacies.drug.domain.Drug;
import com.sa.pharmacies.drug.infrastructure.inputports.restapi.ExistsDrugInputPort;
import com.sa.pharmacies.drug.infrastructure.outputports.db.FindDrugByCodeOutputPort;
import jakarta.transaction.Transactional;

import java.util.Optional;

@Transactional
@UseCase
public class ExistsDrugUseCase implements ExistsDrugInputPort {
    private final FindDrugByCodeOutputPort findDrugByCodeOutputPort;

    public ExistsDrugUseCase(FindDrugByCodeOutputPort findDrugByCodeOutputPort) {
        this.findDrugByCodeOutputPort = findDrugByCodeOutputPort;
    }

    @Override
    public Optional<Drug> findByCode(String code) {
        return findDrugByCodeOutputPort.findByCode(code);
    }
}
