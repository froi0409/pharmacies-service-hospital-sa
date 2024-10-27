package com.sa.pharmacies.pharmacy.application.getlistdrugdatabynameslike;


import com.sa.pharmacies.drug.domain.Drug;
import com.sa.pharmacies.pharmacy.application.getlistdrugdatabycodes.GetItemDrugDataByCodesResponse;
import lombok.Value;

@Value
public class GetItemDrugDataByNamesLikeResponse {
    String code;
    String name;

    public static GetItemDrugDataByNamesLikeResponse from(Drug drug){
        return new GetItemDrugDataByNamesLikeResponse(drug.getCode().toString(),drug.getName());
    }
}
