package com.sa.pharmacies.pharmacydrug.infrastructure.outputadapters.db;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import com.sa.pharmacies.drug.domain.Drug;
import com.sa.pharmacies.pharmacy.domain.Pharmacy;
import com.sa.pharmacies.pharmacydrug.domain.PharmacyDrug;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.UUID;

public class PharmacyDrugDbEntityTest {

    private Pharmacy pharmacy;
    private Drug drug;
    private PharmacyDrug pharmacyDrug;
    private PharmacyDrugDbEntity pharmacyDrugDbEntity;

    @BeforeEach
    public void setup() {
        pharmacy = Pharmacy.builder().idArea("Pharmacy-123").build();
        drug = Drug.builder().code(UUID.randomUUID()).build();
        pharmacyDrug = PharmacyDrug.builder()
                .id(UUID.randomUUID())
                .quantity(10)
                .pharmacy(pharmacy)
                .drug(drug)
                .build();

        pharmacyDrugDbEntity = PharmacyDrugDbEntity.from(pharmacyDrug);
    }

    @Test
    public void testToDomain() {
        PharmacyDrug domainObject = pharmacyDrugDbEntity.toDomain(pharmacy, drug);

        assertNotNull(domainObject);
        assertEquals(pharmacyDrugDbEntity.getId(), domainObject.getId().toString());
        assertEquals(pharmacyDrugDbEntity.getQuantity(), domainObject.getQuantity());
        assertEquals(pharmacyDrugDbEntity.getIdPharmacy(), domainObject.getPharmacy().getIdArea());
        assertEquals(pharmacyDrugDbEntity.getIdDrug(), domainObject.getDrug().getCode().toString());
    }

    @Test
    public void testToDomainId() {
        PharmacyDrug domainObject = pharmacyDrugDbEntity.toDomainId();

        assertNotNull(domainObject);
        assertEquals(pharmacyDrugDbEntity.getId(), domainObject.getId().toString());
        assertEquals(pharmacyDrugDbEntity.getQuantity(), domainObject.getQuantity());
        assertEquals(pharmacyDrugDbEntity.getIdPharmacy(), domainObject.getPharmacy().getIdArea());
        assertEquals(pharmacyDrugDbEntity.getIdDrug(), domainObject.getDrug().getCode().toString());
    }

    @Test
    public void testFrom() {
        PharmacyDrugDbEntity entity = PharmacyDrugDbEntity.from(pharmacyDrug);

        assertNotNull(entity);
        assertEquals(pharmacyDrug.getId().toString(), entity.getId());
        assertEquals(pharmacyDrug.getQuantity(), entity.getQuantity());
        assertEquals(pharmacyDrug.getPharmacy().getIdArea(), entity.getIdPharmacy());
        assertEquals(pharmacyDrug.getDrug().getCode().toString(), entity.getIdDrug());
    }
}
