package com.sa.pharmacies.drug.infrastructure.outputadapters.db;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import com.sa.pharmacies.drug.domain.Drug;
import org.junit.jupiter.api.Test;

import java.util.UUID;

public class DrugDbEntityTest {

    @Test
    public void testToDomain() {
        // Configuración del objeto DrugDbEntity
        DrugDbEntity drugDbEntity = DrugDbEntity.builder()
                .code(UUID.randomUUID().toString())
                .name("Ibuprofen")
                .cost(10.0)
                .minimumQuantity(1)
                .unitPrice(12.5)
                .build();

        // Conversión a objeto Drug (dominio)
        Drug drug = drugDbEntity.toDomain();

        // Verificaciones
        assertNotNull(drug);
        assertEquals(drugDbEntity.getCode(), drug.getCode().toString());
        assertEquals(drugDbEntity.getName(), drug.getName());
        assertEquals(drugDbEntity.getCost(), drug.getCost());
        assertEquals(drugDbEntity.getMinimumQuantity(), drug.getMinimumQuantity());
        assertEquals(drugDbEntity.getUnitPrice(), drug.getUnitPrice());
    }

    @Test
    public void testFromDomain() {
        // Configuración del objeto Drug (dominio)
        Drug drug = Drug.builder()
                .code(UUID.randomUUID())
                .name("Paracetamol")
                .cost(5.0)
                .minimumQuantity(2)
                .unitPrice(6.5)
                .build();

        // Conversión a DrugDbEntity
        DrugDbEntity drugDbEntity = DrugDbEntity.from(drug);

        // Verificaciones
        assertNotNull(drugDbEntity);
        assertEquals(drug.getCode().toString(), drugDbEntity.getCode());
        assertEquals(drug.getName(), drugDbEntity.getName());
        assertEquals(drug.getCost(), drugDbEntity.getCost());
        assertEquals(drug.getMinimumQuantity(), drugDbEntity.getMinimumQuantity());
        assertEquals(drug.getUnitPrice(), drugDbEntity.getUnitPrice());
    }

    @Test
    public void testFromDomainWithNullCode() {
        // Configuración del objeto Drug con código nulo
        Drug drug = Drug.builder()
                .name("Aspirin")
                .cost(8.0)
                .minimumQuantity(1)
                .unitPrice(9.5)
                .build();

        // Conversión a DrugDbEntity (se generará un nuevo código UUID)
        DrugDbEntity drugDbEntity = DrugDbEntity.from(drug);

        // Verificaciones
        assertNotNull(drugDbEntity);
        assertNotNull(drugDbEntity.getCode());  // Debe generarse un UUID
        assertEquals(drug.getName(), drugDbEntity.getName());
        assertEquals(drug.getCost(), drugDbEntity.getCost());
        assertEquals(drug.getMinimumQuantity(), drugDbEntity.getMinimumQuantity());
        assertEquals(drug.getUnitPrice(), drugDbEntity.getUnitPrice());
    }
}
