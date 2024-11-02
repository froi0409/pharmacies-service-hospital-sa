package com.sa.pharmacies.pharmacydrug.infrastructure.inputports.restapi;

import com.sa.pharmacies.pharmacy.application.getlistdrugdatabynameslike.GetItemDrugDataByNamesLikeResponse;

import java.util.List;

public interface GetListDrugDataByPharmacyInputPort {
    List<GetItemDrugDataByNamesLikeResponse> getDrugsName(String idPharmacy);
}
