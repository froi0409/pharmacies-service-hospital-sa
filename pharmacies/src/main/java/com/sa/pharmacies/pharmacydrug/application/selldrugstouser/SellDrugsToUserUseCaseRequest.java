package com.sa.pharmacies.pharmacydrug.application.selldrugstouser;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.Value;

import java.time.LocalDate;
import java.util.List;

@Value
@NoArgsConstructor(force = true)
public class SellDrugsToUserUseCaseRequest {
    LocalDate date;
    double totalCost;
    List<SellDrugsToUserItemUseCaseRequest> pharmacyDescriptionRequest;
}
