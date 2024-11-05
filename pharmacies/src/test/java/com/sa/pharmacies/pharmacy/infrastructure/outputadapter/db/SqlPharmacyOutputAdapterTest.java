package com.sa.pharmacies.pharmacy.infrastructure.outputadapter.db;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;

import com.sa.pharmacies.pharmacy.domain.Pharmacy;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Optional;

public class SqlPharmacyOutputAdapterTest {

    @Mock
    private JpaPharmacyDbRepository jpaPharmacyDbRepository;

    @InjectMocks
    private SqlPharmacyOutputAdapter adapter;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testSave() {
        // Configuración de un objeto Pharmacy
        Pharmacy pharmacy = Pharmacy.builder()
                .idArea("Area-123")
                .build();

        // Invocación del método save
        adapter.save(pharmacy);

        // Verificación de que el repositorio guarde la entidad PharmacyDbEntity
        verify(jpaPharmacyDbRepository, times(1)).save(any(PharmacyDbEntity.class));
    }

    @Test
    public void testFindById() {
        // Configuración de un objeto PharmacyDbEntity
        String id = "Area-123";
        PharmacyDbEntity pharmacyDbEntity = PharmacyDbEntity.builder()
                .idArea(id)
                .build();

        when(jpaPharmacyDbRepository.findById(id)).thenReturn(Optional.of(pharmacyDbEntity));

        // Invocación del método findById
        Optional<Pharmacy> foundPharmacy = adapter.findById(id);

        // Verificaciones
        verify(jpaPharmacyDbRepository, times(1)).findById(id);
        assertTrue(foundPharmacy.isPresent());
        assertEquals(pharmacyDbEntity.getIdArea(), foundPharmacy.get().getIdArea());
    }

    @Test
    public void testFindByIdNotFound() {
        // Configuración de un ID que no existe en el repositorio
        String id = "NonExistentId";
        when(jpaPharmacyDbRepository.findById(id)).thenReturn(Optional.empty());

        // Invocación del método findById
        Optional<Pharmacy> foundPharmacy = adapter.findById(id);

        // Verificaciones
        verify(jpaPharmacyDbRepository, times(1)).findById(id);
        assertTrue(foundPharmacy.isEmpty());
    }
}
