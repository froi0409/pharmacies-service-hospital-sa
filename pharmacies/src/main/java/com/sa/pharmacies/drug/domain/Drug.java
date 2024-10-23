package com.sa.pharmacies.drug.domain;

import com.sa.pharmacies.common.annotation.DomainEntity;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
@Builder
@DomainEntity
public class Drug {
    private UUID code;
    private String name;
    private double cost;
    private Integer minimumQuantity;
    private double unitPrice;

    public boolean validateCost(){
        return cost > 0;
    }

    public boolean validateMinimumQuantity(){
        return minimumQuantity > 0;
    }

    public boolean validateUnitPrice(){
        return unitPrice > 0;
    }
}


