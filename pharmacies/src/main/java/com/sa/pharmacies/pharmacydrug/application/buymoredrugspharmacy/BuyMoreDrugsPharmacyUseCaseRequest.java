package com.sa.pharmacies.pharmacydrug.application.buymoredrugspharmacy;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.Value;

import java.util.Date;

@Value
@NoArgsConstructor(force = true)
@AllArgsConstructor
public class BuyMoreDrugsPharmacyUseCaseRequest {
    Integer quantity;
    Date date;
}
