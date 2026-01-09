package com.breudes.clinicflow.schedule.infra.security;

import com.breudes.clinicflow.schedule.infra.security.exception.UserAccessDeniedHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfigurations {
    @Autowired
    private SecurityFilter securityFilter;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
        return httpSecurity.csrf(AbstractHttpConfigurer::disable)
                .sessionManagement(sm -> sm.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests((authorizeHttpRequests) -> authorizeHttpRequests
                        .requestMatchers(HttpMethod.POST, "/login").permitAll()
                        // all users can change user's password
                        .requestMatchers(HttpMethod.PATCH, "/users").permitAll()
                        // patient can see appointments (but only related to them)
                        .requestMatchers(HttpMethod.GET, "/appointments/**").permitAll()
                        // only doctor and nurse can create users and appointments
                        .requestMatchers(HttpMethod.POST, "/users").hasAnyRole("DOCTOR", "NURSE")
                        .requestMatchers(HttpMethod.POST, "/appointments").hasAnyRole("DOCTOR", "NURSE")
                        // other requests are limited to doctor and nurse as well
                        .requestMatchers("/users/**").hasAnyRole( "DOCTOR", "NURSE")
                        .requestMatchers("/appointments/**").hasAnyRole("DOCTOR", "NURSE")
                        .anyRequest().permitAll()
                )
                .addFilterBefore(securityFilter, UsernamePasswordAuthenticationFilter.class)
                .exceptionHandling(ex -> ex.accessDeniedHandler(new UserAccessDeniedHandler()))
                .build();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration)
            throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}

