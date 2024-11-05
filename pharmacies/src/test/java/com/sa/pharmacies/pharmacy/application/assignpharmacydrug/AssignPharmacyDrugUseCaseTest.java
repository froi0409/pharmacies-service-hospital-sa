package com.sa.pharmacies.pharmacy.application.assignpharmacydrug;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.sa.pharmacies.common.exceptions.EntityAlreadyExistsException;
import com.sa.pharmacies.drug.domain.Drug;
import com.sa.pharmacies.drug.infrastructure.outputports.db.FindDrugByCodeOutputPort;
import com.sa.pharmacies.pharmacy.domain.Pharmacy;
import com.sa.pharmacies.pharmacy.infrastructure.outputports.db.FindPharmacyByIdOutputPort;
import com.sa.pharmacies.pharmacydrug.domain.PharmacyDrug;
import com.sa.pharmacies.pharmacydrug.infrastructure.outputports.db.FindPharmacyDrugOutputPort;
import com.sa.pharmacies.pharmacydrug.infrastructure.outputports.db.SavePharmacyDrugOutputPort;
import com.sa.pharmacies.pharmacydrug.infrastructure.outputports.kafka.PaymentDrugProducerOutputPort;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Optional;
import java.util.UUID;

public class AssignPharmacyDrugUseCaseTest {

    @Mock
    private FindPharmacyByIdOutputPort findPharmacyByIdOutputPort;

    @Mock
    private FindDrugByCodeOutputPort findDrugByCodeOutputPort;

    @Mock
    private FindPharmacyDrugOutputPort findPharmacyDrugOutputPort;

    @Mock
    private SavePharmacyDrugOutputPort savePharmacyDrugOutputPort;

    @Mock
    private PaymentDrugProducerOutputPort paymentDrugProducerOutputPort;

    @InjectMocks
    private AssignPharmacyDrugUseCase assignPharmacyDrugUseCase;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testAssign_WhenPharmacyDrugAlreadyExists_ThrowsEntityAlreadyExistsException() throws JsonProcessingException {
        // Configuración de farmacia y medicamento existente
        String idPharmacy = "Pharmacy-123";
        String codeDrug = UUID.randomUUID().toString();
        AssignPharmacyDrugRequest request = new AssignPharmacyDrugRequest(10, idPharmacy, codeDrug);

        PharmacyDrug existingPharmacyDrug = PharmacyDrug.builder().build();

        when(findPharmacyDrugOutputPort.findByIdAndCode(idPharmacy, codeDrug)).thenReturn(Optional.of(existingPharmacyDrug));

        // Verificación de la excepción lanzada
        assertThrows(IllegalArgumentException.class, () -> assignPharmacyDrugUseCase.assign(request));

        // Verificar que no se intenta guardar ni enviar eventos
        verify(savePharmacyDrugOutputPort, never()).save(any(PharmacyDrug.class));
        verify(paymentDrugProducerOutputPort, never()).sendPaymentDrugEvent(any());
    }

    @Test
    public void testAssign_WhenPharmacyNotFound_ThrowsIllegalArgumentException() throws JsonProcessingException {
        // Configuración de farmacia inexistente
        String idPharmacy = "Pharmacy-123";
        String codeDrug = UUID.randomUUID().toString();
        AssignPharmacyDrugRequest request = new AssignPharmacyDrugRequest(10, idPharmacy, codeDrug);

        when(findPharmacyByIdOutputPort.findById(idPharmacy)).thenReturn(Optional.empty());

        // Verificación de la excepción lanzada
        assertThrows(IllegalArgumentException.class, () -> assignPharmacyDrugUseCase.assign(request));

        // Verificación de que no se intenta guardar ni enviar eventos
        verify(savePharmacyDrugOutputPort, never()).save(any(PharmacyDrug.class));
        verify(paymentDrugProducerOutputPort, never()).sendPaymentDrugEvent(any());
    }

    @Test
    public void testAssign_WhenDrugNotFound_ThrowsIllegalArgumentException() throws JsonProcessingException {
        // Configuración de medicamento inexistente
        String idPharmacy = "Pharmacy-123";
        String codeDrug = UUID.randomUUID().toString();
        AssignPharmacyDrugRequest request = new AssignPharmacyDrugRequest(10, idPharmacy, codeDrug);

        Pharmacy pharmacy = Pharmacy.builder().idArea(idPharmacy).build();
        when(findPharmacyByIdOutputPort.findById(idPharmacy)).thenReturn(Optional.of(pharmacy));
        when(findDrugByCodeOutputPort.findByCode(codeDrug)).thenReturn(Optional.empty());

        // Verificación de la excepción lanzada
        assertThrows(IllegalArgumentException.class, () -> assignPharmacyDrugUseCase.assign(request));

        // Verificación de que no se intenta guardar ni enviar eventos
        verify(savePharmacyDrugOutputPort, never()).save(any(PharmacyDrug.class));
        verify(paymentDrugProducerOutputPort, never()).sendPaymentDrugEvent(any());
    }

    @Test
    public void testAssign_WhenValidRequest_SavesPharmacyDrugAndSendsPaymentEvent() throws JsonProcessingException, EntityAlreadyExistsException {
        // Configuración de farmacia y medicamento válidos
        String idPharmacy = "Pharmacy-123";
        String codeDrug = UUID.randomUUID().toString();
        AssignPharmacyDrugRequest request = new AssignPharmacyDrugRequest(10, idPharmacy, codeDrug);

        Pharmacy pharmacy = Pharmacy.builder().idArea(idPharmacy).build();
        Drug drug = Drug.builder()
                .code(UUID.fromString(codeDrug))
                .name("Ibuprofen")
                .cost(10.0)
                .build();

        when(findPharmacyByIdOutputPort.findById(idPharmacy)).thenReturn(Optional.of(pharmacy));
        when(findDrugByCodeOutputPort.findByCode(codeDrug)).thenReturn(Optional.of(drug));
        when(findPharmacyDrugOutputPort.findByIdAndCode(idPharmacy, codeDrug)).thenReturn(Optional.empty());

        // Invocación del caso de uso
        assignPharmacyDrugUseCase.assign(request);

        // Verificación de que se guarda el medicamento en la farmacia
        verify(savePharmacyDrugOutputPort, times(1)).save(any(PharmacyDrug.class));

        // Verificación de que se envía el evento de pago
        double expectedAmount = request.getQuantity() * drug.getCost();
        verify(paymentDrugProducerOutputPort, times(1)).sendPaymentDrugEvent(argThat(paymentRequest ->
                paymentRequest.getAmount() == expectedAmount &&
                        paymentRequest.getIdDrug().equals(drug.getCode().toString())
        ));
    }
}
