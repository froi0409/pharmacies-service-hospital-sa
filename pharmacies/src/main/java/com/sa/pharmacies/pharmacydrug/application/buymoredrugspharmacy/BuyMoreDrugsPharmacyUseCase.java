package com.sa.pharmacies.pharmacydrug.application.buymoredrugspharmacy;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.sa.pharmacies.common.annotation.UseCase;
import com.sa.pharmacies.drug.domain.Drug;
import com.sa.pharmacies.drug.infrastructure.outputports.db.FindDrugByCodeOutputPort;
import com.sa.pharmacies.pharmacy.domain.Pharmacy;
import com.sa.pharmacies.pharmacy.infrastructure.outputports.db.FindPharmacyByIdOutputPort;
import com.sa.pharmacies.pharmacydrug.domain.PharmacyDrug;
import com.sa.pharmacies.pharmacydrug.infrastructure.inputports.restapi.BuyMoreDrugsPharmacyInputPort;
import com.sa.pharmacies.pharmacydrug.infrastructure.outputports.db.FindPharmacyDrugOutputPort;
import com.sa.pharmacies.pharmacydrug.infrastructure.outputports.db.UpdatePharmacyDrugOutputPort;
import com.sa.pharmacies.pharmacydrug.infrastructure.outputports.kafka.PaymentDrugProducerOutputPort;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;

@Transactional
@UseCase
public class BuyMoreDrugsPharmacyUseCase implements BuyMoreDrugsPharmacyInputPort {
    private final FindPharmacyByIdOutputPort findPharmacyByIdOutputPort;
    private final FindDrugByCodeOutputPort findDrugByCodeOutputPort;
    private final FindPharmacyDrugOutputPort findPharmacyDrugOutputPort;
    private final UpdatePharmacyDrugOutputPort updatePharmacyDrugOutputPort;
    private final PaymentDrugProducerOutputPort paymentDrugProducerOutputPort;

    @Autowired
    public BuyMoreDrugsPharmacyUseCase(FindPharmacyByIdOutputPort findPharmacyByIdOutputPort, FindDrugByCodeOutputPort findDrugByCodeOutputPort, FindPharmacyDrugOutputPort findPharmacyDrugOutputPort, UpdatePharmacyDrugOutputPort updatePharmacyDrugOutputPort, PaymentDrugProducerOutputPort paymentDrugProducerOutputPort) {
        this.findPharmacyByIdOutputPort = findPharmacyByIdOutputPort;
        this.findDrugByCodeOutputPort = findDrugByCodeOutputPort;
        this.findPharmacyDrugOutputPort = findPharmacyDrugOutputPort;
        this.updatePharmacyDrugOutputPort = updatePharmacyDrugOutputPort;
        this.paymentDrugProducerOutputPort = paymentDrugProducerOutputPort;
    }

    @Override
    public void buyMoreDrugsPharmacy(String idPharmacy, String code, BuyMoreDrugsPharmacyUseCaseRequest request) throws EntityNotFoundException, IllegalArgumentException, JsonProcessingException {
        //find the pharmacy
        Pharmacy pharmacy = findPharmacyByIdOutputPort.findById(idPharmacy)
                .orElseThrow(() -> new EntityNotFoundException("Pharmacy not found"));

        //find the drug
        Drug drug = findDrugByCodeOutputPort.findByCode(code)
                .orElseThrow(() -> new EntityNotFoundException("drug not found"));

        //find the relationship
        PharmacyDrug pharmacyDrug = findPharmacyDrugOutputPort.findByIdAndCodeObject(pharmacy, drug)
                .orElseThrow(() -> new EntityNotFoundException("there's not kind of drug in this pharmacy"));

        //validate
        int newQuantity = pharmacyDrug.getQuantity() + request.getQuantity();

        if (newQuantity < 0 ){
            throw new IllegalArgumentException("Quantity must be greater than zero");
        }

        if (request.getQuantity() < 0 ){
            throw new IllegalArgumentException("Quantity must be greater than zero");
        }

        pharmacyDrug.setQuantity(newQuantity);

        ///event ot the bill
        //request how to connect, get the price, cost, unit cost and the date from request.getDate()
        double calculatedAmount = request.getQuantity() * drug.getCost();
        // Crear PaymentDrugRequest con la información necesaria
        PaymentDrugRequest paymentDrugRequest = new PaymentDrugRequest();
        paymentDrugRequest.setAmount(calculatedAmount); // Debes calcular el monto según el precio y la cantidad
        paymentDrugRequest.setDate(request.getDate());
        paymentDrugRequest.setIdDrug(code);

        //send to kafka
        paymentDrugProducerOutputPort.sendPaymentDrugEvent(paymentDrugRequest);

        //save
        updatePharmacyDrugOutputPort.updatePharmacyDrug(pharmacyDrug);
    }


}
