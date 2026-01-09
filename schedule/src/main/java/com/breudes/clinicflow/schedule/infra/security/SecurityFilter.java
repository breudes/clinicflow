package com.breudes.clinicflow.schedule.infra.security;

import com.breudes.clinicflow.schedule.entity.User;
import com.breudes.clinicflow.schedule.infra.security.service.TokenService;
import com.breudes.clinicflow.schedule.repository.UserRepository;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import java.io.IOException;
import java.util.Optional;

@Component
public class SecurityFilter extends OncePerRequestFilter {
    @Autowired
    private TokenService tokenService;
    @Autowired
    private UserRepository userRepository;

    public SecurityFilter(TokenService tokenService, UserRepository userRepository) {
        this.tokenService = tokenService;
        this.userRepository = userRepository;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request,@NonNull HttpServletResponse response, @NonNull FilterChain filterChain)
            throws ServletException, IOException {
        var authHeader = request.getHeader("Authorization");

        if (authHeader != null) {
            var token = authHeader.replace("Bearer ", "");
            String username = tokenService.getSubject(token);
            if (!username.isBlank()) {
                Optional<User> user = userRepository.findByUsername(username);
                UsernamePasswordAuthenticationToken authentication;
                if (user.isPresent()) {
                    authentication = new UsernamePasswordAuthenticationToken(
                            user,
                            null,
                            user.get().getAuthorities()
                    );
                } else {
                    throw new RuntimeException("User not found to authenticate.");
                }
                SecurityContextHolder.getContext().setAuthentication(authentication);
            }
        }
        filterChain.doFilter(request, response);
    }
}