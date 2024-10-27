package com.sa.pharmacies.pharmacy.infrastructure.inputports.restapi;

import com.sa.pharmacies.common.exceptions.EntityAlreadyExistsException;
import com.sa.pharmacies.pharmacy.domain.Pharmacy;

public interface CreatePharmacyByEventInputPort {
     void createPharmacyByEvent(String id) throws EntityAlreadyExistsException, IllegalArgumentException;
}
