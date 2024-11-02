package com.sa.pharmacies.pharmacy.application.getlistdrugdatabypharmacy;

import com.sa.pharmacies.drug.domain.Drug;
import lombok.Value;

@Value
public class GetListDrugDataByPharmacyResponse {
    String code;
    String name;
    double cost;
    double unitPrice;
    int stock;

    public static GetListDrugDataByPharmacyResponse from(Drug drug, int stock){
        return new GetListDrugDataByPharmacyResponse(drug.getCodeString(), drug.getName(), drug.getCost()
                ,drug.getUnitPrice(), stock);
    }
}
