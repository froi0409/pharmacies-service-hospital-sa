package com.sa.pharmacies.pharmacy.application.getdrugbycode;

import com.sa.pharmacies.drug.domain.Drug;
import lombok.NoArgsConstructor;
import lombok.Value;

@Value
public class GetDrugByCodeResponse {
    String code;
    String name;
    double cost;
    double unitPrice;
    int minimumQuantity;


    public static GetDrugByCodeResponse from(Drug drug){
        return new GetDrugByCodeResponse(drug.getCodeString(), drug.getName(), drug.getCost()
        ,drug.getUnitPrice(), drug.getMinimumQuantity());
    }
}
