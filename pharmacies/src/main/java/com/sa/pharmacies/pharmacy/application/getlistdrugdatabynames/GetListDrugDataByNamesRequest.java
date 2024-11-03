package com.sa.pharmacies.pharmacy.application.getlistdrugdatabynames;

import lombok.NoArgsConstructor;
import lombok.Value;

import java.util.List;

@Value
@NoArgsConstructor(force = true)
public class GetListDrugDataByNamesRequest {
    List<String> names;
}
