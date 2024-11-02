package com.sa.pharmacies.pharmacydrug.infrastructure.inputadapters.restapi;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.sa.pharmacies.common.annotation.WebAdapter;
import com.sa.pharmacies.pharmacydrug.application.buymoredrugspharmacy.BuyMoreDrugsPharmacyUseCaseRequest;
import com.sa.pharmacies.pharmacydrug.infrastructure.inputports.restapi.BuyMoreDrugsPharmacyInputPort;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@WebAdapter
@RestController
@RequestMapping("api/v1/pharmacies-drugs")
@SecurityRequirement(name = "bearerAuth")
public class PharmacyDrugController {
    private final BuyMoreDrugsPharmacyInputPort buyMoreDrugsPharmacyInputPort;

    @Autowired
    public PharmacyDrugController(BuyMoreDrugsPharmacyInputPort buyMoreDrugsPharmacyInputPort) {
        this.buyMoreDrugsPharmacyInputPort = buyMoreDrugsPharmacyInputPort;
    }

    @PostMapping("buy-more/{idPharmacy}/{codeDrug}")
    public ResponseEntity<String> buyMoreDrugs(@PathVariable("idPharmacy") String idPharmacy,
                                               @PathVariable("codeDrug") String code,
                                               @RequestBody BuyMoreDrugsPharmacyUseCaseRequest request) throws JsonProcessingException {
        buyMoreDrugsPharmacyInputPort.buyMoreDrugsPharmacy(idPharmacy, code, request);
        return ResponseEntity.ok().build();
    }
}
