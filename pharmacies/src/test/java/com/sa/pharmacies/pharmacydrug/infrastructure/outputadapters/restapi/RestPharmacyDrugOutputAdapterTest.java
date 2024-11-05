package com.sa.pharmacies.pharmacydrug.infrastructure.outputadapters.restapi;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

import com.sa.pharmacies.pharmacydrug.application.selldrugstouser.PayPharmacyRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

public class RestPharmacyDrugOutputAdapterTest {

    @Mock
    private RestTemplate restTemplate;

    @InjectMocks
    private RestPharmacyDrugOutputAdapter restPharmacyDrugOutputAdapter;

    private final String URL_FINANCES = "lb://FINANCES/api/v1/";

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testSendBillPharmacyDrug_WhenResponseIsSuccessful_ReturnsPdfBytes() {
        String idPharmacy = "Pharmacy-123";
        String idUser = "User-456";
        String idEmployee = "Employee-789";
        PayPharmacyRequest payPharmacyRequest = new PayPharmacyRequest();
        byte[] expectedPdfBytes = "Sample PDF Content".getBytes();
        String url = URL_FINANCES + "bills/pharmacy/" + idPharmacy + "/" + idUser + "/" + idEmployee;

        when(restTemplate.postForEntity(url, payPharmacyRequest, byte[].class))
                .thenReturn(new ResponseEntity<>(expectedPdfBytes, HttpStatus.OK));

        byte[] result = restPharmacyDrugOutputAdapter.SendBillPharmacyDrug(idPharmacy, idUser, idEmployee, payPharmacyRequest);

        verify(restTemplate, times(1)).postForEntity(url, payPharmacyRequest, byte[].class);
        assertEquals(expectedPdfBytes, result);
    }

    @Test
    public void testSendBillPharmacyDrug_WhenResponseIsNotSuccessful_ThrowsIllegalArgumentException() {
        String idPharmacy = "Pharmacy-123";
        String idUser = "User-456";
        String idEmployee = "Employee-789";
        PayPharmacyRequest payPharmacyRequest = new PayPharmacyRequest();
        String url = URL_FINANCES + "bills/pharmacy/" + idPharmacy + "/" + idUser + "/" + idEmployee;

        when(restTemplate.postForEntity(url, payPharmacyRequest, byte[].class))
                .thenReturn(new ResponseEntity<>(HttpStatus.BAD_REQUEST));

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () ->
                restPharmacyDrugOutputAdapter.SendBillPharmacyDrug(idPharmacy, idUser, idEmployee, payPharmacyRequest));

        assertEquals("Error en la respuesta del servidor al intentar generar la factura.", exception.getMessage());
        verify(restTemplate, times(1)).postForEntity(url, payPharmacyRequest, byte[].class);
    }

    @Test
    public void testSendBillPharmacyDrug_WhenHttpClientErrorException_ThrowsRuntimeException() {
        String idPharmacy = "Pharmacy-123";
        String idUser = "User-456";
        String idEmployee = "Employee-789";
        PayPharmacyRequest payPharmacyRequest = new PayPharmacyRequest();
        String url = URL_FINANCES + "bills/pharmacy/" + idPharmacy + "/" + idUser + "/" + idEmployee;

        HttpClientErrorException httpException = new HttpClientErrorException(HttpStatus.NOT_FOUND, "Not Found");
        when(restTemplate.postForEntity(url, payPharmacyRequest, byte[].class)).thenThrow(httpException);

        RuntimeException exception = assertThrows(RuntimeException.class, () ->
                restPharmacyDrugOutputAdapter.SendBillPharmacyDrug(idPharmacy, idUser, idEmployee, payPharmacyRequest));

        assertEquals("Error al enviar la solicitud para generar la factura: 404 Not Found", exception.getMessage());
        verify(restTemplate, times(1)).postForEntity(url, payPharmacyRequest, byte[].class);
    }
}
