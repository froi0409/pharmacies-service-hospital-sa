package com.sa.pharmacies.pharmacydrug.infrastructure.outputadapters.kafka;

import static org.mockito.Mockito.*;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sa.pharmacies.pharmacydrug.application.buymoredrugspharmacy.PaymentDrugRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.kafka.core.KafkaTemplate;

public class PaymentDrugProducerOutputAdapterTest {

    @Mock
    private KafkaTemplate<String, String> kafkaTemplate;

    @InjectMocks
    private PaymentDrugProducerOutputAdapter paymentDrugProducerOutputAdapter;

    private ObjectMapper objectMapper;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
        objectMapper = new ObjectMapper();
    }

    @Test
    public void testSendPaymentDrugEvent_SendsMessageToKafka() throws JsonProcessingException {
        // Configuración del objeto PaymentDrugRequest
        PaymentDrugRequest request = new PaymentDrugRequest(100.0, "2024-11-01", "drug-123");

        // Conversión de request a JSON
        String expectedJson = objectMapper.writeValueAsString(request);

        // Invocación del método a probar
        paymentDrugProducerOutputAdapter.sendPaymentDrugEvent(request);

        // Verificación de que se envió el mensaje JSON al tema de Kafka
        verify(kafkaTemplate, times(1)).send("payment-drug", expectedJson);
    }
}
