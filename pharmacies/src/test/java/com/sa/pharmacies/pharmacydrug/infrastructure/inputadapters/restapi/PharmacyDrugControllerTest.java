package com.sa.pharmacies.pharmacydrug.infrastructure.inputadapters.restapi;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.itextpdf.text.DocumentException;
import com.sa.pharmacies.pharmacy.application.getlistdrugdatabypharmacy.GetListDrugDataByPharmacyResponse;
import com.sa.pharmacies.pharmacydrug.application.buymoredrugspharmacy.BuyMoreDrugsPharmacyUseCaseRequest;
import com.sa.pharmacies.pharmacydrug.application.selldrugstouser.SellDrugsToUserUseCase;
import com.sa.pharmacies.pharmacydrug.application.selldrugstouser.SellDrugsToUserUseCaseRequest;
import com.sa.pharmacies.pharmacydrug.infrastructure.inputports.restapi.BuyMoreDrugsPharmacyInputPort;
import com.sa.pharmacies.pharmacydrug.infrastructure.inputports.restapi.GetListDrugDataByPharmacyInputPort;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.*;

import java.io.IOException;
import java.util.List;

public class PharmacyDrugControllerTest {

    @Mock
    private BuyMoreDrugsPharmacyInputPort buyMoreDrugsPharmacyInputPort;

    @Mock
    private GetListDrugDataByPharmacyInputPort getListDrugDataByPharmacyInputPort;

    @Mock
    private SellDrugsToUserUseCase sellDrugsToUserUseCase;

    @InjectMocks
    private PharmacyDrugController pharmacyDrugController;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testBuyMoreDrugs() throws JsonProcessingException {
        String idPharmacy = "Pharmacy-123";
        String codeDrug = "Drug-456";
        BuyMoreDrugsPharmacyUseCaseRequest request = new BuyMoreDrugsPharmacyUseCaseRequest(10, null);

        ResponseEntity<String> response = pharmacyDrugController.buyMoreDrugs(idPharmacy, codeDrug, request);

        verify(buyMoreDrugsPharmacyInputPort, times(1)).buyMoreDrugsPharmacy(idPharmacy, codeDrug, request);
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    public void testGetDrugsNameByPharmacy() {
        String idPharmacy = "Pharmacy-123";
        List<GetListDrugDataByPharmacyResponse> responses = List.of(
                new GetListDrugDataByPharmacyResponse("Drug-1", "Ibuprofen", 10.0, 12.5, 100),
                new GetListDrugDataByPharmacyResponse("Drug-2", "Paracetamol", 5.0, 7.5, 50)
        );

        when(getListDrugDataByPharmacyInputPort.getDrugsName(idPharmacy)).thenReturn(responses);

        ResponseEntity<List<GetListDrugDataByPharmacyResponse>> response = pharmacyDrugController.getDrugsNameByParmacy(idPharmacy);

        verify(getListDrugDataByPharmacyInputPort, times(1)).getDrugsName(idPharmacy);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(responses, response.getBody());
    }

    @Test
    public void testPayBillPharmacy() throws IOException, DocumentException {
        String idPharmacy = "Pharmacy-123";
        String idUser = "User-456";
        String idEmployee = "Employee-789";
        SellDrugsToUserUseCaseRequest bill = new SellDrugsToUserUseCaseRequest(null, 100.0, List.of());

        byte[] expectedPdf = "Sample PDF Content".getBytes();
        when(sellDrugsToUserUseCase.sellDrugs(idPharmacy, idUser, idEmployee, bill)).thenReturn(expectedPdf);

        HttpEntity<byte[]> response = pharmacyDrugController.payBillPharmacy(idPharmacy, idUser, idEmployee, bill);

        verify(sellDrugsToUserUseCase, times(1)).sellDrugs(idPharmacy, idUser, idEmployee, bill);
        assertEquals(MediaType.APPLICATION_PDF_VALUE, MediaType.APPLICATION_PDF_VALUE);
        assertEquals("attachment; filename=bill.pdf", response.getHeaders().getFirst(HttpHeaders.CONTENT_DISPOSITION));
        assertEquals(expectedPdf, response.getBody());
    }
}
