package com.sa.pharmacies.pharmacydrug.infrastructure.outputports.restapi;

import com.sa.pharmacies.pharmacydrug.application.selldrugstouser.PayPharmacyRequest;

public interface BillPharmacyDrugOutputPort {
    byte[]  SendBillPharmacyDrug(String idPharmacy, String idUser, String idEmployee,PayPharmacyRequest payPharmacyRequest);
}
