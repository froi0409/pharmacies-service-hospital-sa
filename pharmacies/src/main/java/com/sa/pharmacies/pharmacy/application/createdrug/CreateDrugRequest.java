package com.sa.pharmacies.pharmacy.application.createdrug;

import com.sa.pharmacies.drug.domain.Drug;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.Value;

@Value
@NoArgsConstructor(force = true)
@AllArgsConstructor
public class CreateDrugRequest {
    String name;
    double cost;
    Integer minimumQuantity;
    double unitPrice;

    public Drug toDomain(){
        return Drug.builder()
                .name(name)
                .cost(cost)
                .minimumQuantity(minimumQuantity)
                .unitPrice(unitPrice)
                .build();
    }
}
