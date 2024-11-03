package com.sa.pharmacies.pharmacydrug.application.removequantityfromhospitals;

import lombok.NoArgsConstructor;
import lombok.Value;

@Value
@NoArgsConstructor(force=true)
public class RemoveQuantityFromHospitalsRequest {
    String code;
    String idPharmacy;
    int quantity;

}
