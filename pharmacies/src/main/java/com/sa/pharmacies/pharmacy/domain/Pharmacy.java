package com.sa.pharmacies.pharmacy.domain;

import com.sa.pharmacies.common.annotation.DomainEntity;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
@DomainEntity
public class Pharmacy {
    private String idArea;
}
