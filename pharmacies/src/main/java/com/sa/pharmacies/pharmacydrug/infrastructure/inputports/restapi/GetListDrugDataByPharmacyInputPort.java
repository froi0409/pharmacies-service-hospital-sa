package com.sa.pharmacies.pharmacydrug.infrastructure.inputports.restapi;

import com.sa.pharmacies.pharmacy.application.getlistdrugdatabynameslike.GetItemDrugDataByNamesLikeResponse;
import com.sa.pharmacies.pharmacy.application.getlistdrugdatabypharmacy.GetListDrugDataByPharmacyResponse;

import java.util.List;

public interface GetListDrugDataByPharmacyInputPort {
    List<GetListDrugDataByPharmacyResponse> getDrugsName(String idPharmacy);
}
