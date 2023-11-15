package com.example.springsecurityjwtapi.services;

import com.example.springsecurityjwtapi.entities.Role;

import java.util.Optional;

public interface RoleService {
    Optional<Role> findByName(String name);
}
