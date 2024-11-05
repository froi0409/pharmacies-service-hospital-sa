package com.sa.pharmacies.pharmacydrug.application.buymoredrugspharmacy;


import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.sa.pharmacies.drug.domain.Drug;
import com.sa.pharmacies.drug.infrastructure.outputports.db.FindDrugByCodeOutputPort;
import com.sa.pharmacies.pharmacy.domain.Pharmacy;
import com.sa.pharmacies.pharmacy.infrastructure.outputports.db.FindPharmacyByIdOutputPort;
import com.sa.pharmacies.pharmacydrug.domain.PharmacyDrug;
import com.sa.pharmacies.pharmacydrug.infrastructure.outputports.db.FindPharmacyDrugOutputPort;
import com.sa.pharmacies.pharmacydrug.infrastructure.outputports.db.UpdatePharmacyDrugOutputPort;
import com.sa.pharmacies.pharmacydrug.infrastructure.outputports.kafka.PaymentDrugProducerOutputPort;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Date;
import java.util.Optional;
import java.util.UUID;

public class BuyMoreDrugsPharmacyUseCaseTest {

    @Mock
    private FindPharmacyByIdOutputPort findPharmacyByIdOutputPort;

    @Mock
    private FindDrugByCodeOutputPort findDrugByCodeOutputPort;

    @Mock
    private FindPharmacyDrugOutputPort findPharmacyDrugOutputPort;

    @Mock
    private UpdatePharmacyDrugOutputPort updatePharmacyDrugOutputPort;

    @Mock
    private PaymentDrugProducerOutputPort paymentDrugProducerOutputPort;

    @InjectMocks
    private BuyMoreDrugsPharmacyUseCase buyMoreDrugsPharmacyUseCase;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testBuyMoreDrugsPharmacy_WhenPharmacyNotFound_ThrowsEntityNotFoundException() throws JsonProcessingException {
        // Configuración de farmacia inexistente
        String idPharmacy = "Pharmacy-123";
        String codeDrug = UUID.randomUUID().toString();
        BuyMoreDrugsPharmacyUseCaseRequest request = new BuyMoreDrugsPharmacyUseCaseRequest(10, new Date());

        when(findPharmacyByIdOutputPort.findById(idPharmacy)).thenReturn(Optional.empty());

        // Verificación de la excepción lanzada
        assertThrows(EntityNotFoundException.class, () -> buyMoreDrugsPharmacyUseCase.buyMoreDrugsPharmacy(idPharmacy, codeDrug, request));

        // Verificación de que no se intenta actualizar ni enviar eventos
        verify(updatePharmacyDrugOutputPort, never()).updatePharmacyDrug(any(PharmacyDrug.class));
        verify(paymentDrugProducerOutputPort, never()).sendPaymentDrugEvent(any());
    }

    @Test
    public void testBuyMoreDrugsPharmacy_WhenDrugNotFound_ThrowsEntityNotFoundException() throws JsonProcessingException {
        // Configuración de medicamento inexistente
        String idPharmacy = "Pharmacy-123";
        String codeDrug = UUID.randomUUID().toString();
        BuyMoreDrugsPharmacyUseCaseRequest request = new BuyMoreDrugsPharmacyUseCaseRequest(10, new Date());

        Pharmacy pharmacy = Pharmacy.builder().idArea(idPharmacy).build();
        when(findPharmacyByIdOutputPort.findById(idPharmacy)).thenReturn(Optional.of(pharmacy));
        when(findDrugByCodeOutputPort.findByCode(codeDrug)).thenReturn(Optional.empty());

        // Verificación de la excepción lanzada
        assertThrows(EntityNotFoundException.class, () -> buyMoreDrugsPharmacyUseCase.buyMoreDrugsPharmacy(idPharmacy, codeDrug, request));

        // Verificación de que no se intenta actualizar ni enviar eventos
        verify(updatePharmacyDrugOutputPort, never()).updatePharmacyDrug(any(PharmacyDrug.class));
        verify(paymentDrugProducerOutputPort, never()).sendPaymentDrugEvent(any());
    }

    @Test
    public void testBuyMoreDrugsPharmacy_WhenPharmacyDrugNotFound_ThrowsEntityNotFoundException() throws JsonProcessingException {
        // Configuración de relación entre farmacia y medicamento inexistente
        String idPharmacy = "Pharmacy-123";
        String codeDrug = UUID.randomUUID().toString();
        BuyMoreDrugsPharmacyUseCaseRequest request = new BuyMoreDrugsPharmacyUseCaseRequest(10, new Date());

        Pharmacy pharmacy = Pharmacy.builder().idArea(idPharmacy).build();
        Drug drug = Drug.builder().code(UUID.fromString(codeDrug)).name("Ibuprofen").cost(10.0).build();

        when(findPharmacyByIdOutputPort.findById(idPharmacy)).thenReturn(Optional.of(pharmacy));
        when(findDrugByCodeOutputPort.findByCode(codeDrug)).thenReturn(Optional.of(drug));
        when(findPharmacyDrugOutputPort.findByIdAndCodeObject(pharmacy, drug)).thenReturn(Optional.empty());

        // Verificación de la excepción lanzada
        assertThrows(EntityNotFoundException.class, () -> buyMoreDrugsPharmacyUseCase.buyMoreDrugsPharmacy(idPharmacy, codeDrug, request));

        // Verificación de que no se intenta actualizar ni enviar eventos
        verify(updatePharmacyDrugOutputPort, never()).updatePharmacyDrug(any(PharmacyDrug.class));
        verify(paymentDrugProducerOutputPort, never()).sendPaymentDrugEvent(any());
    }

    @Test
    public void testBuyMoreDrugsPharmacy_WhenValidRequest_UpdatesPharmacyDrugAndSendsPaymentEvent() throws JsonProcessingException {
        // Configuración de farmacia, medicamento y relación válidos
        String idPharmacy = "Pharmacy-123";
        String codeDrug = UUID.randomUUID().toString();
        Date date = new Date();
        BuyMoreDrugsPharmacyUseCaseRequest request = new BuyMoreDrugsPharmacyUseCaseRequest(10, date);

        Pharmacy pharmacy = Pharmacy.builder().idArea(idPharmacy).build();
        Drug drug = Drug.builder().code(UUID.fromString(codeDrug)).name("Ibuprofen").cost(10.0).build();
        PharmacyDrug pharmacyDrug = PharmacyDrug.builder().pharmacy(pharmacy).drug(drug).quantity(5).build();

        when(findPharmacyByIdOutputPort.findById(idPharmacy)).thenReturn(Optional.of(pharmacy));
        when(findDrugByCodeOutputPort.findByCode(codeDrug)).thenReturn(Optional.of(drug));
        when(findPharmacyDrugOutputPort.findByIdAndCodeObject(pharmacy, drug)).thenReturn(Optional.of(pharmacyDrug));

        // Invocación del caso de uso
        buyMoreDrugsPharmacyUseCase.buyMoreDrugsPharmacy(idPharmacy, codeDrug, request);

        // Verificación de actualización del PharmacyDrug
        verify(updatePharmacyDrugOutputPort, times(1)).updatePharmacyDrug(any(PharmacyDrug.class));
        assertEquals(15, pharmacyDrug.getQuantity()); // Validar la nueva cantidad

        // Verificación del envío del evento de pago
        double expectedAmount = request.getQuantity() * drug.getCost();
        verify(paymentDrugProducerOutputPort, times(1)).sendPaymentDrugEvent(argThat(paymentRequest ->
                paymentRequest.getAmount().equals(expectedAmount) &&
                        paymentRequest.getIdDrug().equals(codeDrug)
        ));
    }
}
