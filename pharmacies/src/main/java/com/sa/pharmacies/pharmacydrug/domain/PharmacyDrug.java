package com.sa.pharmacies.pharmacydrug.domain;

import com.sa.pharmacies.common.annotation.DomainEntity;
import com.sa.pharmacies.drug.domain.Drug;
import com.sa.pharmacies.pharmacy.domain.Pharmacy;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
@Builder
@DomainEntity
public class PharmacyDrug {
    private UUID id;
    private Integer quantity;
    private Pharmacy pharmacy;
    private Drug drug;

    public boolean validateQuantity() {
        return quantity != null && quantity > 0;
    }
}
