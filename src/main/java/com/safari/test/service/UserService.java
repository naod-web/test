package com.safari.test.service;



import com.safari.test.dto.UserRegistration;
import com.safari.test.entity.UserConf;

import com.safari.test.repository.UserConfRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@Transactional
public class UserService {

    private final UserConfRepository userRepository;

    // Inject PasswordEncoder
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public UserService(UserConfRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    // Register a new user → ALWAYS encrypt password
    public UserConf registerUser(UserRegistration dto) {
        if (userRepository.findByEid(dto.getEid()).isPresent()) {
            throw new IllegalArgumentException("User EID already exists");
        }

        if (userRepository.findByEmail(dto.getEmail()).isPresent()) {
            throw new IllegalArgumentException("Email already exists");
        }

        UserConf user = new UserConf();
        mapDtoToEntity(user, dto, true); // isNew = true

        // CRITICAL: Always encrypt password on registration
        if (dto.getPassword() != null && !dto.getPassword().isBlank()) {
            user.setPassword(passwordEncoder.encode(dto.getPassword()));
        } else {
            throw new IllegalArgumentException("Password is required for new user");
        }

        return userRepository.save(user);
    }

    // Update existing user → encrypt password only if it's being changed
    public UserConf updateUser(String eid, UserRegistration dto) {
        UserConf existing = userRepository.findByEid(eid)
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (dto.getEmail() != null && !dto.getEmail().equals(existing.getEmail())) {
            if (userRepository.findByEmail(dto.getEmail()).isPresent()) {
                throw new IllegalArgumentException("Email already exists");
            }
        }

        mapDtoToEntity(existing, dto, false); // isNew = false

        // Only update (and encrypt) password if a new one is provided
        if (dto.getPassword() != null && !dto.getPassword().isBlank()) {
            existing.setPassword(passwordEncoder.encode(dto.getPassword()));
        }
        // If password is null or empty → keep old (already encrypted) password

        return userRepository.save(existing);
    }

    // Update user status (unchanged)
    @Transactional
    public UserConf updateUserStatus(String eid, String status) {
        UserConf user = userRepository.findByEid(eid)
                .orElseThrow(() -> new RuntimeException("User not found"));
        user.setStatus(status.toUpperCase());
        return userRepository.save(user);
    }

    // ... rest of your methods unchanged ...

    // Get users with pagination & search
    @Transactional(readOnly = true)
    public Page<UserConf> getAllUsers(Pageable pageable, String search) {
        if (search == null || search.isEmpty()) {
            return userRepository.findAll(pageable);
        }
        return userRepository.findByNameContainingIgnoreCaseOrEmailContainingIgnoreCase(search, search, pageable);
    }

    @Transactional(readOnly = true)
    public Optional<UserConf> getUserByEid(String eid) {
        return userRepository.findByEid(eid);
    }

    @Transactional(readOnly = true)
    public Optional<UserConf> getUserByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    @Transactional(readOnly = true)
    public Page<UserConf> getUsersByRole(String role, Pageable pageable) {
        return userRepository.findByRoleContainingIgnoreCase(role, pageable);
    }

    @Transactional(readOnly = true)
    public Page<UserConf> getUsersByStatus(String status, Pageable pageable) {
        return userRepository.findByStatusContainingIgnoreCase(status, pageable);
    }

    public void deleteUser(String eid) {
        UserConf user = userRepository.findByEid(eid)
                .orElseThrow(() -> new RuntimeException("User not found"));
        userRepository.delete(user);
    }

    // Updated mapper: no longer sets plain password directly
    private UserConf mapDtoToEntity(UserConf user, UserRegistration dto, boolean isNew) {
        if (isNew) {
            user.setStatus(dto.getStatus() != null ? dto.getStatus().toUpperCase() : "ACTIVE");
            Optional.ofNullable(dto.getEid()).ifPresent(user::setEid);
        }

        Optional.ofNullable(dto.getName()).ifPresent(user::setName);
        Optional.ofNullable(dto.getEmail()).ifPresent(user::setEmail);
        Optional.ofNullable(dto.getStatus()).ifPresent(status -> user.setStatus(status.toUpperCase()));



        return user;
    }
}

