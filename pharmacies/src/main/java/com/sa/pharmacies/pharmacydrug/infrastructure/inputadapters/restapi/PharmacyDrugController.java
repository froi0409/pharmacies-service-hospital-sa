package com.sa.pharmacies.pharmacydrug.infrastructure.inputadapters.restapi;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.itextpdf.text.DocumentException;
import com.sa.pharmacies.common.annotation.WebAdapter;
import com.sa.pharmacies.pharmacy.application.getlistdrugdatabynameslike.GetItemDrugDataByNamesLikeResponse;
import com.sa.pharmacies.pharmacy.application.getlistdrugdatabypharmacy.GetListDrugDataByPharmacyResponse;
import com.sa.pharmacies.pharmacydrug.application.buymoredrugspharmacy.BuyMoreDrugsPharmacyUseCaseRequest;
import com.sa.pharmacies.pharmacydrug.application.selldrugstouser.SellDrugsToUserUseCase;
import com.sa.pharmacies.pharmacydrug.application.selldrugstouser.SellDrugsToUserUseCaseRequest;
import com.sa.pharmacies.pharmacydrug.infrastructure.inputports.restapi.BuyMoreDrugsPharmacyInputPort;
import com.sa.pharmacies.pharmacydrug.infrastructure.inputports.restapi.GetListDrugDataByPharmacyInputPort;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;

@WebAdapter
@RestController
@RequestMapping("api/v1/pharmacies-drugs")
@SecurityRequirement(name = "bearerAuth")
public class PharmacyDrugController {
    private final BuyMoreDrugsPharmacyInputPort buyMoreDrugsPharmacyInputPort;
    private final GetListDrugDataByPharmacyInputPort getListDrugDataByPharmacyInputPort;
    private final SellDrugsToUserUseCase sellDrugsToUserUseCase;

    @Autowired
    public PharmacyDrugController(BuyMoreDrugsPharmacyInputPort buyMoreDrugsPharmacyInputPort, GetListDrugDataByPharmacyInputPort getListDrugDataByPharmacyInputPort, SellDrugsToUserUseCase sellDrugsToUserUseCase) {
        this.buyMoreDrugsPharmacyInputPort = buyMoreDrugsPharmacyInputPort;
        this.getListDrugDataByPharmacyInputPort = getListDrugDataByPharmacyInputPort;
        this.sellDrugsToUserUseCase = sellDrugsToUserUseCase;
    }

    @PostMapping("buy-more/{idPharmacy}/{codeDrug}")
    public ResponseEntity<String> buyMoreDrugs(@PathVariable("idPharmacy") String idPharmacy,
                                               @PathVariable("codeDrug") String code,
                                               @RequestBody BuyMoreDrugsPharmacyUseCaseRequest request) throws JsonProcessingException {
        buyMoreDrugsPharmacyInputPort.buyMoreDrugsPharmacy(idPharmacy, code, request);
        return ResponseEntity.ok().build();
    }

    @GetMapping("get-list-drug-by-pharmacy/{idPharmacy}")
    public ResponseEntity<List<GetListDrugDataByPharmacyResponse>> getDrugsNameByParmacy(@PathVariable("idPharmacy") String idPharmacy) {
        List<GetListDrugDataByPharmacyResponse> responses = getListDrugDataByPharmacyInputPort.getDrugsName(idPharmacy);
        return ResponseEntity.ok(responses);
    }

    @PostMapping("sell-drugs=by-pharmacy/{idPharmacy}/{idUser}/{idEmployee}")
    public HttpEntity<byte[]> payBillPharmacy (
            @PathVariable String idPharmacy,
            @PathVariable String idUser,
            @PathVariable String idEmployee,
            @RequestBody SellDrugsToUserUseCaseRequest bill) throws DocumentException, IOException{

        byte[] data = sellDrugsToUserUseCase.sellDrugs(idPharmacy, idUser, idEmployee, bill);

        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=bill.pdf");
        headers.add(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_PDF_VALUE);

        return new HttpEntity<>(data, headers);
    }
}
