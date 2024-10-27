package com.sa.pharmacies.pharmacy.application.getlistdrugdatabycodes;

import com.sa.pharmacies.common.annotation.UseCase;
import com.sa.pharmacies.drug.domain.Drug;
import com.sa.pharmacies.drug.infrastructure.inputports.restapi.GetListDrugDataByCodesInputPort;
import com.sa.pharmacies.drug.infrastructure.outputports.db.FindAllDrugOutputPort;
import com.sa.pharmacies.drug.infrastructure.outputports.db.FindDrugByCodesOutputPort;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Transactional
@UseCase
public class GetListDrugDataByCodesUseCase implements GetListDrugDataByCodesInputPort {
    private final FindDrugByCodesOutputPort findDrugByCodesOutputPort;

    @Autowired
    public GetListDrugDataByCodesUseCase(FindDrugByCodesOutputPort findDrugByCodesOutputPort) {
        this.findDrugByCodesOutputPort = findDrugByCodesOutputPort;
    }

    @Override
    public List<GetItemDrugDataByCodesResponse> getList(GetListDrugDataByCodesRequest request) throws EntityNotFoundException {
        List<String> codes = request.getCodes();
        // Buscamos todos los medicamentos que coinciden con los códigos proporcionados
        List<Drug> foundDrugs = findDrugByCodesOutputPort.findByCodes(codes);

        // Verificamos si hay algún código que no haya sido encontrado
        Set<String> foundCodes = foundDrugs.stream()
                .map(Drug::getCodeString)
                .collect(Collectors.toSet());

        codes.removeAll(foundCodes);
        if (!codes.isEmpty()) {
            throw new EntityNotFoundException("No se encontraron medicamentos para los códigos: " + String.join(", ", codes));
        }

        // Convertimos la lista de dominio a respuestas DTO
        return foundDrugs.stream()
                .map(GetItemDrugDataByCodesResponse::from)
                .collect(Collectors.toList());
    }
}
