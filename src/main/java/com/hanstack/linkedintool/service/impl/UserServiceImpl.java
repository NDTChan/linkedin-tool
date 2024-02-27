package com.hanstack.linkedintool.service.impl;

import com.hanstack.linkedintool.dto.UserDTO;
import com.hanstack.linkedintool.model.Role;
import com.hanstack.linkedintool.model.User;
import com.hanstack.linkedintool.repository.RoleRepository;
import com.hanstack.linkedintool.repository.UserRepository;
import com.hanstack.linkedintool.service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class UserServiceImpl implements UserService {

    private UserRepository userRepository;
    private RoleRepository roleRepository;
    private PasswordEncoder passwordEncoder;
    @Override
    public void saveUser(UserDTO userDto) {
        User user = new User();
        user.setName(userDto.getUserName());
        user.setEmail(userDto.getEmail());

        user.setPassword(passwordEncoder.encode(userDto.getPassword()));
        Role role = roleRepository.findByName("ROLE_ADMIN");
        if(role == null){
            role = checkRoleExist();
        }
        user.setRoles(List.of(role));
        userRepository.save(user);
    }

    @Override
    public User findByEmail(String email) {
        return userRepository.findByEmail(email);
    }


    private Role checkRoleExist() {
        Role role = new Role();
        role.setName("ROLE_ADMIN");
        return roleRepository.save(role);
    }
}
