package com.sa.pharmacies.pharmacy.infrastructure.inputadapters.restapi;

import com.sa.pharmacies.common.annotation.WebAdapter;
import com.sa.pharmacies.common.exceptions.EntityAlreadyExistsException;
import com.sa.pharmacies.drug.infrastructure.inputports.restapi.CreateDrugInputPort;
import com.sa.pharmacies.pharmacy.application.createdrug.CreateDrugRequest;
import com.sa.pharmacies.pharmacy.domain.Pharmacy;
import com.sa.pharmacies.pharmacy.infrastructure.inputports.restapi.CreatePharmacyByEventInputPort;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@WebAdapter
@RestController
@RequestMapping("v1/pharmacies")
public class PharmacyController {
    private final CreatePharmacyByEventInputPort createPharmacyByEventInputPort;
    private final CreateDrugInputPort createDrugInputPort;
    @Autowired
    public PharmacyController(CreatePharmacyByEventInputPort createPharmacyByEventInputPort, CreateDrugInputPort createDrugInputPort) {
        this.createPharmacyByEventInputPort = createPharmacyByEventInputPort;
        this.createDrugInputPort = createDrugInputPort;
    }

    @PostMapping("{idArea}")
    public ResponseEntity<String> createPharmacy(@PathVariable String idArea) throws EntityAlreadyExistsException {
        createPharmacyByEventInputPort.createPharmacyByEvent(idArea);
        return ResponseEntity.status(HttpStatus.CREATED).body("Pharmacy created");
    }

    @PostMapping("drug")
    public ResponseEntity<String> createDrug(@RequestBody CreateDrugRequest createDrugRequest) throws EntityAlreadyExistsException {
        String id = createDrugInputPort.createDrug(createDrugRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body(id);
    }

}
