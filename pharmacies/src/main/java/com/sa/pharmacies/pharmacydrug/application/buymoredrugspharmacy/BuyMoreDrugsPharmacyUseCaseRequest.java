package com.sa.pharmacies.pharmacydrug.application.buymoredrugspharmacy;

import lombok.NoArgsConstructor;
import lombok.Value;

import java.util.Date;

@Value
@NoArgsConstructor(force = true)
public class BuyMoreDrugsPharmacyUseCaseRequest {
    Integer quantity;
    Date date;
}
