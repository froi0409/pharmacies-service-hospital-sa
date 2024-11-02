package com.sa.pharmacies.pharmacy.application.getlistdrugdatabypharmacy;

import com.netflix.discovery.converters.Auto;
import com.sa.pharmacies.common.annotation.UseCase;
import com.sa.pharmacies.drug.domain.Drug;
import com.sa.pharmacies.drug.infrastructure.outputports.db.FindAllDrugOutputPort;
import com.sa.pharmacies.pharmacy.application.getlistdrugdatabynameslike.GetItemDrugDataByNamesLikeResponse;
import com.sa.pharmacies.pharmacydrug.domain.PharmacyDrug;
import com.sa.pharmacies.pharmacydrug.infrastructure.inputports.restapi.GetListDrugDataByPharmacyInputPort;
import com.sa.pharmacies.pharmacydrug.infrastructure.outputports.db.FindPharmacyDrugByIdPharmacyOutputPort;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Transactional
@UseCase
public class GetListDrugDataByPharmacyUseCase implements GetListDrugDataByPharmacyInputPort {
    private final FindPharmacyDrugByIdPharmacyOutputPort findPharmacyDrugByIdPharmacyOutputPort;
    private final FindAllDrugOutputPort findAllDrugOutputPort;

    @Autowired
    public GetListDrugDataByPharmacyUseCase(FindPharmacyDrugByIdPharmacyOutputPort findPharmacyDrugByIdPharmacyOutputPort, FindAllDrugOutputPort findAllDrugOutputPort) {
        this.findPharmacyDrugByIdPharmacyOutputPort = findPharmacyDrugByIdPharmacyOutputPort;
        this.findAllDrugOutputPort = findAllDrugOutputPort;
    }

    @Override
    public List<GetItemDrugDataByNamesLikeResponse> getDrugsName(String idPharmacy) {
        // Obtener todos los PharmacyDrug de la farmacia específica
        List<PharmacyDrug> pharmacyDrugs = findPharmacyDrugByIdPharmacyOutputPort.findByPharmacyId(idPharmacy);
        System.out.println(pharmacyDrugs.toString());
        // Obtener la lista de IDs de medicamentos (idDrug) de los PharmacyDrug
        Set<String> drugIds = pharmacyDrugs.stream()
                .map((pharmacyDrug -> pharmacyDrug.getDrug().getCode().toString()))
                .collect(Collectors.toSet());

        // Obtener todos los Drug y filtrar solo los que coincidan con los drugIds
        List<Drug> drugs = findAllDrugOutputPort.findAllDrugs().stream()
                .filter(drug -> drugIds.contains(drug.getCode().toString()))
                .toList();

        // Mapear cada Drug a GetItemDrugDataByNamesLikeResponse
        return drugs.stream()
                .map(GetItemDrugDataByNamesLikeResponse::from)
                .collect(Collectors.toList());

    }

}
