package com.sa.pharmacies.pharmacy.application.getdrugbycode;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

import com.sa.pharmacies.drug.domain.Drug;
import com.sa.pharmacies.drug.infrastructure.outputports.db.FindDrugByCodeOutputPort;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Optional;
import java.util.UUID;

public class GetDrugByCodeUseCaseTest {

    @Mock
    private FindDrugByCodeOutputPort findDrugByCodeOutputPort;

    @InjectMocks
    private GetDrugByCodeUseCase getDrugByCodeUseCase;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testGetDrugByCode_WhenDrugExists_ReturnsDrugResponse() {
        // Configuración de un medicamento existente
        String code = UUID.randomUUID().toString();
        Drug drug = Drug.builder()
                .code(UUID.fromString(code))
                .name("Ibuprofen")
                .cost(10.0)
                .minimumQuantity(1)
                .unitPrice(12.5)
                .build();

        when(findDrugByCodeOutputPort.findByCode(code)).thenReturn(Optional.of(drug));

        // Invocación del caso de uso
        GetDrugByCodeResponse result = getDrugByCodeUseCase.getDrugByCode(code);

        // Verificaciones
        verify(findDrugByCodeOutputPort, times(1)).findByCode(code);
        assertEquals(drug.getCodeString(), result.getCode());
        assertEquals(drug.getName(), result.getName());
        assertEquals(drug.getCost(), result.getCost());
        assertEquals(drug.getUnitPrice(), result.getUnitPrice());
        assertEquals(drug.getMinimumQuantity(), result.getMinimumQuantity());
    }

    @Test
    public void testGetDrugByCode_WhenDrugDoesNotExist_ThrowsEntityNotFoundException() {
        // Configuración de un código inexistente
        String code = UUID.randomUUID().toString();

        when(findDrugByCodeOutputPort.findByCode(code)).thenReturn(Optional.empty());

        // Verificación de la excepción lanzada
        assertThrows(EntityNotFoundException.class, () -> getDrugByCodeUseCase.getDrugByCode(code));

        // Verificación de que se intentó buscar el medicamento
        verify(findDrugByCodeOutputPort, times(1)).findByCode(code);
    }
}
