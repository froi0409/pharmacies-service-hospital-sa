package com.sa.pharmacies.drug.infrastructure.inputports.restapi;


import com.sa.pharmacies.pharmacy.application.getalllistdrugdatabypharmacy.GetAllListDrugDataByPharmacyResponse;

import java.util.List;

public interface GetAllListDrugDataByPharmacyInputPort {
    List<GetAllListDrugDataByPharmacyResponse> getAll();
}
