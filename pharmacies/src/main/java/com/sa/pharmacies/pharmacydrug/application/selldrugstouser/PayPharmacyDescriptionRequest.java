package com.sa.pharmacies.pharmacydrug.application.selldrugstouser;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.Value;

@Data
public class PayPharmacyDescriptionRequest {
    String idProduct;
    int quantity;
    double unitPrice;
    double unitCost;
}
