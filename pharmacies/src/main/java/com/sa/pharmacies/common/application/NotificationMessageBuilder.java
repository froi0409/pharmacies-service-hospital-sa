package com.sa.pharmacies.common.application;

import com.sa.pharmacies.drug.domain.Drug;

public class NotificationMessageBuilder {
    public static String createMinimumQuantityMessage(Drug drug, int quantity) {
        return String.format("Attention: The minimum quantity for the drug '%s' (Code: %s) has been reached or is below the threshold. Current quantity: %d. Please restock as soon as possible.",
                drug.getName(), drug.getCode(), quantity);
    }
}
