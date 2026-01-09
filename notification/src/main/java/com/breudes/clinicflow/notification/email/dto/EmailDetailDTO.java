package com.breudes.clinicflow.notification.email.dto;

public record EmailDetailDTO(
        String to,
        String subject,
        String body
) {
}