package com.sa.pharmacies.pharmacydrug.application.selldrugstouser;

import lombok.NoArgsConstructor;
import lombok.Value;

@Value
@NoArgsConstructor(force = true)
public class SellDrugsToUserItemUseCaseRequest {
    String code;
    int quantity;
}
