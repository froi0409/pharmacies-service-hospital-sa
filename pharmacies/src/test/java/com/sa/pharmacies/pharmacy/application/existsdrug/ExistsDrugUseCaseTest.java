package com.sa.pharmacies.pharmacy.application.existsdrug;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;

import com.sa.pharmacies.drug.domain.Drug;
import com.sa.pharmacies.drug.infrastructure.outputports.db.FindDrugByCodeOutputPort;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Optional;
import java.util.UUID;

public class ExistsDrugUseCaseTest {

    @Mock
    private FindDrugByCodeOutputPort findDrugByCodeOutputPort;

    @InjectMocks
    private ExistsDrugUseCase existsDrugUseCase;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testFindByCode_WhenDrugExists_ReturnsDrug() {
        // Configuración del objeto Drug existente
        String code = UUID.randomUUID().toString();
        Drug existingDrug = Drug.builder()
                .code(UUID.fromString(code))
                .name("Ibuprofen")
                .cost(10.0)
                .minimumQuantity(1)
                .unitPrice(12.5)
                .build();

        when(findDrugByCodeOutputPort.findByCode(code)).thenReturn(Optional.of(existingDrug));

        // Invocación del caso de uso
        Optional<Drug> result = existsDrugUseCase.findByCode(code);

        // Verificaciones
        verify(findDrugByCodeOutputPort, times(1)).findByCode(code);
        assertTrue(result.isPresent());
        assertEquals(existingDrug, result.get());
    }

    @Test
    public void testFindByCode_WhenDrugDoesNotExist_ReturnsEmptyOptional() {
        // Configuración de un código de medicamento inexistente
        String code = UUID.randomUUID().toString();

        when(findDrugByCodeOutputPort.findByCode(code)).thenReturn(Optional.empty());

        // Invocación del caso de uso
        Optional<Drug> result = existsDrugUseCase.findByCode(code);

        // Verificaciones
        verify(findDrugByCodeOutputPort, times(1)).findByCode(code);
        assertTrue(result.isEmpty());
    }
}
