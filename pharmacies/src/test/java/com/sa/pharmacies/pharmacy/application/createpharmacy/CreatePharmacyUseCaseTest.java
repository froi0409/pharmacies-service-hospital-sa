package com.sa.pharmacies.pharmacy.application.createpharmacy;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

import com.sa.pharmacies.common.exceptions.EntityAlreadyExistsException;
import com.sa.pharmacies.pharmacy.domain.Pharmacy;
import com.sa.pharmacies.pharmacy.infrastructure.outputports.db.FindPharmacyByIdOutputPort;
import com.sa.pharmacies.pharmacy.infrastructure.outputports.db.SavePharmacyOutputPort;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Optional;

public class CreatePharmacyUseCaseTest {

    @Mock
    private SavePharmacyOutputPort savePharmacyOutputPort;

    @Mock
    private FindPharmacyByIdOutputPort findPharmacyByIdOutputPort;

    @InjectMocks
    private CreatePharmacyUseCase createPharmacyUseCase;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testCreatePharmacyByEvent_WhenPharmacyAlreadyExists_ThrowsEntityAlreadyExistsException() {
        // Configuración del ID existente
        String id = "Area-123";
        Pharmacy existingPharmacy = Pharmacy.builder().idArea(id).build();

        when(findPharmacyByIdOutputPort.findById(id)).thenReturn(Optional.of(existingPharmacy));

        // Verificación de la excepción lanzada
        assertThrows(EntityAlreadyExistsException.class, () -> createPharmacyUseCase.createPharmacyByEvent(id));

        // Verificar que no se intentó guardar la farmacia
        verify(savePharmacyOutputPort, never()).save(any(Pharmacy.class));
    }

    @Test
    public void testCreatePharmacyByEvent_WhenPharmacyDoesNotExist_SavesPharmacy() throws EntityAlreadyExistsException {
        // Configuración del ID inexistente
        String id = "Area-456";

        when(findPharmacyByIdOutputPort.findById(id)).thenReturn(Optional.empty());

        // Invocación del caso de uso
        createPharmacyUseCase.createPharmacyByEvent(id);

        // Verificación de que se guarda la farmacia
        verify(savePharmacyOutputPort, times(1)).save(any(Pharmacy.class));
    }
}
