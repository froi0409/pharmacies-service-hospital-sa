package com.sa.pharmacies.pharmacydrug.infrastructure.inputports.restapi;

import com.sa.pharmacies.pharmacydrug.application.removequantityfromhospitals.RemoveQuantityFromHospitalsRequest;
import com.sa.pharmacies.pharmacydrug.application.removequantityfromhospitals.RemoveQuantityFromHospitalsResponse;
import jakarta.persistence.EntityNotFoundException;

import java.util.List;

public interface RemoveQuantityFromHospitalsInputPort {
    List<RemoveQuantityFromHospitalsResponse> remove(List<RemoveQuantityFromHospitalsRequest> requests) throws EntityNotFoundException;
}
