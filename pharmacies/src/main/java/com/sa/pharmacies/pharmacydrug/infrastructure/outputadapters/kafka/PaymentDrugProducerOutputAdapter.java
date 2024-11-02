package com.sa.pharmacies.pharmacydrug.infrastructure.outputadapters.kafka;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sa.pharmacies.pharmacydrug.application.buymoredrugspharmacy.PaymentDrugRequest;
import com.sa.pharmacies.pharmacydrug.infrastructure.outputports.kafka.PaymentDrugProducerOutputPort;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
public class PaymentDrugProducerOutputAdapter implements PaymentDrugProducerOutputPort {

    private final KafkaTemplate<String, String> kafkaTemplate;
    private static final String TOPIC = "payment-drug";

    @Autowired
    public PaymentDrugProducerOutputAdapter(KafkaTemplate<String, String> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    @Override
    public void sendPaymentDrugEvent(PaymentDrugRequest request) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        String jsonRequest = objectMapper.writeValueAsString(request);
        kafkaTemplate.send(TOPIC, jsonRequest);
    }
}
