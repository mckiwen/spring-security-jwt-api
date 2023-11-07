package com.example.springsecurityjwtapi.security.service;

import com.example.springsecurityjwtapi.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.ArrayList;

/**
 * Authenticate a User from the database.
 *
 * Authentication Manager calls loadUserByUsername method of this class to get user details from the database
 * when a User tries to be authenticated.
 */
public class UserDetailsServiceImpl implements UserDetailsService {

    @Autowired
    UserRepository userRepository;
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
            User user = userRepository.findByUsername(username)
                    .orElseThrow(() -> new UsernameNotFoundException("User Not Found with username: " + username));

            return new org.springframework.security.core.userdetails.User(
                    user.getUsername(), user.getPassword(), new ArrayList<>());
    }
}
