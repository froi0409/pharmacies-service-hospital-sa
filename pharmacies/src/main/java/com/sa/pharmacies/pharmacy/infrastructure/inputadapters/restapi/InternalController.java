package com.sa.pharmacies.pharmacy.infrastructure.inputadapters.restapi;

import com.sa.pharmacies.common.annotation.WebAdapter;
import com.sa.pharmacies.pharmacy.application.getlistdrugdatabynameslike.GetItemDrugDataByNamesLikeResponse;
import com.sa.pharmacies.pharmacydrug.application.removequantityfromhospitals.RemoveQuantityFromHospitalsRequest;
import com.sa.pharmacies.pharmacydrug.application.removequantityfromhospitals.RemoveQuantityFromHospitalsResponse;
import com.sa.pharmacies.pharmacydrug.infrastructure.inputports.restapi.RemoveQuantityFromHospitalsInputPort;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@WebAdapter
@RestController
@RequestMapping("api/v1/pharmacies")
@SecurityRequirement(name = "bearerAuth")
public class InternalController {
    private final RemoveQuantityFromHospitalsInputPort removeQuantityFromHospitalsInputPort;

    @Autowired
    public InternalController(RemoveQuantityFromHospitalsInputPort removeQuantityFromHospitalsInputPort) {
        this.removeQuantityFromHospitalsInputPort = removeQuantityFromHospitalsInputPort;
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'PHARMACY', 'RECEPTIONIST')")
    @PostMapping("drug/get-list-drugs-currency-hospital")
    public ResponseEntity<List<RemoveQuantityFromHospitalsResponse>> removeQuantityFromHospitals(
            @RequestBody List<RemoveQuantityFromHospitalsRequest> requests
    ){
        List<RemoveQuantityFromHospitalsResponse> response = removeQuantityFromHospitalsInputPort.remove(requests);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }
}
