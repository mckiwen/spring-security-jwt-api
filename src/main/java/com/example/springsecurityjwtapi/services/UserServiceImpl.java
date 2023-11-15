package com.example.springsecurityjwtapi.services;

import com.example.springsecurityjwtapi.dtos.RegisterDTO;
import com.example.springsecurityjwtapi.entities.Role;
import com.example.springsecurityjwtapi.entities.UserEntity;
import com.example.springsecurityjwtapi.repositories.RoleRepository;
import com.example.springsecurityjwtapi.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements UserDetailsService, UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleService roleService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        UserEntity user = findByUsername(username).orElseThrow(() -> new UsernameNotFoundException("Username not found"));
        return new User(user.getUsername(), user.getPassword(), mapRolesToAuthorities(user.getRoles()));
    }

    /**
     * This method takes a list of Roles and map it into a Collection of GrantedAuthority
     * to define roles and permissions of users then.
     * @param roles
     * @return
     */
    private Collection<GrantedAuthority> mapRolesToAuthorities(List<Role> roles){
        return roles.stream().map(role -> new SimpleGrantedAuthority(role.getName())).collect(Collectors.toList());

    }

    @Override
    public Optional<UserEntity> findByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    @Override
    public Boolean existsByUsername(String username) {
        return userRepository.existsByUsername(username);
    }

    @Override
    public void save(RegisterDTO registerDTO) {
        UserEntity user = new UserEntity();
        user.setUsername(registerDTO.getUsername());
        user.setPassword(passwordEncoder.encode(registerDTO.getPassword()));

        Role roles = roleService.findByName("USER").get();
        user.setRoles(Collections.singletonList(roles));

        userRepository.save(user);
    }

}
