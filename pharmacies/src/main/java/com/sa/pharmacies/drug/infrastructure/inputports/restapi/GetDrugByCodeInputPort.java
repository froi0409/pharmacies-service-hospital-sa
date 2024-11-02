package com.sa.pharmacies.drug.infrastructure.inputports.restapi;

import com.sa.pharmacies.pharmacy.application.getdrugbycode.GetDrugByCodeResponse;
import jakarta.persistence.EntityNotFoundException;


public interface GetDrugByCodeInputPort {
    GetDrugByCodeResponse getDrugByCode(String code) throws EntityNotFoundException;
}
