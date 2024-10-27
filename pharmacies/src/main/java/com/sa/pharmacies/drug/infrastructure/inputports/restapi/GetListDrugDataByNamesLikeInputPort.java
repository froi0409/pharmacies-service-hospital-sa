package com.sa.pharmacies.drug.infrastructure.inputports.restapi;

import com.sa.pharmacies.pharmacy.application.getlistdrugdatabynameslike.GetItemDrugDataByNamesLikeResponse;

import java.util.List;

public interface GetListDrugDataByNamesLikeInputPort {
    List<GetItemDrugDataByNamesLikeResponse> getDrugNamesLike(String name);
}
