package com.hanstack.linkedintool.service;

import com.hanstack.linkedintool.dto.UserDTO;
import com.hanstack.linkedintool.model.User;

import java.util.List;

public interface UserService {
    void saveUser(UserDTO userDto);

    User findByEmail(String email);

    List<UserDTO> findAllUsers();
}
