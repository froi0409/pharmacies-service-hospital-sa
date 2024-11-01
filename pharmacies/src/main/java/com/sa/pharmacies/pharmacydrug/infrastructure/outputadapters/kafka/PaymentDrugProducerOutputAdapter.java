package com.sa.pharmacies.pharmacydrug.infrastructure.outputadapters.kafka;

import com.sa.pharmacies.pharmacydrug.application.buymoredrugspharmacy.PaymentDrugRequest;
import com.sa.pharmacies.pharmacydrug.infrastructure.outputports.kafka.PaymentDrugProducerOutputPort;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
public class PaymentDrugProducerOutputAdapter implements PaymentDrugProducerOutputPort {

    private final KafkaTemplate<String, PaymentDrugRequest> kafkaTemplate;
    private static final String TOPIC = "payment-drug";

    @Autowired
    public PaymentDrugProducerOutputAdapter(KafkaTemplate<String, PaymentDrugRequest> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    @Override
    public void sendPaymentDrugEvent(PaymentDrugRequest request) {
        kafkaTemplate.send(TOPIC, request);
    }
}
