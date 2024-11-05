package com.sa.pharmacies.pharmacy.application.getlistdrugdatabycodes;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

import com.sa.pharmacies.drug.domain.Drug;
import com.sa.pharmacies.drug.infrastructure.outputports.db.FindDrugByCodesOutputPort;
import jakarta.persistence.EntityNotFoundException;
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

public class GetListDrugDataByCodesUseCaseTest {

    @Mock
    private FindDrugByCodesOutputPort findDrugByCodesOutputPort;

    @InjectMocks
    private GetListDrugDataByCodesUseCase getListDrugDataByCodesUseCase;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testGetList_WhenNoCodesAreFound_ThrowsEntityNotFoundException() {
        // Configuración de códigos que no existen
        List<String> codes = Arrays.asList(UUID.randomUUID().toString(), UUID.randomUUID().toString());
        GetListDrugDataByCodesRequest request = new GetListDrugDataByCodesRequest(codes);

        when(findDrugByCodesOutputPort.findByCodes(codes)).thenReturn(List.of());

        // Verificación de la excepción lanzada
        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class, () -> getListDrugDataByCodesUseCase.getList(request));
        assertEquals("No se encontraron medicamentos para los códigos: " + String.join(", ", codes), exception.getMessage());

        // Verificación de la llamada al método
        verify(findDrugByCodesOutputPort, times(1)).findByCodes(codes);
    }
}
