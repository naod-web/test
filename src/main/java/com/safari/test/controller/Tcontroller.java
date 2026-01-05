

package com.safari.test.controller;



import com.safari.test.dto.LoginRequest;
import com.safari.test.dto.UserRegistration;
import com.safari.test.repository.UserConfRepository;
import com.safari.test.service.UserService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import org.springframework.jdbc.core.JdbcTemplate;
import tools.jackson.databind.ObjectMapper;

import java.util.*;

@CrossOrigin(
        origins = "http://localhost:8084",
        allowCredentials = "true",
        maxAge = 3600
)
@RestController

public class Tcontroller {


    private final UserConfRepository userRepository;
    //private final PasswordEncoder passwordEncoder;
    private final ObjectMapper objectMapper;
    //private final LdapUserRepository ldapUserRepository;
    // private final JasyptReEncrypt jasyptReEncrypt = new JasyptReEncrypt();

    private final JdbcTemplate jdbcTemplate;

    @Autowired
    private UserService userService;

    @Autowired
    private AuthenticationManager authenticationManager;
//

    /// /    @Autowired
    /// /    private UserDetailsServiceImpl userDetailsService;
//
//    @Autowired
//    private JwtUtil jwtUtil;
    public Tcontroller(
            UserConfRepository userRepository, ObjectMapper objectMapper,
            JdbcTemplate jdbcTemplate) {
//        this.qrService = qrService;
        // this.service = service;
        this.objectMapper = objectMapper;
        this.userRepository = userRepository;

        this.jdbcTemplate = jdbcTemplate;
    }

    // ================= Helper Methods =================
    private String getCurrentUsername() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return authentication.getName();
    }


    // ================= Login Endpoint =================
    @PostMapping("/login")
    public ResponseEntity<?> authenticateUser(@Valid @RequestBody LoginRequest loginRequest) {
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            loginRequest.getEmail(),
                            loginRequest.getPassword()
                    )
//            loginRequest.getEmail(),loginRequest.getPassword()
            );
        } catch (BadCredentialsException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(Map.of("error", "Invalid email or password"));
        }

//        final UserDetails userDetails = userDetailsService.loadUserByUsername(loginRequest.getEmail());
//        final String accessToken = jwtUtil.generateToken(userDetails);

//        final String refreshToken = jwtUtil.generateRefreshToken(userDetails);

        User user = userRepository.findByEmail(loginRequest.getEmail()).orElseThrow(() -> new UsernameNotFoundException(
                        "User Not Found with email: " + loginRequest.getEmail()));

        return ResponseEntity.ok(Map.of(
//                "email", userService.getEmail(),
//                "email", user
//                "name", user.getName(),
//                "status", user.getStatus()
        ));
    }


    @PostMapping("/users")
    public ResponseEntity<?> registerQrUser(@Valid @RequestBody UserRegistration dto) {
        if (userRepository.existsByEid(dto.getEid())) {
            return ResponseEntity.badRequest().body(Map.of("error", "EID already exists"));
        }
        if (userRepository.existsByEmail(dto.getEmail())) {
            return ResponseEntity.badRequest().body(Map.of("error", "Email already exists"));
        }

//        User user = new User();
        User user = new User();
        user.setEid(dto.getEid());
        user.setName(dto.getName());
        user.setEmail(dto.getEmail());


//        user.setStatus(dto.getStatus());
//        user.setPassword(passwordEncoder.encode(dto.getPassword()));
//        userRepository.save(user);
        userRepository.save(user);

        return ResponseEntity.ok(Map.of("message", "User " + user.getName() + " registered successfully!"));
    }


}