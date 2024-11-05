package com.sa.pharmacies.pharmacy.application.getalllistdrugdatabypharmacy;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

import com.sa.pharmacies.drug.domain.Drug;
import com.sa.pharmacies.drug.infrastructure.outputports.db.FindAllDrugOutputPort;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;

public class GetAllListDrugDataByPharmacyUseCaseTest {

    @Mock
    private FindAllDrugOutputPort findAllDrugOutputPort;

    @InjectMocks
    private GetAllListDrugDataByPharmacyUseCase getAllListDrugDataByPharmacyUseCase;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testGetAll_ReturnsListOfDrugData() {
        // Configuración de una lista de objetos Drug
        List<Drug> drugList = Arrays.asList(
                Drug.builder()
                        .code(UUID.randomUUID())
                        .name("Ibuprofen")
                        .cost(10.0)
                        .minimumQuantity(1)
                        .unitPrice(12.5)
                        .build(),
                Drug.builder()
                        .code(UUID.randomUUID())
                        .name("Paracetamol")
                        .cost(5.0)
                        .minimumQuantity(2)
                        .unitPrice(6.5)
                        .build()
        );

        when(findAllDrugOutputPort.findAllDrugs()).thenReturn(drugList);

        // Invocación del caso de uso
        List<GetAllListDrugDataByPharmacyResponse> result = getAllListDrugDataByPharmacyUseCase.getAll();

        // Verificaciones
        verify(findAllDrugOutputPort, times(1)).findAllDrugs();
        assertEquals(drugList.size(), result.size());
        assertEquals(drugList.get(0).getName(), result.get(0).getName());
        assertEquals(drugList.get(1).getName(), result.get(1).getName());
    }
}
