package com.dreamteam.eduuca.controllers;

import com.dreamteam.eduuca.config.JwtUtils;
import com.dreamteam.eduuca.entities.User;
import com.dreamteam.eduuca.payload.request.LoginRequest;
import com.dreamteam.eduuca.payload.request.SignupRequest;
import com.dreamteam.eduuca.payload.response.JwtResponse;
import com.dreamteam.eduuca.payload.response.UserDTO;
import com.dreamteam.eduuca.services.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import javax.persistence.EntityNotFoundException;
import java.util.Set;
import java.util.stream.Collectors;

@Log4j2
@Controller
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {
    private final AuthenticationManager authenticationManager;
    private final UserService userService;
    private final JwtUtils jwtUtils;

    @PostMapping("/login")
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public ResponseEntity<JwtResponse> authenticateUser(@RequestBody LoginRequest loginRequest) {
        log.debug("authenticateUser() called. Login request cannot be displayed");

        User user;

        try {
            user = userService.loadUserByEmail(loginRequest.getEmail());
        } catch (EntityNotFoundException e) {
            throw new SecurityException("User does not exist", e);
        }

        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        user.getUsername(),
                        loginRequest.getPassword())
        );

        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwt = jwtUtils.generateJwtToken(authentication);

        Set<String> roles = user
                .getAuthorities()
                .stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.toSet());

        return ResponseEntity.ok().body(new JwtResponse(jwt, user.getId(), user.getUsername(), user.getEmail(), roles));
    }

    @PostMapping("/signup")
    @ResponseStatus(HttpStatus.OK)
    public @ResponseBody ResponseEntity<UserDTO> registerUser(@RequestBody SignupRequest signUpRequest) {
        log.debug("registerUser() called. User info cannot be displayed");
        UserDTO user = userService.saveUser(signUpRequest);
        return ResponseEntity.ok().body(user);
    }
}
