package com.breudes.clinicflow.schedule.dto.user;

public record ChangeUserPasswordDTO(
        Long id,
        String oldPassword,
        String newPassword
) {
}
