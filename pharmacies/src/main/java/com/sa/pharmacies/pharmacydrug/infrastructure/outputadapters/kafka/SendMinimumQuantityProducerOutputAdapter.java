package com.sa.pharmacies.pharmacydrug.infrastructure.outputadapters.kafka;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sa.pharmacies.pharmacydrug.infrastructure.outputports.kafka.SendMinimumQuantityProducerOutputPort;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
public class SendMinimumQuantityProducerOutputAdapter implements SendMinimumQuantityProducerOutputPort {

    private final KafkaTemplate<String, String> kafkaTemplate;
    private static final String TOPIC = "send-all-by-type";

    @Autowired
    public SendMinimumQuantityProducerOutputAdapter(KafkaTemplate<String, String> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    @Override
    public void sendNotification(String type, String description) {
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            // Crear el request con los datos proporcionados
            SendAllNotificationRequest request = new SendAllNotificationRequest(type, description);

            // Convertir el request a JSON
            String message = objectMapper.writeValueAsString(request);

            // Enviar el mensaje al tópico Kafka
            kafkaTemplate.send(TOPIC, message);
        } catch (JsonProcessingException e) {
            e.printStackTrace(); // Registrar el error
            // Podrías también lanzar una excepción personalizada si deseas manejar los fallos
        }
    }
}
