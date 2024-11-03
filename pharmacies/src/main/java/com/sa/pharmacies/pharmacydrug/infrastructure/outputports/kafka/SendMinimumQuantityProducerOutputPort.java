package com.sa.pharmacies.pharmacydrug.infrastructure.outputports.kafka;

public interface SendMinimumQuantityProducerOutputPort {
    void sendNotification(String type, String description);
}
