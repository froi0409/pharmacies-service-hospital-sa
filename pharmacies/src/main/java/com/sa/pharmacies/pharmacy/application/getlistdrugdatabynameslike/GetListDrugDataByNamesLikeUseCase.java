package com.sa.pharmacies.pharmacy.application.getlistdrugdatabynameslike;

import com.sa.pharmacies.common.annotation.UseCase;
import com.sa.pharmacies.drug.domain.Drug;
import com.sa.pharmacies.drug.infrastructure.inputports.restapi.GetListDrugDataByNamesLikeInputPort;
import com.sa.pharmacies.drug.infrastructure.outputports.db.FindDrugByNamesLikeOutputPort;
import com.sa.pharmacies.pharmacy.application.getlistdrugdatabycodes.GetItemDrugDataByCodesResponse;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.stream.Collectors;

@Transactional
@UseCase
public class GetListDrugDataByNamesLikeUseCase implements GetListDrugDataByNamesLikeInputPort {
    private final FindDrugByNamesLikeOutputPort outputPort;

    @Autowired
    public GetListDrugDataByNamesLikeUseCase(FindDrugByNamesLikeOutputPort outputPort) {
        this.outputPort = outputPort;
    }

    @Override
    public List<GetItemDrugDataByNamesLikeResponse> getDrugNamesLike(String name) {
        List<Drug> foundDrugs = outputPort.findByNamesLike(name);

        return foundDrugs.stream()
                .map(GetItemDrugDataByNamesLikeResponse::from)
                .collect(Collectors.toList());
    }
}
