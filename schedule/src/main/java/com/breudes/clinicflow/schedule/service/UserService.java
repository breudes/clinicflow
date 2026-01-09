package com.breudes.clinicflow.schedule.service;

import com.breudes.clinicflow.schedule.dto.user.UserOutputDTO;
import com.breudes.clinicflow.schedule.entity.User;
import com.breudes.clinicflow.schedule.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import java.util.Objects;
import java.util.Optional;

import static com.breudes.clinicflow.schedule.service.util.AuthenticatedUserData.getAuthenticatedUserId;

@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private final BCryptPasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository, BCryptPasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public ResponseEntity<String> createUser(User user) {
        Optional<User> existingUserByEmail = userRepository.findByEmail(user.getEmail());
        if (existingUserByEmail.isPresent()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Error: Email is already registered.");
        }
        Optional<User> existingUserByUsername = userRepository.findByUsername(user.getUsername());
        if (existingUserByUsername.isPresent()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Error: Username is already registered.");
        }

        if(user.getUsername().isBlank()){
            return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).body("Alert: Username is required.");
        }
        if(user.getPassword().isBlank()){
            return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).body("Alert: Password is required.");
        }
        if(user.getEmail().isBlank()){
            return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).body("Alert: E-mail is required.");
        }
        if(user.getUserType().name().isBlank()){
            return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).body("Alert: User type is required.");
        }
        user.setActive(true);
        User resultUser = userRepository.save(user);
        UserOutputDTO resultUserDTO = UserOutputDTO.fromEntity(resultUser);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body("User created successfully.\n" + resultUserDTO);
    }

    public ResponseEntity<String> updateUser(Long id, UserOutputDTO userOutputDTO) {
        Optional<User> existingUser = userRepository.findById(id);
        // Password is not updated here
        if(existingUser.isPresent()){
            User user = existingUser.get();
            user.setName(userOutputDTO.name());
            user.setUsername(userOutputDTO.username());
            user.setEmail(userOutputDTO.email());
            user.setBirthDate(userOutputDTO.birthDate());
            user.setUserType(userOutputDTO.userType());
            user.setActive(true);
            user.setLicense(userOutputDTO.license());
            user.setSpecialty(userOutputDTO.specialty());

            userRepository.save(user);
            return ResponseEntity.status(200).body("User successfully updated.");
        }else{
            return ResponseEntity.status(404).body("User not found.");
        }
    }

    public ResponseEntity<String> deleteUser(Long id) {
        if(id == null){
            return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).body("Id not provided. Id is null.");
        }
        Long loggedUserId = Objects.requireNonNull(getAuthenticatedUserId()).getId();
        System.out.println(loggedUserId);
        if(Objects.equals(loggedUserId, id)){
            return ResponseEntity.status(403).body("You cannot delete yourself.");
        }
        Optional<User> user = userRepository.findById(id);

        if (user.isPresent()) {
            User foundUser = user.get();
            foundUser.setActive(false);
            userRepository.save(foundUser);

            if(!foundUser.getActive()){
                return ResponseEntity.status(200).body("User successfully deleted.");
            }else{
                return ResponseEntity.status(400).body("User already deleted.");
            }
        } else {
            return ResponseEntity.status(404).body("User not found.");
        }
    }

    public ResponseEntity<String> changePassword(Long userId, String oldPassword, String newPassword) {
        Optional<User> user = userRepository.findById(userId);
        if(user.isPresent()){
            User foundUser = user.get();
            if(!foundUser.getActive()){
                return ResponseEntity.status(400).body("User not active.");
            }
            if (!passwordEncoder.matches(oldPassword, foundUser.getPassword())) {
                throw new IllegalArgumentException("Old password is incorrect.");
            }
            foundUser.setPassword(passwordEncoder.encode(newPassword));
            userRepository.save(foundUser);
            return ResponseEntity.status(200).body("Password successfully updated.");
        } else {
            return ResponseEntity.status(404).body("User not found.");
        }
    }
}