package com.sa.pharmacies.pharmacy.infrastructure.outputadapter.db;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import com.sa.pharmacies.pharmacy.domain.Pharmacy;
import org.junit.jupiter.api.Test;

public class PharmacyDbEntityTest {

    @Test
    public void testToDomain() {
        // Configuración del objeto PharmacyDbEntity
        PharmacyDbEntity pharmacyDbEntity = PharmacyDbEntity.builder()
                .idArea("Area-123")
                .build();

        // Conversión a objeto Pharmacy (dominio)
        Pharmacy pharmacy = pharmacyDbEntity.toDomain();

        // Verificaciones
        assertNotNull(pharmacy);
        assertEquals(pharmacyDbEntity.getIdArea(), pharmacy.getIdArea());
    }

    @Test
    public void testFromDomain() {
        // Configuración del objeto Pharmacy (dominio)
        Pharmacy pharmacy = Pharmacy.builder()
                .idArea("Area-123")
                .build();

        // Conversión a PharmacyDbEntity
        PharmacyDbEntity pharmacyDbEntity = PharmacyDbEntity.fromDomain(pharmacy);

        // Verificaciones
        assertNotNull(pharmacyDbEntity);
        assertEquals(pharmacy.getIdArea(), pharmacyDbEntity.getIdArea());
    }
}
