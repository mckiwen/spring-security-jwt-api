package com.example.springsecurityjwtapi.services;

import com.example.springsecurityjwtapi.dtos.RegisterDTO;
import com.example.springsecurityjwtapi.entities.UserEntity;

import java.util.Optional;

public interface UserService {
    Optional<UserEntity> findByUsername(String username);
    Boolean existsByUsername(String username);

    void save(RegisterDTO registerDTO);
}
