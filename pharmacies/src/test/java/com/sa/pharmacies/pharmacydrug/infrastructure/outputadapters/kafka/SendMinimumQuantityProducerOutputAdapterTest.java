package com.sa.pharmacies.pharmacydrug.infrastructure.outputadapters.kafka;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.kafka.core.KafkaTemplate;

public class SendMinimumQuantityProducerOutputAdapterTest {

    @Mock
    private KafkaTemplate<String, String> kafkaTemplate;

    @InjectMocks
    private SendMinimumQuantityProducerOutputAdapter sendMinimumQuantityProducerOutputAdapter;

    private ObjectMapper objectMapper;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
        objectMapper = new ObjectMapper();
    }

    @Test
    public void testSendNotification_SendsMessageToKafka() throws JsonProcessingException {
        // Configuración de los datos de la notificación
        String type = "LowStock";
        String description = "The stock is below minimum threshold.";
        SendAllNotificationRequest request = new SendAllNotificationRequest(type, description);

        // Serialización esperada de la solicitud
        String expectedMessage = objectMapper.writeValueAsString(request);

        // Invocación del método a probar
        sendMinimumQuantityProducerOutputAdapter.sendNotification(type, description);

        // Verificación de que se envió el mensaje JSON al tema de Kafka
        verify(kafkaTemplate, times(1)).send("send-all-by-type", expectedMessage);
    }
}
