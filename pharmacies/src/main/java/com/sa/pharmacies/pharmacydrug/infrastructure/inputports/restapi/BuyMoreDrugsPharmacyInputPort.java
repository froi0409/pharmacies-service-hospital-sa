package com.sa.pharmacies.pharmacydrug.infrastructure.inputports.restapi;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.sa.pharmacies.pharmacydrug.application.buymoredrugspharmacy.BuyMoreDrugsPharmacyUseCaseRequest;
import jakarta.persistence.EntityNotFoundException;

public interface BuyMoreDrugsPharmacyInputPort {
    void buyMoreDrugsPharmacy(String idPharmacy, String code, BuyMoreDrugsPharmacyUseCaseRequest request) throws EntityNotFoundException, IllegalArgumentException, JsonProcessingException;
}
