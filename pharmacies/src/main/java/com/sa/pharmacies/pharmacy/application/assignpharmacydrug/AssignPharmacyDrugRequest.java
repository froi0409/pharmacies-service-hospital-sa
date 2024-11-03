package com.sa.pharmacies.pharmacy.application.assignpharmacydrug;


import com.sa.pharmacies.pharmacydrug.domain.PharmacyDrug;
import lombok.NoArgsConstructor;
import lombok.Value;

@Value
@NoArgsConstructor(force = true)
public class AssignPharmacyDrugRequest {
    Integer quantity;
    String idPharmacy;
    String codeDrug;

    public PharmacyDrug toDomain(){
        return PharmacyDrug.builder()
                .quantity(quantity)
                .build();
    }
}
