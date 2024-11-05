package com.sa.pharmacies.pharmacy.application.getlistdrugdatabynameslike;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

import com.sa.pharmacies.drug.domain.Drug;
import com.sa.pharmacies.drug.infrastructure.outputports.db.FindDrugByNamesLikeOutputPort;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

public class GetListDrugDataByNamesLikeUseCaseTest {

    @Mock
    private FindDrugByNamesLikeOutputPort outputPort;

    @InjectMocks
    private GetListDrugDataByNamesLikeUseCase getListDrugDataByNamesLikeUseCase;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testGetDrugNamesLike_WhenDrugsAreFound_ReturnsListOfDrugData() {
        // Configuración de medicamentos con nombres similares
        String namePattern = "Ibup";
        List<Drug> foundDrugs = List.of(
                Drug.builder()
                        .code(UUID.randomUUID())
                        .name("Ibuprofen")
                        .cost(10.0)
                        .minimumQuantity(1)
                        .unitPrice(12.5)
                        .build(),
                Drug.builder()
                        .code(UUID.randomUUID())
                        .name("Ibuprof")
                        .cost(8.0)
                        .minimumQuantity(1)
                        .unitPrice(10.0)
                        .build()
        );

        when(outputPort.findByNamesLike(namePattern)).thenReturn(foundDrugs);

        // Invocación del caso de uso
        List<GetItemDrugDataByNamesLikeResponse> result = getListDrugDataByNamesLikeUseCase.getDrugNamesLike(namePattern);

        // Verificaciones
        verify(outputPort, times(1)).findByNamesLike(namePattern);
        assertEquals(foundDrugs.size(), result.size());
        assertEquals(foundDrugs.get(0).getName(), result.get(0).getName());
        assertEquals(foundDrugs.get(1).getName(), result.get(1).getName());
    }

    @Test
    public void testGetDrugNamesLike_WhenNoDrugsFound_ReturnsEmptyList() {
        // Configuración de un patrón de nombre que no tiene coincidencias
        String namePattern = "NonExistentDrug";

        when(outputPort.findByNamesLike(namePattern)).thenReturn(List.of());

        // Invocación del caso de uso
        List<GetItemDrugDataByNamesLikeResponse> result = getListDrugDataByNamesLikeUseCase.getDrugNamesLike(namePattern);

        // Verificaciones
        verify(outputPort, times(1)).findByNamesLike(namePattern);
        assertEquals(0, result.size());
    }
}
