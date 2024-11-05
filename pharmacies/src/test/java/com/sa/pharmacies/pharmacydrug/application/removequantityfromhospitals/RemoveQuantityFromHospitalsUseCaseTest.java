package com.sa.pharmacies.pharmacydrug.application.removequantityfromhospitals;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

import com.sa.pharmacies.drug.domain.Drug;
import com.sa.pharmacies.drug.infrastructure.outputports.db.FindDrugByCodeOutputPort;
import com.sa.pharmacies.pharmacy.domain.Pharmacy;
import com.sa.pharmacies.pharmacy.infrastructure.outputports.db.FindPharmacyByIdOutputPort;
import com.sa.pharmacies.pharmacydrug.domain.PharmacyDrug;
import com.sa.pharmacies.pharmacydrug.infrastructure.outputports.db.FindPharmacyDrugOutputPort;
import com.sa.pharmacies.pharmacydrug.infrastructure.outputports.db.UpdatePharmacyDrugOutputPort;
import com.sa.pharmacies.pharmacydrug.infrastructure.outputports.kafka.SendMinimumQuantityProducerOutputPort;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class RemoveQuantityFromHospitalsUseCaseTest {

    @Mock
    private FindPharmacyByIdOutputPort findPharmacyByIdOutputPort;

    @Mock
    private FindDrugByCodeOutputPort findDrugByCodeOutputPort;

    @Mock
    private FindPharmacyDrugOutputPort findPharmacyDrugOutputPort;

    @Mock
    private UpdatePharmacyDrugOutputPort updatePharmacyDrugOutputPort;

    @Mock
    private SendMinimumQuantityProducerOutputPort sendMinimumQuantityProducerOutputPort;

    @InjectMocks
    private RemoveQuantityFromHospitalsUseCase removeQuantityFromHospitalsUseCase;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testRemove_WhenPharmacyNotFound_ThrowsEntityNotFoundException() {
        // Configuración de farmacia inexistente
        String idPharmacy = "Pharmacy-123";
        String codeDrug = UUID.randomUUID().toString();
        RemoveQuantityFromHospitalsRequest request = new RemoveQuantityFromHospitalsRequest(codeDrug, idPharmacy, 5);

        when(findPharmacyByIdOutputPort.findById(idPharmacy)).thenReturn(Optional.empty());

        // Verificación de la excepción lanzada
        assertThrows(EntityNotFoundException.class, () -> removeQuantityFromHospitalsUseCase.remove(List.of(request)));

        verify(updatePharmacyDrugOutputPort, never()).updatePharmacyDrug(any(PharmacyDrug.class));
    }

    @Test
    public void testRemove_WhenDrugNotFound_ThrowsEntityNotFoundException() {
        // Configuración de medicamento inexistente
        String idPharmacy = "Pharmacy-123";
        String codeDrug = UUID.randomUUID().toString();
        RemoveQuantityFromHospitalsRequest request = new RemoveQuantityFromHospitalsRequest(codeDrug, idPharmacy, 5);

        Pharmacy pharmacy = Pharmacy.builder().idArea(idPharmacy).build();
        when(findPharmacyByIdOutputPort.findById(idPharmacy)).thenReturn(Optional.of(pharmacy));
        when(findDrugByCodeOutputPort.findByCode(codeDrug)).thenReturn(Optional.empty());

        // Verificación de la excepción lanzada
        assertThrows(EntityNotFoundException.class, () -> removeQuantityFromHospitalsUseCase.remove(List.of(request)));

        verify(updatePharmacyDrugOutputPort, never()).updatePharmacyDrug(any(PharmacyDrug.class));
    }

    @Test
    public void testRemove_WhenInsufficientQuantity_ThrowsIllegalArgumentException() {
        // Configuración de cantidad insuficiente
        String idPharmacy = "Pharmacy-123";
        String codeDrug = UUID.randomUUID().toString();
        RemoveQuantityFromHospitalsRequest request = new RemoveQuantityFromHospitalsRequest(codeDrug, idPharmacy, 10);

        Pharmacy pharmacy = Pharmacy.builder().idArea(idPharmacy).build();
        Drug drug = Drug.builder().code(UUID.fromString(codeDrug)).name("Ibuprofen").minimumQuantity(2).build();
        PharmacyDrug pharmacyDrug = PharmacyDrug.builder().pharmacy(pharmacy).drug(drug).quantity(5).build();

        when(findPharmacyByIdOutputPort.findById(idPharmacy)).thenReturn(Optional.of(pharmacy));
        when(findDrugByCodeOutputPort.findByCode(codeDrug)).thenReturn(Optional.of(drug));
        when(findPharmacyDrugOutputPort.findByIdAndCodeObject(pharmacy, drug)).thenReturn(Optional.of(pharmacyDrug));

        // Verificación de la excepción lanzada
        assertThrows(IllegalArgumentException.class, () -> removeQuantityFromHospitalsUseCase.remove(List.of(request)));

        verify(updatePharmacyDrugOutputPort, never()).updatePharmacyDrug(any(PharmacyDrug.class));
    }

    @Test
    public void testRemove_WhenQuantityFallsBelowMinimum_SendsNotification() {
        // Configuración de cantidad que cae por debajo del mínimo
        String idPharmacy = "Pharmacy-123";
        String codeDrug = UUID.randomUUID().toString();
        RemoveQuantityFromHospitalsRequest request = new RemoveQuantityFromHospitalsRequest(codeDrug, idPharmacy, 5);

        Pharmacy pharmacy = Pharmacy.builder().idArea(idPharmacy).build();
        Drug drug = Drug.builder().code(UUID.fromString(codeDrug)).name("Ibuprofen").minimumQuantity(3).build();
        PharmacyDrug pharmacyDrug = PharmacyDrug.builder().pharmacy(pharmacy).drug(drug).quantity(7).build();

        when(findPharmacyByIdOutputPort.findById(idPharmacy)).thenReturn(Optional.of(pharmacy));
        when(findDrugByCodeOutputPort.findByCode(codeDrug)).thenReturn(Optional.of(drug));
        when(findPharmacyDrugOutputPort.findByIdAndCodeObject(pharmacy, drug)).thenReturn(Optional.of(pharmacyDrug));

        // Invocación del caso de uso
        removeQuantityFromHospitalsUseCase.remove(List.of(request));

        // Verificación de actualización y envío de notificación
        verify(updatePharmacyDrugOutputPort, times(1)).updatePharmacyDrug(pharmacyDrug);
        verify(sendMinimumQuantityProducerOutputPort, times(1)).sendNotification(eq(pharmacy.getIdArea()), anyString());
    }

    @Test
    public void testRemove_WhenValidRequest_UpdatesQuantityWithoutNotification() {
        // Configuración de solicitud válida sin caída por debajo del mínimo
        String idPharmacy = "Pharmacy-123";
        String codeDrug = UUID.randomUUID().toString();
        RemoveQuantityFromHospitalsRequest request = new RemoveQuantityFromHospitalsRequest(codeDrug, idPharmacy, 2);

        Pharmacy pharmacy = Pharmacy.builder().idArea(idPharmacy).build();
        Drug drug = Drug.builder().code(UUID.fromString(codeDrug)).name("Ibuprofen").minimumQuantity(3).build();
        PharmacyDrug pharmacyDrug = PharmacyDrug.builder().pharmacy(pharmacy).drug(drug).quantity(10).build();

        when(findPharmacyByIdOutputPort.findById(idPharmacy)).thenReturn(Optional.of(pharmacy));
        when(findDrugByCodeOutputPort.findByCode(codeDrug)).thenReturn(Optional.of(drug));
        when(findPharmacyDrugOutputPort.findByIdAndCodeObject(pharmacy, drug)).thenReturn(Optional.of(pharmacyDrug));

        // Invocación del caso de uso
        removeQuantityFromHospitalsUseCase.remove(List.of(request));

        // Verificación de actualización sin notificación
        verify(updatePharmacyDrugOutputPort, times(1)).updatePharmacyDrug(pharmacyDrug);
        verify(sendMinimumQuantityProducerOutputPort, never()).sendNotification(anyString(), anyString());
    }
}
