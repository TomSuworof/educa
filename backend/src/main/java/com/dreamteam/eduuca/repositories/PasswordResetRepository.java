package com.dreamteam.eduuca.repositories;

import com.dreamteam.eduuca.entities.user.passwordreset.PasswordResetRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface PasswordResetRepository extends JpaRepository<PasswordResetRequest, UUID> {
    Optional<PasswordResetRequest> findByUsername(String username);
}
