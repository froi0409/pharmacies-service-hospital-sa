package com.sa.pharmacies.pharmacydrug.infrastructure.outputadapters.kafka;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class SendAllNotificationRequest {
    private String type;
    private String description;
}