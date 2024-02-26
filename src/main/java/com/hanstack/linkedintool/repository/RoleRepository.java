package com.hanstack.linkedintool.repository;

import com.hanstack.linkedintool.model.Role;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoleRepository extends JpaRepository<Role, Long> {
    Role findByName(String name);
}
