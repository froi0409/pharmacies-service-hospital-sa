package com.sa.pharmacies.drug.infrastructure.inputports.restapi;

import com.sa.pharmacies.pharmacy.application.getlistdrugdatabycodes.GetItemDrugDataByCodesResponse;
import com.sa.pharmacies.pharmacy.application.getlistdrugdatabycodes.GetListDrugDataByCodesRequest;
import jakarta.persistence.EntityNotFoundException;

import java.util.List;

public interface GetListDrugDataByCodesInputPort {
    List<GetItemDrugDataByCodesResponse> getList(GetListDrugDataByCodesRequest request) throws EntityNotFoundException;
}
