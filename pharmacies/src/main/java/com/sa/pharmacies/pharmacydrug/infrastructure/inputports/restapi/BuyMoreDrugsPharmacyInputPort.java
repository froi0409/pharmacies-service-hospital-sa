package com.sa.pharmacies.pharmacydrug.infrastructure.inputports.restapi;

import com.sa.pharmacies.pharmacydrug.application.buymoredrugspharmacy.BuyMoreDrugsPharmacyUseCaseRequest;
import jakarta.persistence.EntityNotFoundException;

public interface BuyMoreDrugsPharmacyInputPort {
    void buyMoreDrugsPharmacy(String idPharmacy, String code, BuyMoreDrugsPharmacyUseCaseRequest request) throws EntityNotFoundException, IllegalArgumentException;
}
