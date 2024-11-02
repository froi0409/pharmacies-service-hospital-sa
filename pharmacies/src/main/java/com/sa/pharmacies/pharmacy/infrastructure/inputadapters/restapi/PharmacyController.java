package com.sa.pharmacies.pharmacy.infrastructure.inputadapters.restapi;

import com.sa.pharmacies.common.annotation.WebAdapter;
import com.sa.pharmacies.common.exceptions.EntityAlreadyExistsException;
import com.sa.pharmacies.drug.infrastructure.inputports.restapi.CreateDrugInputPort;
import com.sa.pharmacies.drug.infrastructure.inputports.restapi.ExistsDrugInputPort;
import com.sa.pharmacies.drug.infrastructure.inputports.restapi.GetListDrugDataByCodesInputPort;
import com.sa.pharmacies.drug.infrastructure.inputports.restapi.GetListDrugDataByNamesLikeInputPort;
import com.sa.pharmacies.pharmacy.application.assignpharmacydrug.AssignPharmacyDrugRequest;
import com.sa.pharmacies.pharmacy.application.createdrug.CreateDrugRequest;
import com.sa.pharmacies.pharmacy.application.getlistdrugdatabycodes.GetItemDrugDataByCodesResponse;
import com.sa.pharmacies.pharmacy.application.getlistdrugdatabycodes.GetListDrugDataByCodesRequest;
import com.sa.pharmacies.pharmacy.application.getlistdrugdatabynameslike.GetItemDrugDataByNamesLikeResponse;
import com.sa.pharmacies.pharmacy.application.getlistdrugdatabynameslike.GetListDrugDataByNamesLikeUseCase;
import com.sa.pharmacies.pharmacy.domain.Pharmacy;
import com.sa.pharmacies.pharmacy.infrastructure.inputports.restapi.CreatePharmacyByEventInputPort;
import com.sa.pharmacies.pharmacy.infrastructure.inputports.restapi.ExistsPharmacyInputPort;
import com.sa.pharmacies.pharmacydrug.infrastructure.inputports.restapi.AssignPharmacyDrugInputPort;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@WebAdapter
@RestController
@RequestMapping("api/v1/pharmacies")
@SecurityRequirement(name = "bearerAuth")
public class PharmacyController {
    private final CreatePharmacyByEventInputPort createPharmacyByEventInputPort;
    private final CreateDrugInputPort createDrugInputPort;
    private final GetListDrugDataByCodesInputPort getListDrugDataByCodesInputPort;
    private final AssignPharmacyDrugInputPort assignPharmacyDrugInputPort;
    private final ExistsPharmacyInputPort existsPharmacyInputPort;
    private final GetListDrugDataByNamesLikeInputPort getListDrugDataByNamesLikeInputPort;
    private final ExistsDrugInputPort existsDrugInputPort;

    @Autowired
    public PharmacyController(CreatePharmacyByEventInputPort createPharmacyByEventInputPort, CreateDrugInputPort createDrugInputPort, GetListDrugDataByCodesInputPort getListDrugDataByCodesInputPort, AssignPharmacyDrugInputPort assignPharmacyDrugInputPort, ExistsPharmacyInputPort existsPharmacyInputPort, GetListDrugDataByNamesLikeInputPort getListDrugDataByNamesLikeInputPort, ExistsDrugInputPort existsDrugInputPort) {
        this.createPharmacyByEventInputPort = createPharmacyByEventInputPort;
        this.createDrugInputPort = createDrugInputPort;
        this.getListDrugDataByCodesInputPort = getListDrugDataByCodesInputPort;
        this.assignPharmacyDrugInputPort = assignPharmacyDrugInputPort;
        this.existsPharmacyInputPort = existsPharmacyInputPort;
        this.getListDrugDataByNamesLikeInputPort = getListDrugDataByNamesLikeInputPort;
        this.existsDrugInputPort = existsDrugInputPort;
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

    @PostMapping("drug/get-list-drug-data-by-names-like/{name}")
    public ResponseEntity<List<GetItemDrugDataByNamesLikeResponse>> getListDrugDataByNamesLike(
            @PathVariable String name
    ){
        List<GetItemDrugDataByNamesLikeResponse> response = getListDrugDataByNamesLikeInputPort.getDrugNamesLike(name);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @PostMapping("get-list-drug-data-by-codes")
    public ResponseEntity<List<GetItemDrugDataByCodesResponse>> getListDrugDataByCodes(
            @RequestBody GetListDrugDataByCodesRequest request
            ){
        List<GetItemDrugDataByCodesResponse> response = getListDrugDataByCodesInputPort.getList(request);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @PostMapping("assign/pharmacy-drug")
    public ResponseEntity<String> assignPharmacyDrugInputPort(@RequestBody AssignPharmacyDrugRequest request) throws EntityAlreadyExistsException {
        assignPharmacyDrugInputPort.assign(request);
        return ResponseEntity.status(HttpStatus.CREATED).body("Drug assign to this pharmacy");
    }

    @RequestMapping(method = RequestMethod.HEAD, path = "/pharmacy/{id}")
    public ResponseEntity<Void> checkClientExists(@RequestParam("id") String id) {
        if(existsPharmacyInputPort.getPharmacyById(id).isPresent()){
            return ResponseEntity.status(HttpStatus.OK).build();
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }

    @RequestMapping(method = RequestMethod.HEAD, path = "/drug/{code}")
    public ResponseEntity<Void> checkDrugExists(@RequestParam("code") String code) {
        if (existsDrugInputPort.findByCode(code).isPresent()){
            return ResponseEntity.status(HttpStatus.OK).build();
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }

}
