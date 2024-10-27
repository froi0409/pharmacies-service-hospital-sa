package com.sa.pharmacies.pharmacydrug.infrastructure.inputports.restapi;

import com.sa.pharmacies.common.exceptions.EntityAlreadyExistsException;
import com.sa.pharmacies.pharmacy.application.assignpharmacydrug.AssignPharmacyDrugRequest;

public interface AssignPharmacyDrugInputPort {
    void assign(AssignPharmacyDrugRequest request) throws EntityAlreadyExistsException, IllegalArgumentException;
}
