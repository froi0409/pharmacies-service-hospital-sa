package com.sa.pharmacies.pharmacy.application.getlistdrugdatabypharmacy;

import com.sa.pharmacies.drug.domain.Drug;
import lombok.Value;

@Value
public class GetListDrugDataByPharmacyResponse {
    String code;
    String name;

    public static GetListDrugDataByPharmacyResponse from(Drug drug){
        return new GetListDrugDataByPharmacyResponse(drug.getCode().toString(), drug.getName());
    }
}
