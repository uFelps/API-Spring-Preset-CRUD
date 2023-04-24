package com.spring.bootcamp.repositories;

import com.spring.bootcamp.entities.Role;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoleRepository extends JpaRepository<Role, Long> {
}
