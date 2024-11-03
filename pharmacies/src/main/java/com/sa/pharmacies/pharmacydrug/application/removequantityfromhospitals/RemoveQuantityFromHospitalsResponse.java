package com.sa.pharmacies.pharmacydrug.application.removequantityfromhospitals;

import com.sa.pharmacies.drug.domain.Drug;
import com.sa.pharmacies.pharmacydrug.domain.PharmacyDrug;
import lombok.Value;

@Value
public class RemoveQuantityFromHospitalsResponse {
    String code;
    double unitPrice;
    double cost;

    public static RemoveQuantityFromHospitalsResponse from(Drug drug) {
        return new RemoveQuantityFromHospitalsResponse(
                drug.getCodeString(),
                drug.getUnitPrice(),
                drug.getCost()
        );
    }
}
