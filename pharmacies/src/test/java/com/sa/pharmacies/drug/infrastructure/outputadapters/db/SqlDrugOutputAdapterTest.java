package com.sa.pharmacies.drug.infrastructure.outputadapters.db;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.mockito.Mockito.*;

import com.sa.pharmacies.drug.domain.Drug;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

public class SqlDrugOutputAdapterTest {

    @Mock
    private JpaDrugDbRepository jpaRepository;

    @InjectMocks
    private SqlDrugOutputAdapter adapter;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testSave() {
        Drug drug = Drug.builder()
                .code(UUID.randomUUID())
                .name("Ibuprofen")
                .cost(10.0)
                .minimumQuantity(1)
                .unitPrice(12.5)
                .build();

        DrugDbEntity drugDbEntity = DrugDbEntity.from(drug);
        when(jpaRepository.save(any(DrugDbEntity.class))).thenReturn(drugDbEntity);

        Drug savedDrug = adapter.save(drug);

        verify(jpaRepository, times(1)).save(any(DrugDbEntity.class));
        assertEquals(drug.getName(), savedDrug.getName());
        assertEquals(drug.getCost(), savedDrug.getCost());
        assertEquals(drug.getMinimumQuantity(), savedDrug.getMinimumQuantity());
        assertEquals(drug.getUnitPrice(), savedDrug.getUnitPrice());
    }

    @Test
    public void testFindByCode() {
        String code = UUID.randomUUID().toString();
        DrugDbEntity drugDbEntity = DrugDbEntity.builder()
                .code(code)
                .name("Paracetamol")
                .cost(5.0)
                .minimumQuantity(2)
                .unitPrice(6.5)
                .build();

        when(jpaRepository.findById(code)).thenReturn(Optional.of(drugDbEntity));

        Optional<Drug> foundDrug = adapter.findByCode(code);

        verify(jpaRepository, times(1)).findById(code);
        assertTrue(foundDrug.isPresent());
        assertEquals(drugDbEntity.getName(), foundDrug.get().getName());
    }

    @Test
    public void testFindByName() {
        String name = "Aspirin";
        DrugDbEntity drugDbEntity = DrugDbEntity.builder()
                .code(UUID.randomUUID().toString())
                .name(name)
                .cost(3.0)
                .minimumQuantity(1)
                .unitPrice(4.0)
                .build();

        when(jpaRepository.findByName(name)).thenReturn(Optional.of(drugDbEntity));

        Optional<Drug> foundDrug = adapter.findByName(name);

        verify(jpaRepository, times(1)).findByName(name);
        assertTrue(foundDrug.isPresent());
        assertEquals(drugDbEntity.getName(), foundDrug.get().getName());
    }

    @Test
    public void testFindAllDrugs() {
        List<DrugDbEntity> drugDbEntities = Arrays.asList(
                DrugDbEntity.builder().code(UUID.randomUUID().toString()).name("Drug1").cost(5.0).minimumQuantity(1).unitPrice(5.5).build(),
                DrugDbEntity.builder().code(UUID.randomUUID().toString()).name("Drug2").cost(7.0).minimumQuantity(1).unitPrice(8.5).build()
        );

        when(jpaRepository.findAll()).thenReturn(drugDbEntities);

        List<Drug> drugs = adapter.findAllDrugs();

        verify(jpaRepository, times(1)).findAll();
        assertEquals(drugDbEntities.size(), drugs.size());
        assertEquals(drugDbEntities.get(0).getName(), drugs.get(0).getName());
    }

    @Test
    public void testFindByCodes() {
        List<String> codes = Arrays.asList(UUID.randomUUID().toString(), UUID.randomUUID().toString());
        List<DrugDbEntity> drugDbEntities = codes.stream()
                .map(code -> DrugDbEntity.builder().code(code).name("Drug" + code).cost(10.0).minimumQuantity(1).unitPrice(12.0).build())
                .collect(Collectors.toList());

        when(jpaRepository.findByCodes(codes)).thenReturn(drugDbEntities);

        List<Drug> drugs = adapter.findByCodes(codes);

        verify(jpaRepository, times(1)).findByCodes(codes);
        assertEquals(drugDbEntities.size(), drugs.size());
        assertEquals(drugDbEntities.get(0).getName(), drugs.get(0).getName());
    }

    @Test
    public void testFindByNames() {
        List<String> names = Arrays.asList("Ibuprofen", "Paracetamol");
        List<DrugDbEntity> drugDbEntities = names.stream()
                .map(name -> DrugDbEntity.builder().code(UUID.randomUUID().toString()).name(name).cost(8.0).minimumQuantity(2).unitPrice(9.0).build())
                .collect(Collectors.toList());

        when(jpaRepository.findByNames(names)).thenReturn(drugDbEntities);

        List<Drug> drugs = adapter.findByNames(names);

        verify(jpaRepository, times(1)).findByNames(names);
        assertEquals(drugDbEntities.size(), drugs.size());
        assertEquals(drugDbEntities.get(0).getName(), drugs.get(0).getName());
    }

    @Test
    public void testFindByNamesLike() {
        String namePattern = "Ibu";
        List<DrugDbEntity> drugDbEntities = Arrays.asList(
                DrugDbEntity.builder().code(UUID.randomUUID().toString()).name("Ibuprofen").cost(10.0).minimumQuantity(1).unitPrice(12.5).build(),
                DrugDbEntity.builder().code(UUID.randomUUID().toString()).name("Ibuprof").cost(10.0).minimumQuantity(1).unitPrice(12.5).build()
        );

        when(jpaRepository.findByNamesLike(namePattern)).thenReturn(drugDbEntities);

        List<Drug> drugs = adapter.findByNamesLike(namePattern);

        verify(jpaRepository, times(1)).findByNamesLike(namePattern);
        assertEquals(drugDbEntities.size(), drugs.size());
        assertTrue(drugs.stream().allMatch(drug -> drug.getName().contains(namePattern)));
    }
}
