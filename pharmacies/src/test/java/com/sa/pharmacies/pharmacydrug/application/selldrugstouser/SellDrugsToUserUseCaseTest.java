package com.sa.pharmacies.pharmacydrug.application.selldrugstouser;

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
import com.sa.pharmacies.pharmacydrug.infrastructure.outputports.restapi.BillPharmacyDrugOutputPort;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class SellDrugsToUserUseCaseTest {

    @Mock
    private FindPharmacyByIdOutputPort findPharmacyByIdOutputPort;

    @Mock
    private FindDrugByCodeOutputPort findDrugByCodeOutputPort;

    @Mock
    private FindPharmacyDrugOutputPort findPharmacyDrugOutputPort;

    @Mock
    private UpdatePharmacyDrugOutputPort updatePharmacyDrugOutputPort;

    @Mock
    private BillPharmacyDrugOutputPort billPharmacyDrugOutputPort;

    @Mock
    private SendMinimumQuantityProducerOutputPort sendMinimumQuantityProducerOutputPort;

    @InjectMocks
    private SellDrugsToUserUseCase sellDrugsToUserUseCase;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testSellDrugs_WhenPharmacyNotFound_ThrowsEntityNotFoundException() {
        String idPharmacy = "Pharmacy-123";
        String idUser = "User-1";
        String idEmployee = "Employee-1";
        SellDrugsToUserUseCaseRequest request = new SellDrugsToUserUseCaseRequest(LocalDate.now(), 100.0, List.of());

        when(findPharmacyByIdOutputPort.findById(idPharmacy)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> sellDrugsToUserUseCase.sellDrugs(idPharmacy, idUser, idEmployee, request));

        verify(updatePharmacyDrugOutputPort, never()).updatePharmacyDrug(any(PharmacyDrug.class));
    }

    @Test
    public void testSellDrugs_WhenDrugNotFound_ThrowsEntityNotFoundException() {
        String idPharmacy = "Pharmacy-123";
        String idUser = "User-1";
        String idEmployee = "Employee-1";
        String codeDrug = UUID.randomUUID().toString();
        SellDrugsToUserItemUseCaseRequest itemRequest = new SellDrugsToUserItemUseCaseRequest(codeDrug, 5);
        SellDrugsToUserUseCaseRequest request = new SellDrugsToUserUseCaseRequest(LocalDate.now(), 100.0, List.of(itemRequest));

        Pharmacy pharmacy = Pharmacy.builder().idArea(idPharmacy).build();
        when(findPharmacyByIdOutputPort.findById(idPharmacy)).thenReturn(Optional.of(pharmacy));
        when(findDrugByCodeOutputPort.findByCode(codeDrug)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> sellDrugsToUserUseCase.sellDrugs(idPharmacy, idUser, idEmployee, request));

        verify(updatePharmacyDrugOutputPort, never()).updatePharmacyDrug(any(PharmacyDrug.class));
    }

    @Test
    public void testSellDrugs_WhenInsufficientQuantity_ThrowsIllegalArgumentException() {
        String idPharmacy = "Pharmacy-123";
        String idUser = "User-1";
        String idEmployee = "Employee-1";
        String codeDrug = UUID.randomUUID().toString();
        SellDrugsToUserItemUseCaseRequest itemRequest = new SellDrugsToUserItemUseCaseRequest(codeDrug, 10);
        SellDrugsToUserUseCaseRequest request = new SellDrugsToUserUseCaseRequest(LocalDate.now(), 100.0, List.of(itemRequest));

        Pharmacy pharmacy = Pharmacy.builder().idArea(idPharmacy).build();
        Drug drug = Drug.builder().code(UUID.fromString(codeDrug)).minimumQuantity(2).unitPrice(10.0).build();
        PharmacyDrug pharmacyDrug = PharmacyDrug.builder().pharmacy(pharmacy).drug(drug).quantity(5).build();

        when(findPharmacyByIdOutputPort.findById(idPharmacy)).thenReturn(Optional.of(pharmacy));
        when(findDrugByCodeOutputPort.findByCode(codeDrug)).thenReturn(Optional.of(drug));
        when(findPharmacyDrugOutputPort.findByIdAndCodeObject(pharmacy, drug)).thenReturn(Optional.of(pharmacyDrug));

        assertThrows(IllegalArgumentException.class, () -> sellDrugsToUserUseCase.sellDrugs(idPharmacy, idUser, idEmployee, request));

        verify(updatePharmacyDrugOutputPort, never()).updatePharmacyDrug(any(PharmacyDrug.class));
    }

    @Test
    public void testSellDrugs_WhenValidRequest_UpdatesPharmacyDrugAndSendsBill() {
        String idPharmacy = "Pharmacy-123";
        String idUser = "User-1";
        String idEmployee = "Employee-1";
        String codeDrug = UUID.randomUUID().toString();
        SellDrugsToUserItemUseCaseRequest itemRequest = new SellDrugsToUserItemUseCaseRequest(codeDrug, 2);
        SellDrugsToUserUseCaseRequest request = new SellDrugsToUserUseCaseRequest(LocalDate.now(), 20.0, List.of(itemRequest));

        Pharmacy pharmacy = Pharmacy.builder().idArea(idPharmacy).build();
        Drug drug = Drug.builder().code(UUID.fromString(codeDrug)).unitPrice(10.0).minimumQuantity(1).build();
        PharmacyDrug pharmacyDrug = PharmacyDrug.builder().pharmacy(pharmacy).drug(drug).quantity(5).build();

        when(findPharmacyByIdOutputPort.findById(idPharmacy)).thenReturn(Optional.of(pharmacy));
        when(findDrugByCodeOutputPort.findByCode(codeDrug)).thenReturn(Optional.of(drug));
        when(findPharmacyDrugOutputPort.findByIdAndCodeObject(pharmacy, drug)).thenReturn(Optional.of(pharmacyDrug));

        byte[] expectedBill = "Sample Bill".getBytes();
        when(billPharmacyDrugOutputPort.SendBillPharmacyDrug(anyString(), anyString(), anyString(), any())).thenReturn(expectedBill);

        byte[] result = sellDrugsToUserUseCase.sellDrugs(idPharmacy, idUser, idEmployee, request);

        verify(updatePharmacyDrugOutputPort, times(1)).updatePharmacyDrug(pharmacyDrug);
        assertEquals(3, pharmacyDrug.getQuantity());
        verify(billPharmacyDrugOutputPort, times(1)).SendBillPharmacyDrug(eq(idPharmacy), eq(idUser), eq(idEmployee), any());
        assertEquals(expectedBill, result);
    }
}
