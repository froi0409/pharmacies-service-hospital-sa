package com.sa.pharmacies.pharmacy.application.getalllistdrugdatabypharmacy;

import com.sa.pharmacies.drug.domain.Drug;
import lombok.Value;

@Value
public class GetAllListDrugDataByPharmacyResponse {
    String code;
    String name;
    double cost;
    double unitPrice;
    int minimumQuantity;

    public static GetAllListDrugDataByPharmacyResponse from(Drug drug){
        return new GetAllListDrugDataByPharmacyResponse(drug.getCodeString(), drug.getName(), drug.getCost()
                ,drug.getUnitPrice(), drug.getMinimumQuantity());
    }
}
