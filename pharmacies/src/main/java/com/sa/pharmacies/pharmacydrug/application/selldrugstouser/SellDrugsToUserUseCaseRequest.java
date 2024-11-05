package com.sa.pharmacies.pharmacydrug.application.selldrugstouser;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.Value;

import java.time.LocalDate;
import java.util.List;

@Value
@NoArgsConstructor(force = true)
@AllArgsConstructor
public class SellDrugsToUserUseCaseRequest {
    LocalDate date;
    double totalCost;
    List<SellDrugsToUserItemUseCaseRequest> pharmacyDescriptionRequest;
}
