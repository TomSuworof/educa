package com.dreamteam.eduuca.repositories;

import com.dreamteam.eduuca.entities.user.role.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {
}