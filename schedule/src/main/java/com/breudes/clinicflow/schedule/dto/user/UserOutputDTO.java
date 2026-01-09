package com.breudes.clinicflow.schedule.dto.user;

import com.breudes.clinicflow.schedule.entity.User;
import com.breudes.clinicflow.schedule.entity.enums.UserType;

import java.time.LocalDate;

public record UserOutputDTO(
        Long id,
        String name,
        String username,
        String email,
        LocalDate birthDate,
        UserType userType,
        Boolean active,
        String license,
        String specialty
) {
    public static UserOutputDTO fromEntity(User user) {
        return new UserOutputDTO(
                user.getId(),
                user.getName(),
                user.getUsername(),
                user.getEmail(),
                user.getBirthDate(),
                user.getUserType(),
                user.getActive(),
                user.getLicense(),
                user.getSpecialty()
        );
    }

    public String toString(){
        return "UserDTO{ " +
                "name='" + name + '\'' +
                ", username='" + username + '\'' +
                ", email='" + email + '\'' +
                ", birthDate='" + birthDate + '\'' +
                ", userType =" + userType +
                ", active=" + active +
                '}';

    }
}
