package com.sa.pharmacies.pharmacydrug.application.selldrugstouser;

import com.sa.pharmacies.common.annotation.UseCase;
import com.sa.pharmacies.common.application.NotificationMessageBuilder;
import com.sa.pharmacies.drug.domain.Drug;
import com.sa.pharmacies.drug.infrastructure.outputports.db.FindDrugByCodeOutputPort;
import com.sa.pharmacies.pharmacy.domain.Pharmacy;
import com.sa.pharmacies.pharmacy.infrastructure.outputports.db.FindPharmacyByIdOutputPort;
import com.sa.pharmacies.pharmacydrug.domain.PharmacyDrug;
import com.sa.pharmacies.pharmacydrug.infrastructure.inputports.restapi.SellDrugsToUserInputPort;
import com.sa.pharmacies.pharmacydrug.infrastructure.outputports.db.FindPharmacyDrugOutputPort;
import com.sa.pharmacies.pharmacydrug.infrastructure.outputports.db.UpdatePharmacyDrugOutputPort;
import com.sa.pharmacies.pharmacydrug.infrastructure.outputports.kafka.SendMinimumQuantityProducerOutputPort;
import com.sa.pharmacies.pharmacydrug.infrastructure.outputports.restapi.BillPharmacyDrugOutputPort;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;

@Transactional
@UseCase
public class SellDrugsToUserUseCase implements SellDrugsToUserInputPort {
    private final FindPharmacyByIdOutputPort findPharmacyByIdOutputPort;
    private final FindDrugByCodeOutputPort findDrugByCodeOutputPort;
    private final FindPharmacyDrugOutputPort findPharmacyDrugOutputPort;
    private final UpdatePharmacyDrugOutputPort updatePharmacyDrugOutputPort;
    private final BillPharmacyDrugOutputPort billPharmacyDrugOutputPort;
    private final SendMinimumQuantityProducerOutputPort sendMinimumQuantityProducerOutputPort;

    @Autowired
    public SellDrugsToUserUseCase(FindPharmacyByIdOutputPort findPharmacyByIdOutputPort, FindDrugByCodeOutputPort findDrugByCodeOutputPort, FindPharmacyDrugOutputPort findPharmacyDrugOutputPort, UpdatePharmacyDrugOutputPort updatePharmacyDrugOutputPort, BillPharmacyDrugOutputPort billPharmacyDrugOutputPort, SendMinimumQuantityProducerOutputPort sendMinimumQuantityProducerOutputPort) {
        this.findPharmacyByIdOutputPort = findPharmacyByIdOutputPort;
        this.findDrugByCodeOutputPort = findDrugByCodeOutputPort;
        this.findPharmacyDrugOutputPort = findPharmacyDrugOutputPort;
        this.updatePharmacyDrugOutputPort = updatePharmacyDrugOutputPort;
        this.billPharmacyDrugOutputPort = billPharmacyDrugOutputPort;
        this.sendMinimumQuantityProducerOutputPort = sendMinimumQuantityProducerOutputPort;
    }

    @Override
    public byte[] sellDrugs(String idPharmacy, String idUser, String idEmployee, SellDrugsToUserUseCaseRequest request) throws IllegalArgumentException, EntityNotFoundException {
        //validate if exists
        Pharmacy pharmacy = findPharmacyByIdOutputPort.findById(idPharmacy)
                .orElseThrow(() -> new EntityNotFoundException("Pharmacy not found"));

        List<PayPharmacyDescriptionRequest> payPharmacyDescriptionRequests = new ArrayList<>();
        double totalCost = 0;
        //update the drugs
        for (SellDrugsToUserItemUseCaseRequest item: request.getPharmacyDescriptionRequest()){
            Drug drug = findDrugByCodeOutputPort.findByCode(item.getCode())
                    .orElseThrow(() -> new EntityNotFoundException("drug not found"));

            //find the relationship
            PharmacyDrug pharmacyDrug = findPharmacyDrugOutputPort.findByIdAndCodeObject(pharmacy, drug)
                    .orElseThrow(() -> new EntityNotFoundException("there's not kind of drug in this pharmacy"));

            //validate
            int newQuantity = pharmacyDrug.getQuantity() - item.getQuantity();

            if (newQuantity < 0 ){
                throw new IllegalArgumentException("there's not enough quantity in this pharmacy");
            }

            if (item.getQuantity() < 0 ){
                throw new IllegalArgumentException("Quantity must be greater than zero");
            }

            pharmacyDrug.setQuantity(newQuantity);
            totalCost += item.getQuantity() * drug.getUnitPrice();

            //validate the minimum quantity
            if (pharmacyDrug.getQuantity() <=  drug.getMinimumQuantity()){
                ///event to send notifications sendAllByType
                sendMinimumQuantityProducerOutputPort.sendNotification(pharmacy.getIdArea(), NotificationMessageBuilder.createMinimumQuantityMessage(drug,pharmacyDrug.getQuantity()));
            }

            PayPharmacyDescriptionRequest payPharmacyDescriptionRequest = new PayPharmacyDescriptionRequest();
            payPharmacyDescriptionRequest.setQuantity(item.getQuantity());
            payPharmacyDescriptionRequest.setUnitCost(drug.getCost());
            payPharmacyDescriptionRequest.setIdProduct(drug.getCodeString());
            payPharmacyDescriptionRequest.setUnitPrice(drug.getUnitPrice());

            payPharmacyDescriptionRequests.add(payPharmacyDescriptionRequest);

            updatePharmacyDrugOutputPort.updatePharmacyDrug(pharmacyDrug);
        }

        if (totalCost != request.getTotalCost()){
            throw new IllegalArgumentException("total cost must be equal to request totalCost");
        }

        //do the bill
        ///REST
        PayPharmacyRequest payPharmacyRequest = new PayPharmacyRequest();
        payPharmacyRequest.setDate(request.getDate());
        payPharmacyRequest.setTotalCost(totalCost);
        payPharmacyRequest.setPharmacyDescriptionRequest(payPharmacyDescriptionRequests);

        //send to the endpoint the bill
       return billPharmacyDrugOutputPort.SendBillPharmacyDrug(idPharmacy, idUser, idEmployee, payPharmacyRequest);
    }
}
