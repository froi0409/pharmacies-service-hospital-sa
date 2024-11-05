package com.sa.pharmacies.pharmacydrug.application.removequantityfromhospitals;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.Value;

@Value
@NoArgsConstructor(force=true)
@AllArgsConstructor
public class RemoveQuantityFromHospitalsRequest {
    String code;
    String idPharmacy;
    int quantity;

}
