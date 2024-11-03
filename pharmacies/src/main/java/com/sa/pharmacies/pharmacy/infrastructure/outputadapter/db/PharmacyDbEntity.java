package com.sa.pharmacies.pharmacy.infrastructure.outputadapter.db;

import com.sa.pharmacies.common.annotation.DomainEntity;
import com.sa.pharmacies.pharmacy.domain.Pharmacy;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.*;

@Getter
@Setter
@Builder
@DomainEntity
@Entity
@Table(name = "pharmacy", schema = "pharmacies")
@AllArgsConstructor
@NoArgsConstructor
public class PharmacyDbEntity {
    @Id
    @Column(name = "id_area")
    private String idArea;

    public Pharmacy toDomain(){
        return Pharmacy.builder()
                .idArea(idArea)
                .build();
    }

    public static PharmacyDbEntity fromDomain(Pharmacy pharmacy){
        return PharmacyDbEntity.builder()
                .idArea(pharmacy.getIdArea())
                .build();
    }
}
