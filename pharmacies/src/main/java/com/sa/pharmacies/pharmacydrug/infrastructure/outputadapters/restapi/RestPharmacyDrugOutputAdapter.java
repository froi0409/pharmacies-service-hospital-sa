package com.sa.pharmacies.pharmacydrug.infrastructure.outputadapters.restapi;

import com.sa.pharmacies.common.annotation.OutputAdapter;
import com.sa.pharmacies.pharmacydrug.application.selldrugstouser.PayPharmacyRequest;
import com.sa.pharmacies.pharmacydrug.infrastructure.outputports.restapi.BillPharmacyDrugOutputPort;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

@OutputAdapter
public class RestPharmacyDrugOutputAdapter implements BillPharmacyDrugOutputPort {

    private final RestTemplate restTemplate;
    private final String URL_FINANCES = "lb://FINANCES/api/v1/";

    @Autowired
    public RestPharmacyDrugOutputAdapter(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    @Override
    public byte[] SendBillPharmacyDrug(String idPharmacy, String idUser, String idEmployee, PayPharmacyRequest payPharmacyRequest) {
        String url = URL_FINANCES + "bills/pharmacy/" + idPharmacy + "/" + idUser + "/" + idEmployee;
        try {
            // Enviamos la solicitud y esperamos una respuesta en formato de bytes (PDF)
            ResponseEntity<byte[]> response = restTemplate.postForEntity(url, payPharmacyRequest, byte[].class);

            // Verificamos que la respuesta tenga un estado exitoso antes de devolver el contenido
            if (response.getStatusCode().is2xxSuccessful()) {
                return response.getBody();  // Devolvemos el contenido del PDF como un array de bytes
            } else {
                throw new IllegalArgumentException("Error en la respuesta del servidor al intentar generar la factura.");
            }
        } catch (HttpClientErrorException e) {
            // Manejo de excepciones en caso de error HTTP
            throw new RuntimeException("Error al enviar la solicitud para generar la factura: " + e.getMessage(), e);
        }
    }
}
