package com.sa.pharmacies.pharmacy.application.existspharmacy;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;

import com.sa.pharmacies.pharmacy.domain.Pharmacy;
import com.sa.pharmacies.pharmacy.infrastructure.outputports.db.FindPharmacyByIdOutputPort;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Optional;

public class ExistsPharmacyUseCaseTest {

    @Mock
    private FindPharmacyByIdOutputPort findPharmacyByIdOutputPort;

    @InjectMocks
    private ExistsPharmacyUseCase existsPharmacyUseCase;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testGetPharmacyById_WhenPharmacyExists_ReturnsPharmacy() {
        // Configuración del objeto Pharmacy existente
        String id = "Area-123";
        Pharmacy existingPharmacy = Pharmacy.builder()
                .idArea(id)
                .build();

        when(findPharmacyByIdOutputPort.findById(id)).thenReturn(Optional.of(existingPharmacy));

        // Invocación del caso de uso
        Optional<Pharmacy> result = existsPharmacyUseCase.getPharmacyById(id);

        // Verificaciones
        verify(findPharmacyByIdOutputPort, times(1)).findById(id);
        assertTrue(result.isPresent());
        assertEquals(existingPharmacy, result.get());
    }

    @Test
    public void testGetPharmacyById_WhenPharmacyDoesNotExist_ReturnsEmptyOptional() {
        // Configuración de un ID de farmacia inexistente
        String id = "NonExistentArea";

        when(findPharmacyByIdOutputPort.findById(id)).thenReturn(Optional.empty());

        // Invocación del caso de uso
        Optional<Pharmacy> result = existsPharmacyUseCase.getPharmacyById(id);

        // Verificaciones
        verify(findPharmacyByIdOutputPort, times(1)).findById(id);
        assertTrue(result.isEmpty());
    }
}
