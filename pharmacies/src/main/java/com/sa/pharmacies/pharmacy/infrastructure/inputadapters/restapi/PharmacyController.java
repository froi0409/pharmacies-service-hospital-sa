package com.sa.pharmacies.pharmacy.infrastructure.inputadapters.restapi;

import com.sa.pharmacies.common.annotation.WebAdapter;
import com.sa.pharmacies.common.exceptions.EntityAlreadyExistsException;
import com.sa.pharmacies.pharmacy.domain.Pharmacy;
import com.sa.pharmacies.pharmacy.infrastructure.inputports.restapi.CreatePharmacyByEventInputPort;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@WebAdapter
@RestController
@RequestMapping("v1/pharmacies")
public class PharmacyController {
    private final CreatePharmacyByEventInputPort createPharmacyByEventInputPort;

    @Autowired
    public PharmacyController(CreatePharmacyByEventInputPort createPharmacyByEventInputPort) {
        this.createPharmacyByEventInputPort = createPharmacyByEventInputPort;
    }

    @PostMapping("{idArea}")
    public ResponseEntity<String> createPharmacy(@PathVariable String idArea) throws EntityAlreadyExistsException {
        createPharmacyByEventInputPort.createPharmacyByEvent(idArea);
        return ResponseEntity.status(HttpStatus.CREATED).body("Pharmacy created");
    }

}
