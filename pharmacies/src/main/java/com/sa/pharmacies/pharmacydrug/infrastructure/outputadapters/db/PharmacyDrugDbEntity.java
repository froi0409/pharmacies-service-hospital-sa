package com.sa.pharmacies.pharmacydrug.infrastructure.outputadapters.db;

import com.sa.pharmacies.common.annotation.DomainEntity;
import com.sa.pharmacies.drug.domain.Drug;
import com.sa.pharmacies.pharmacy.domain.Pharmacy;
import com.sa.pharmacies.pharmacy.infrastructure.outputadapter.db.PharmacyDbEntity;
import com.sa.pharmacies.pharmacydrug.domain.PharmacyDrug;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Setter
@Builder
@Getter
@DomainEntity
@Entity
@Table(name = "pharmacy_drug", schema = "pharmacies")
@AllArgsConstructor
public class PharmacyDrugDbEntity {
    @Id
    @Column(name = "id")
    private String id;

    @Column(name = "quantity")
    private Integer quantity;

    @Column(name = "id_pharmacy")
    private String idPharmacy;

    @Column(name = "id_drug")
    private String idDrug;


    public PharmacyDrug toDomain(Pharmacy pharmacy, Drug drug){
        return PharmacyDrug.builder()
                .id(UUID.fromString(id))
                .quantity(quantity)
                .pharmacy(pharmacy)
                .drug(drug)
                .build();
    }

    public PharmacyDrug toDomainId(){
        return PharmacyDrug.builder()
                .id(UUID.fromString(id))
                .quantity(quantity)
                .pharmacy(Pharmacy.builder().idArea(idPharmacy).build())
                .drug(Drug.builder().code(UUID.fromString(idDrug)).build())
                .build();
    }

    public static PharmacyDrugDbEntity from(PharmacyDrug pharmacyDrug){
        return PharmacyDrugDbEntity.builder()
                .id(pharmacyDrug.getId() != null ? pharmacyDrug.getId().toString() : UUID.randomUUID().toString())
                .quantity(pharmacyDrug.getQuantity())
                .idPharmacy(pharmacyDrug.getPharmacy().getIdArea())
                .idDrug(pharmacyDrug.getDrug().getCode().toString())
                .build();
    }


    public PharmacyDrugDbEntity() {

    }
}
