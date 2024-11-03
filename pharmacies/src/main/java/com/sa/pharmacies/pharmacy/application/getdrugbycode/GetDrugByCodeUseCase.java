package com.sa.pharmacies.pharmacy.application.getdrugbycode;

import com.sa.pharmacies.common.annotation.UseCase;
import com.sa.pharmacies.drug.domain.Drug;
import com.sa.pharmacies.drug.infrastructure.inputports.restapi.GetDrugByCodeInputPort;
import com.sa.pharmacies.drug.infrastructure.outputports.db.FindDrugByCodeOutputPort;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;

@Transactional
@UseCase
public class GetDrugByCodeUseCase implements GetDrugByCodeInputPort {
    private final FindDrugByCodeOutputPort findDrugByCodeOutputPort;

    public GetDrugByCodeUseCase(FindDrugByCodeOutputPort findDrugByCodeOutputPort) {
        this.findDrugByCodeOutputPort = findDrugByCodeOutputPort;
    }


    @Override
    public GetDrugByCodeResponse getDrugByCode(String code) throws EntityNotFoundException {
        Drug drug = findDrugByCodeOutputPort.findByCode(code)
                .orElseThrow(() -> new EntityNotFoundException("the drug with code " + code + " was not found"));
        return GetDrugByCodeResponse.from(drug);
    }
}
