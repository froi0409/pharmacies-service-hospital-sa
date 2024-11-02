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
import java.util.Map;
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
    public List<GetListDrugDataByPharmacyResponse> getDrugsName(String idPharmacy) {
        // Obtener todos los PharmacyDrug de la farmacia específica
        List<PharmacyDrug> pharmacyDrugs = findPharmacyDrugByIdPharmacyOutputPort.findByPharmacyId(idPharmacy);

        // Crear un Map entre el código de Drug y la cantidad (quantity) de PharmacyDrug
        Map<String, Integer> drugStockMap = pharmacyDrugs.stream()
                .collect(Collectors.toMap(
                        pharmacyDrug -> pharmacyDrug.getDrug().getCode().toString(),
                        PharmacyDrug::getQuantity
                ));

        // Obtener todos los Drug y filtrar solo los que coincidan con los drugIds
        List<Drug> drugs = findAllDrugOutputPort.findAllDrugs().stream()
                .filter(drug -> drugStockMap.containsKey(drug.getCode().toString()))
                .toList();

        // Mapear cada Drug a GetListDrugDataByPharmacyResponse, usando el stock del Map
        return drugs.stream()
                .map(drug -> GetListDrugDataByPharmacyResponse.from(
                        drug,
                        drugStockMap.getOrDefault(drug.getCode().toString(), 0)  // Obtener el stock desde el Map
                ))
                .collect(Collectors.toList());
    }

}
