package com.sa.pharmacies.pharmacy.infrastructure.inputadapters.kafka;

import com.sa.pharmacies.common.exceptions.EntityAlreadyExistsException;
import com.sa.pharmacies.pharmacy.infrastructure.inputports.restapi.CreatePharmacyByEventInputPort;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
public class CreatePharmacyListenerInputAdapter {
    private final CreatePharmacyByEventInputPort createPharmacyByEventInputPort;

    @Autowired
    public CreatePharmacyListenerInputAdapter(CreatePharmacyByEventInputPort createPharmacyByEventInputPort) {
        this.createPharmacyByEventInputPort = createPharmacyByEventInputPort;
    }

    @KafkaListener(topics = "create-pharmacy", groupId = "pharmacy-group")
    public void listen(ConsumerRecord<String, String> record) {
        String pharmacyId = record.value();
        try {
            createPharmacyByEventInputPort.createPharmacyByEvent(pharmacyId);
            System.out.println("Pharmacy created: " + pharmacyId);
        } catch (EntityAlreadyExistsException e) {
            throw new RuntimeException(e);
        }
    }

}
