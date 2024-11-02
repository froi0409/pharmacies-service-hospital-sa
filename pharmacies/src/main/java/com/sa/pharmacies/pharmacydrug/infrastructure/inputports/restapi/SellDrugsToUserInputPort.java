package com.sa.pharmacies.pharmacydrug.infrastructure.inputports.restapi;

import com.sa.pharmacies.pharmacydrug.application.selldrugstouser.SellDrugsToUserUseCaseRequest;
import jakarta.persistence.EntityNotFoundException;

public interface SellDrugsToUserInputPort {
    byte[] sellDrugs(String idPharmacy, String idUser, String idEmployee, SellDrugsToUserUseCaseRequest request) throws IllegalArgumentException, EntityNotFoundException;
}
