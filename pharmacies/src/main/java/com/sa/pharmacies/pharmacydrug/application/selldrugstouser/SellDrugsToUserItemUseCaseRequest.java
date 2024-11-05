package com.sa.pharmacies.pharmacydrug.application.selldrugstouser;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.Value;

@Value
@NoArgsConstructor(force = true)
@AllArgsConstructor
public class SellDrugsToUserItemUseCaseRequest {
    String code;
    int quantity;
}
