package com.sa.pharmacies.pharmacy.application.assignpharmacydrug;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.sa.pharmacies.common.annotation.UseCase;
import com.sa.pharmacies.common.exceptions.EntityAlreadyExistsException;
import com.sa.pharmacies.drug.domain.Drug;
import com.sa.pharmacies.drug.infrastructure.outputports.db.FindDrugByCodeOutputPort;
import com.sa.pharmacies.pharmacy.domain.Pharmacy;
import com.sa.pharmacies.pharmacy.infrastructure.outputports.db.FindPharmacyByIdOutputPort;
import com.sa.pharmacies.pharmacydrug.application.buymoredrugspharmacy.PaymentDrugRequest;
import com.sa.pharmacies.pharmacydrug.domain.PharmacyDrug;
import com.sa.pharmacies.pharmacydrug.infrastructure.inputports.restapi.AssignPharmacyDrugInputPort;
import com.sa.pharmacies.pharmacydrug.infrastructure.outputports.db.FindPharmacyDrugOutputPort;
import com.sa.pharmacies.pharmacydrug.infrastructure.outputports.db.SavePharmacyDrugOutputPort;
import com.sa.pharmacies.pharmacydrug.infrastructure.outputports.kafka.PaymentDrugProducerOutputPort;
import jakarta.transaction.Transactional;

import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Optional;

@Transactional
@UseCase
public class AssignPharmacyDrugUseCase implements AssignPharmacyDrugInputPort {
    private final FindPharmacyByIdOutputPort findPharmacyByIdOutputPort;
    private final FindDrugByCodeOutputPort findDrugByCodeOutputPort;
    private final FindPharmacyDrugOutputPort findPharmacyDrugOutputPort;
    private final SavePharmacyDrugOutputPort savePharmacyDrugOutputPort;
    private final PaymentDrugProducerOutputPort paymentDrugProducerOutputPort;

    public AssignPharmacyDrugUseCase(FindPharmacyByIdOutputPort findPharmacyByIdOutputPort, FindDrugByCodeOutputPort findDrugByCodeOutputPort, FindPharmacyDrugOutputPort findPharmacyDrugOutputPort, SavePharmacyDrugOutputPort savePharmacyDrugOutputPort, PaymentDrugProducerOutputPort paymentDrugProducerOutputPort) {
        this.findPharmacyByIdOutputPort = findPharmacyByIdOutputPort;
        this.findDrugByCodeOutputPort = findDrugByCodeOutputPort;
        this.findPharmacyDrugOutputPort = findPharmacyDrugOutputPort;
        this.savePharmacyDrugOutputPort = savePharmacyDrugOutputPort;
        this.paymentDrugProducerOutputPort = paymentDrugProducerOutputPort;
    }

    @Override
    public void assign(AssignPharmacyDrugRequest request) throws EntityAlreadyExistsException, IllegalArgumentException, JsonProcessingException {
        PharmacyDrug pharmacyDrug = request.toDomain();

        //find the pharmacy
        Pharmacy pharmacy = findPharmacyByIdOutputPort.findById(request.getIdPharmacy())
                .orElseThrow(() -> new IllegalArgumentException("Pharmacy not found"));

        //find the drug
        Drug drug = findDrugByCodeOutputPort.findByCode(request.getCodeDrug())
                .orElseThrow(() -> new IllegalArgumentException("drug not found"));

        //validate if the relations not exists
        Optional<PharmacyDrug> isPresent = findPharmacyDrugOutputPort.findByIdAndCode(request.getIdPharmacy(), request.getCodeDrug());

        if (isPresent.isPresent()) {
            throw new EntityAlreadyExistsException("Pharmacy and drug already together. Try modify the quantity only");
        }

        //finish the domain
        pharmacyDrug.setPharmacy(pharmacy);
        pharmacyDrug.setDrug(drug);

        //the payment event
        double calculatedAmount = request.getQuantity() * drug.getCost();
        PaymentDrugRequest paymentDrugRequest = new PaymentDrugRequest();
        paymentDrugRequest.setAmount(calculatedAmount);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        String formattedDate = LocalDate.now().format(formatter);
        paymentDrugRequest.setDate(formattedDate);
        paymentDrugRequest.setIdDrug(drug.getCode().toString());

        //send to kafka
        paymentDrugProducerOutputPort.sendPaymentDrugEvent(paymentDrugRequest);


        //save into the database
        pharmacyDrug = savePharmacyDrugOutputPort.save(pharmacyDrug);

    }
}
