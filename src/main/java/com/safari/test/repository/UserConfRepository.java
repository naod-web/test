package com.safari.test.repository;




import com.safari.test.entity.UserConf;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserConfRepository extends JpaRepository<UserConf, Long> {
    Optional<UserConf> findByEid(String eid);
    Optional<UserConf> findByEmail(String email);

    Page<UserConf> findByNameContainingIgnoreCaseOrEmailContainingIgnoreCase(
            String name, String email, Pageable pageable);

    Page<UserConf> findByRoleContainingIgnoreCase(String role, Pageable pageable);
    Page<UserConf> findByStatusContainingIgnoreCase(String status, Pageable pageable);
}