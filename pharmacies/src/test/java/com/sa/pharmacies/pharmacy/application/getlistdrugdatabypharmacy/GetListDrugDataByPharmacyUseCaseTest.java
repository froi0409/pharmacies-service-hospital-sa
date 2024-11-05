package com.sa.pharmacies.pharmacy.application.getlistdrugdatabypharmacy;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

import com.sa.pharmacies.drug.domain.Drug;
import com.sa.pharmacies.drug.infrastructure.outputports.db.FindAllDrugOutputPort;
import com.sa.pharmacies.pharmacydrug.domain.PharmacyDrug;
import com.sa.pharmacies.pharmacydrug.infrastructure.outputports.db.FindPharmacyDrugByIdPharmacyOutputPort;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

public class GetListDrugDataByPharmacyUseCaseTest {

    @Mock
    private FindPharmacyDrugByIdPharmacyOutputPort findPharmacyDrugByIdPharmacyOutputPort;

    @Mock
    private FindAllDrugOutputPort findAllDrugOutputPort;

    @InjectMocks
    private GetListDrugDataByPharmacyUseCase getListDrugDataByPharmacyUseCase;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testGetDrugsName_WhenDrugsAreAvailableInPharmacy_ReturnsListWithStock() {
        // Configuración de farmacia, medicamentos y stock
        String idPharmacy = "Pharmacy-123";
        Drug drug1 = Drug.builder()
                .code(UUID.randomUUID())
                .name("Ibuprofen")
                .cost(10.0)
                .minimumQuantity(1)
                .unitPrice(12.5)
                .build();
        Drug drug2 = Drug.builder()
                .code(UUID.randomUUID())
                .name("Paracetamol")
                .cost(5.0)
                .minimumQuantity(1)
                .unitPrice(6.5)
                .build();

        PharmacyDrug pharmacyDrug1 = PharmacyDrug.builder()
                .drug(drug1)
                .quantity(100)
                .build();
        PharmacyDrug pharmacyDrug2 = PharmacyDrug.builder()
                .drug(drug2)
                .quantity(200)
                .build();

        List<PharmacyDrug> pharmacyDrugs = List.of(pharmacyDrug1, pharmacyDrug2);
        List<Drug> allDrugs = List.of(drug1, drug2);

        when(findPharmacyDrugByIdPharmacyOutputPort.findByPharmacyId(idPharmacy)).thenReturn(pharmacyDrugs);
        when(findAllDrugOutputPort.findAllDrugs()).thenReturn(allDrugs);

        // Invocación del caso de uso
        List<GetListDrugDataByPharmacyResponse> result = getListDrugDataByPharmacyUseCase.getDrugsName(idPharmacy);

        // Verificaciones
        verify(findPharmacyDrugByIdPharmacyOutputPort, times(1)).findByPharmacyId(idPharmacy);
        verify(findAllDrugOutputPort, times(1)).findAllDrugs();
        assertEquals(2, result.size());

        // Validación del stock y datos de los medicamentos
        Map<String, Integer> expectedStockMap = Map.of(
                drug1.getCodeString(), pharmacyDrug1.getQuantity(),
                drug2.getCodeString(), pharmacyDrug2.getQuantity()
        );

        result.forEach(response -> {
            assertEquals(expectedStockMap.get(response.getCode()), response.getStock());
        });
    }

    @Test
    public void testGetDrugsName_WhenNoDrugsAvailableInPharmacy_ReturnsEmptyList() {
        // Configuración para una farmacia sin medicamentos
        String idPharmacy = "Pharmacy-456";

        when(findPharmacyDrugByIdPharmacyOutputPort.findByPharmacyId(idPharmacy)).thenReturn(List.of());
        when(findAllDrugOutputPort.findAllDrugs()).thenReturn(List.of());

        // Invocación del caso de uso
        List<GetListDrugDataByPharmacyResponse> result = getListDrugDataByPharmacyUseCase.getDrugsName(idPharmacy);

        // Verificaciones
        verify(findPharmacyDrugByIdPharmacyOutputPort, times(1)).findByPharmacyId(idPharmacy);
        verify(findAllDrugOutputPort, times(1)).findAllDrugs();
        assertEquals(0, result.size());
    }
}
