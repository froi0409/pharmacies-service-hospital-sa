package com.sa.pharmacies.pharmacydrug.infrastructure.inputadapters.restapi;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.sa.pharmacies.common.annotation.WebAdapter;
import com.sa.pharmacies.pharmacy.application.getlistdrugdatabynameslike.GetItemDrugDataByNamesLikeResponse;
import com.sa.pharmacies.pharmacydrug.application.buymoredrugspharmacy.BuyMoreDrugsPharmacyUseCaseRequest;
import com.sa.pharmacies.pharmacydrug.infrastructure.inputports.restapi.BuyMoreDrugsPharmacyInputPort;
import com.sa.pharmacies.pharmacydrug.infrastructure.inputports.restapi.GetListDrugDataByPharmacyInputPort;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@WebAdapter
@RestController
@RequestMapping("api/v1/pharmacies-drugs")
@SecurityRequirement(name = "bearerAuth")
public class PharmacyDrugController {
    private final BuyMoreDrugsPharmacyInputPort buyMoreDrugsPharmacyInputPort;
    private final GetListDrugDataByPharmacyInputPort getListDrugDataByPharmacyInputPort;

    @Autowired
    public PharmacyDrugController(BuyMoreDrugsPharmacyInputPort buyMoreDrugsPharmacyInputPort, GetListDrugDataByPharmacyInputPort getListDrugDataByPharmacyInputPort) {
        this.buyMoreDrugsPharmacyInputPort = buyMoreDrugsPharmacyInputPort;
        this.getListDrugDataByPharmacyInputPort = getListDrugDataByPharmacyInputPort;
    }

    @PostMapping("buy-more/{idPharmacy}/{codeDrug}")
    public ResponseEntity<String> buyMoreDrugs(@PathVariable("idPharmacy") String idPharmacy,
                                               @PathVariable("codeDrug") String code,
                                               @RequestBody BuyMoreDrugsPharmacyUseCaseRequest request) throws JsonProcessingException {
        buyMoreDrugsPharmacyInputPort.buyMoreDrugsPharmacy(idPharmacy, code, request);
        return ResponseEntity.ok().build();
    }

    @GetMapping("get-list-drug-by-parmacy/{idPharmacy}")
    public ResponseEntity<List<GetItemDrugDataByNamesLikeResponse>> getDrugsNameByParmacy(@PathVariable("idPharmacy") String idPharmacy) {
        List<GetItemDrugDataByNamesLikeResponse> responses = getListDrugDataByPharmacyInputPort.getDrugsName(idPharmacy);
        return ResponseEntity.ok(responses);
    }
}
