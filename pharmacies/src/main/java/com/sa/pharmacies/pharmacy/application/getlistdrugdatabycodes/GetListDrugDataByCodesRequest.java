package com.sa.pharmacies.pharmacy.application.getlistdrugdatabycodes;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.Value;

import java.util.List;

@Value
@NoArgsConstructor(force = true)
@AllArgsConstructor
public class GetListDrugDataByCodesRequest {
    List<String> codes;
}
