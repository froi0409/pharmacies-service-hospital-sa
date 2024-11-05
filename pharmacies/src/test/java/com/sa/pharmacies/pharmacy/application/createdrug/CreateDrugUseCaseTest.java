package com.sa.pharmacies.pharmacy.application.createdrug;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

import com.sa.pharmacies.common.exceptions.EntityAlreadyExistsException;
import com.sa.pharmacies.drug.domain.Drug;
import com.sa.pharmacies.drug.infrastructure.outputports.db.FindDrugByNameOutputPort;
import com.sa.pharmacies.drug.infrastructure.outputports.db.SaveDrugOutputPort;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Optional;
import java.util.UUID;

public class CreateDrugUseCaseTest {

    @Mock
    private FindDrugByNameOutputPort findDrugByNameOutputPort;

    @Mock
    private SaveDrugOutputPort saveDrugOutputPort;

    @InjectMocks
    private CreateDrugUseCase createDrugUseCase;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testCreateDrug_WhenDrugAlreadyExists_ThrowsEntityAlreadyExistsException() {
        // Configuración del objeto Drug existente
        CreateDrugRequest request = new CreateDrugRequest("Ibuprofen", 10.0, 1, 12.5);
        Drug existingDrug = request.toDomain();

        when(findDrugByNameOutputPort.findByName(request.getName())).thenReturn(Optional.of(existingDrug));

        // Verificación de la excepción lanzada
        assertThrows(EntityAlreadyExistsException.class, () -> createDrugUseCase.createDrug(request));

        // Verificar que no se intentó guardar el medicamento
        verify(saveDrugOutputPort, never()).save(any(Drug.class));
    }

    @Test
    public void testCreateDrug_WhenCostIsInvalid_ThrowsIllegalArgumentException() {
        // Configuración del objeto Drug con costo no válido
        CreateDrugRequest request = new CreateDrugRequest("Ibuprofen", -10.0, 1, 12.5);

        // Verificación de la excepción lanzada
        assertThrows(IllegalArgumentException.class, () -> createDrugUseCase.createDrug(request));

        // Verificar que no se intentó buscar o guardar el medicamento
        verify(findDrugByNameOutputPort, never()).findByName(anyString());
        verify(saveDrugOutputPort, never()).save(any(Drug.class));
    }

    @Test
    public void testCreateDrug_WhenMinimumQuantityIsInvalid_ThrowsIllegalArgumentException() {
        // Configuración del objeto Drug con cantidad mínima no válida
        CreateDrugRequest request = new CreateDrugRequest("Ibuprofen", 10.0, 0, 12.5);

        // Verificación de la excepción lanzada
        assertThrows(IllegalArgumentException.class, () -> createDrugUseCase.createDrug(request));

        // Verificar que no se intentó buscar o guardar el medicamento
        verify(findDrugByNameOutputPort, never()).findByName(anyString());
        verify(saveDrugOutputPort, never()).save(any(Drug.class));
    }

    @Test
    public void testCreateDrug_WhenUnitPriceIsInvalid_ThrowsIllegalArgumentException() {
        // Configuración del objeto Drug con precio unitario no válido
        CreateDrugRequest request = new CreateDrugRequest("Ibuprofen", 10.0, 1, 0.0);

        // Verificación de la excepción lanzada
        assertThrows(IllegalArgumentException.class, () -> createDrugUseCase.createDrug(request));

        // Verificar que no se intentó buscar o guardar el medicamento
        verify(findDrugByNameOutputPort, never()).findByName(anyString());
        verify(saveDrugOutputPort, never()).save(any(Drug.class));
    }
}
