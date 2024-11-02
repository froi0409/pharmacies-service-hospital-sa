package com.sa.pharmacies.pharmacydrug.application.selldrugstouser;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.Value;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;

@Data
public class PayPharmacyRequest {
    LocalDate date;
    double totalCost;
    List<PayPharmacyDescriptionRequest> pharmacyDescriptionRequest;
}
