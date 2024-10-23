package com.sa.pharmacies.drug.infrastructure.outputadapters.db;

import com.sa.pharmacies.common.annotation.DomainEntity;
import com.sa.pharmacies.drug.domain.Drug;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.*;

import java.util.UUID;

@Getter
@Setter
@Builder
@DomainEntity
@Entity
@Table(name = "drug", schema = "pharmacies")
@AllArgsConstructor
@NoArgsConstructor
public class DrugDbEntity {
    @Id
    @Column(name = "code")
    private String code;
    @Column(name = "name")
    private String name;
    @Column(name = "cost")
    private double cost;
    @Column(name = "minimun")
    private Integer minimumQuantity;
    @Column(name = "unit_price")
    private double unitPrice;

    public Drug toDomain(){
        return Drug.builder()
                .code(UUID.fromString(code))
                .name(name)
                .cost(cost)
                .minimumQuantity(minimumQuantity)
                .unitPrice(unitPrice)
                .build();
    }

    public static DrugDbEntity from(Drug drug){
        return DrugDbEntity.builder()
                .code(drug.getCode() != null ? drug.getCode().toString() : UUID.randomUUID().toString())
                .name(drug.getName())
                .cost(drug.getCost())
                .minimumQuantity(drug.getMinimumQuantity())
                .unitPrice(drug.getUnitPrice())
                .build();
    }
}
