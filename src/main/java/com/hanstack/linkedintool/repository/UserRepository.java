package com.hanstack.linkedintool.repository;

import com.hanstack.linkedintool.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
     User findByEmail(String email);

}
