package com.sa.pharmacies.drug.infrastructure.inputports.restapi;

import com.sa.pharmacies.common.exceptions.EntityAlreadyExistsException;
import com.sa.pharmacies.pharmacy.application.createdrug.CreateDrugRequest;

public interface CreateDrugInputPort {
    String createDrug(CreateDrugRequest request) throws IllegalArgumentException, EntityAlreadyExistsException;
}
