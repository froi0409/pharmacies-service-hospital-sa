package com.sa.pharmacies.pharmacydrug.application.removequantityfromhospitals;

import com.sa.pharmacies.common.annotation.UseCase;
import com.sa.pharmacies.common.application.NotificationMessageBuilder;
import com.sa.pharmacies.drug.domain.Drug;
import com.sa.pharmacies.drug.infrastructure.outputports.db.FindDrugByCodeOutputPort;
import com.sa.pharmacies.pharmacy.domain.Pharmacy;
import com.sa.pharmacies.pharmacy.infrastructure.outputports.db.FindPharmacyByIdOutputPort;
import com.sa.pharmacies.pharmacydrug.domain.PharmacyDrug;
import com.sa.pharmacies.pharmacydrug.infrastructure.inputports.restapi.RemoveQuantityFromHospitalsInputPort;
import com.sa.pharmacies.pharmacydrug.infrastructure.outputports.db.FindPharmacyDrugOutputPort;
import com.sa.pharmacies.pharmacydrug.infrastructure.outputports.db.UpdatePharmacyDrugOutputPort;
import com.sa.pharmacies.pharmacydrug.infrastructure.outputports.kafka.PaymentDrugProducerOutputPort;
import com.sa.pharmacies.pharmacydrug.infrastructure.outputports.kafka.SendMinimumQuantityProducerOutputPort;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;

@Transactional
@UseCase
public class RemoveQuantityFromHospitalsUseCase implements RemoveQuantityFromHospitalsInputPort {
    private final FindPharmacyByIdOutputPort findPharmacyByIdOutputPort;
    private final FindDrugByCodeOutputPort findDrugByCodeOutputPort;
    private final FindPharmacyDrugOutputPort findPharmacyDrugOutputPort;
    private final UpdatePharmacyDrugOutputPort updatePharmacyDrugOutputPort;
    private final SendMinimumQuantityProducerOutputPort sendMinimumQuantityProducerOutputPort;

    @Autowired
    public RemoveQuantityFromHospitalsUseCase(FindPharmacyByIdOutputPort findPharmacyByIdOutputPort, FindDrugByCodeOutputPort findDrugByCodeOutputPort, FindPharmacyDrugOutputPort findPharmacyDrugOutputPort, UpdatePharmacyDrugOutputPort updatePharmacyDrugOutputPort, SendMinimumQuantityProducerOutputPort sendMinimumQuantityProducerOutputPort) {
        this.findPharmacyByIdOutputPort = findPharmacyByIdOutputPort;
        this.findDrugByCodeOutputPort = findDrugByCodeOutputPort;
        this.findPharmacyDrugOutputPort = findPharmacyDrugOutputPort;
        this.updatePharmacyDrugOutputPort = updatePharmacyDrugOutputPort;
        this.sendMinimumQuantityProducerOutputPort = sendMinimumQuantityProducerOutputPort;
    }

    @Override
    public List<RemoveQuantityFromHospitalsResponse> remove(List<RemoveQuantityFromHospitalsRequest> requests) throws EntityNotFoundException {
        List<RemoveQuantityFromHospitalsResponse> responses = new ArrayList<>();
        for (RemoveQuantityFromHospitalsRequest request : requests) {
            //find the pharmacy
            Pharmacy pharmacy = findPharmacyByIdOutputPort.findById(request.getIdPharmacy())
                    .orElseThrow(() -> new EntityNotFoundException("Pharmacy not found"));

            //find the drug
            Drug drug = findDrugByCodeOutputPort.findByCode(request.getCode())
                    .orElseThrow(() -> new EntityNotFoundException("drug not found"));

            //find the relationship
            PharmacyDrug pharmacyDrug = findPharmacyDrugOutputPort.findByIdAndCodeObject(pharmacy, drug)
                    .orElseThrow(() -> new EntityNotFoundException("there's not kind of drug in this pharmacy"));

            //validate
            int newQuantity = pharmacyDrug.getQuantity() - request.getQuantity();

            if (newQuantity < 0 ){
                throw new IllegalArgumentException("there's not enough quantity in this pharmacy");
            }

            if (request.getQuantity() < 0 ){
                throw new IllegalArgumentException("Quantity must be greater than zero");
            }

            pharmacyDrug.setQuantity(newQuantity);
            //validate the minimum quantity
            if (pharmacyDrug.getQuantity() <=  drug.getMinimumQuantity()){
                ///event to send notifications sendAllByType
                sendMinimumQuantityProducerOutputPort.sendNotification(pharmacy.getIdArea(), NotificationMessageBuilder.createMinimumQuantityMessage(drug,pharmacyDrug.getQuantity()));
            }
            updatePharmacyDrugOutputPort.updatePharmacyDrug(pharmacyDrug);
            responses.add(RemoveQuantityFromHospitalsResponse.from(drug));
        }
        return responses;
    }
}
