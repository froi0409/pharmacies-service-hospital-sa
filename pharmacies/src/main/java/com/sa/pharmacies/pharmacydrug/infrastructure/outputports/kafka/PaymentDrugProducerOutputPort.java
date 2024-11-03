package com.sa.pharmacies.pharmacydrug.infrastructure.outputports.kafka;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.sa.pharmacies.pharmacydrug.application.buymoredrugspharmacy.PaymentDrugRequest;

public interface PaymentDrugProducerOutputPort {
    void sendPaymentDrugEvent(PaymentDrugRequest request) throws JsonProcessingException;
}
