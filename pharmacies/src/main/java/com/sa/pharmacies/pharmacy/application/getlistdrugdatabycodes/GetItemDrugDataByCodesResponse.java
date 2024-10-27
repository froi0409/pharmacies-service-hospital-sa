package com.sa.pharmacies.pharmacy.application.getlistdrugdatabycodes;

import com.sa.pharmacies.drug.domain.Drug;
import lombok.Value;

@Value
public class GetItemDrugDataByCodesResponse {
    String code;
    String name;

    public static GetItemDrugDataByCodesResponse from(Drug drug){
        return new GetItemDrugDataByCodesResponse(drug.getCode().toString(),drug.getName());
    }
}
