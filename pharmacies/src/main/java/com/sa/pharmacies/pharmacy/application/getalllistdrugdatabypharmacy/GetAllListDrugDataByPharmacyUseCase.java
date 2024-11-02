package com.sa.pharmacies.pharmacy.application.getalllistdrugdatabypharmacy;

import com.sa.pharmacies.common.annotation.UseCase;
import com.sa.pharmacies.drug.infrastructure.inputports.restapi.GetAllListDrugDataByPharmacyInputPort;
import com.sa.pharmacies.drug.infrastructure.outputports.db.FindAllDrugOutputPort;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.stream.Collectors;

@Transactional
@UseCase
public class GetAllListDrugDataByPharmacyUseCase implements GetAllListDrugDataByPharmacyInputPort {
    private final FindAllDrugOutputPort findAllDrugOutputPort;

    @Autowired
    public GetAllListDrugDataByPharmacyUseCase(FindAllDrugOutputPort findAllDrugOutputPort) {
        this.findAllDrugOutputPort = findAllDrugOutputPort;
    }

    @Override
    public List<GetAllListDrugDataByPharmacyResponse> getAll() {
        return findAllDrugOutputPort.findAllDrugs()
                .stream()
                .map(GetAllListDrugDataByPharmacyResponse ::from)
                .collect(Collectors.toList());
    }
}
