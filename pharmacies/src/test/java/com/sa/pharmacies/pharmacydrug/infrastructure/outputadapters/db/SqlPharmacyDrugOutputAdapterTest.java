package com.sa.pharmacies.pharmacydrug.infrastructure.outputadapters.db;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import com.sa.pharmacies.drug.domain.Drug;
import com.sa.pharmacies.pharmacy.domain.Pharmacy;
import com.sa.pharmacies.pharmacydrug.domain.PharmacyDrug;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class SqlPharmacyDrugOutputAdapterTest {

    @Mock
    private JpaPharmacyDrugRepository jpaPharmacyDrugRepository;

    @InjectMocks
    private SqlPharmacyDrugOutputAdapter sqlPharmacyDrugOutputAdapter;

    private Pharmacy pharmacy;
    private Drug drug;
    private PharmacyDrug pharmacyDrug;
    private PharmacyDrugDbEntity pharmacyDrugDbEntity;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);

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
    public void testSave() {
        when(jpaPharmacyDrugRepository.save(any(PharmacyDrugDbEntity.class))).thenReturn(pharmacyDrugDbEntity);

        PharmacyDrug savedPharmacyDrug = sqlPharmacyDrugOutputAdapter.save(pharmacyDrug);

        verify(jpaPharmacyDrugRepository, times(1)).save(any(PharmacyDrugDbEntity.class));
        assertEquals(pharmacyDrug.getQuantity(), savedPharmacyDrug.getQuantity());
        assertEquals(pharmacyDrug.getPharmacy().getIdArea(), savedPharmacyDrug.getPharmacy().getIdArea());
        assertEquals(pharmacyDrug.getDrug().getCode(), savedPharmacyDrug.getDrug().getCode());
    }

    @Test
    public void testFindByIdAndCode() {
        String idPharmacy = pharmacy.getIdArea();
        String codeDrug = drug.getCode().toString();

        when(jpaPharmacyDrugRepository.findByIdPharmacyAndIdDrug(idPharmacy, codeDrug)).thenReturn(Optional.of(pharmacyDrugDbEntity));

        Optional<PharmacyDrug> result = sqlPharmacyDrugOutputAdapter.findByIdAndCode(idPharmacy, codeDrug);

        verify(jpaPharmacyDrugRepository, times(1)).findByIdPharmacyAndIdDrug(idPharmacy, codeDrug);
        assertTrue(result.isPresent());
        assertEquals(pharmacyDrug.getQuantity(), result.get().getQuantity());
    }

    @Test
    public void testFindByIdAndCodeObject() {
        when(jpaPharmacyDrugRepository.findByIdPharmacyAndIdDrug(pharmacy.getIdArea(), drug.getCode().toString()))
                .thenReturn(Optional.of(pharmacyDrugDbEntity));

        Optional<PharmacyDrug> result = sqlPharmacyDrugOutputAdapter.findByIdAndCodeObject(pharmacy, drug);

        verify(jpaPharmacyDrugRepository, times(1)).findByIdPharmacyAndIdDrug(pharmacy.getIdArea(), drug.getCode().toString());
        assertTrue(result.isPresent());
        assertEquals(pharmacyDrug.getQuantity(), result.get().getQuantity());
    }

    @Test
    public void testUpdatePharmacyDrug() {
        when(jpaPharmacyDrugRepository.findById(pharmacyDrug.getId().toString())).thenReturn(Optional.of(pharmacyDrugDbEntity));

        pharmacyDrug.setQuantity(15); // Actualizamos la cantidad

        sqlPharmacyDrugOutputAdapter.updatePharmacyDrug(pharmacyDrug);

        verify(jpaPharmacyDrugRepository, times(1)).save(any(PharmacyDrugDbEntity.class));
        assertEquals(15, pharmacyDrug.getQuantity());
    }

    @Test
    public void testFindByPharmacyId() {
        String idPharmacy = pharmacy.getIdArea();
        when(jpaPharmacyDrugRepository.findByPharmacyId(idPharmacy)).thenReturn(List.of(pharmacyDrugDbEntity));

        List<PharmacyDrug> result = sqlPharmacyDrugOutputAdapter.findByPharmacyId(idPharmacy);

        verify(jpaPharmacyDrugRepository, times(1)).findByPharmacyId(idPharmacy);
        assertEquals(1, result.size());
        assertEquals(pharmacyDrug.getQuantity(), result.get(0).getQuantity());
    }
}
