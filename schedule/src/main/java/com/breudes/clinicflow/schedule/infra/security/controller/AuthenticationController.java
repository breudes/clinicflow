package com.breudes.clinicflow.schedule.infra.security.controller;

import com.breudes.clinicflow.schedule.entity.User;
import com.breudes.clinicflow.schedule.infra.security.data.TokenData;
import com.breudes.clinicflow.schedule.infra.security.data.UserAuthenticationData;
import com.breudes.clinicflow.schedule.infra.security.service.TokenService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/login")
public class AuthenticationController {
    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private TokenService tokenService;

    @PostMapping
    public ResponseEntity<TokenData> userLogin(@RequestBody @Valid UserAuthenticationData userAuthenticationData) {
        var authenticationToken = new UsernamePasswordAuthenticationToken(
                userAuthenticationData.username(),
                userAuthenticationData.password()
        );
        var authenticatedUser = authenticationManager.authenticate(authenticationToken);
        var JWTToken = tokenService.generateToken((User) authenticatedUser.getPrincipal());
        return ResponseEntity.ok(new TokenData(JWTToken));
    }
}