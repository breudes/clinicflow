package com.breudes.clinicflow.schedule.controller;

import com.breudes.clinicflow.schedule.dto.user.ChangeUserPasswordDTO;
import com.breudes.clinicflow.schedule.dto.user.UserOutputDTO;
import com.breudes.clinicflow.schedule.entity.User;
import com.breudes.clinicflow.schedule.repository.UserRepository;
import com.breudes.clinicflow.schedule.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/users")
public class UserController {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private UserService userService;

    @GetMapping
    public List<UserOutputDTO> listAllUsers() {
        List<User> users = userRepository.findAll();
        return users.stream()
                .map(UserOutputDTO::fromEntity)
                .collect(Collectors.toList());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteUser(@PathVariable Long id) {
        return userService.deleteUser(id);
    }

    @PostMapping
    public ResponseEntity<String> createUser(@RequestBody User user) {
        try{
            return userService.createUser(user);
        } catch (DataIntegrityViolationException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Error: invalid data.");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Unexpected error while creating the user.");
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<String> updateUser(@RequestBody UserOutputDTO userDTO){
        return userService.updateUser(userDTO.id(), userDTO);
    }

    @PatchMapping
    public ResponseEntity<String> changePassword(@RequestBody ChangeUserPasswordDTO changeUserPasswordDTO) {
        return userService.changePassword(
                changeUserPasswordDTO.id(),
                changeUserPasswordDTO.oldPassword(),
                changeUserPasswordDTO.newPassword()
        );
    }
}