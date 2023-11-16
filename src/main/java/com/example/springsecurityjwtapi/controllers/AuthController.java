package com.example.springsecurityjwtapi.controllers;

import com.example.springsecurityjwtapi.dtos.AuthResponseDTO;
import com.example.springsecurityjwtapi.dtos.LoginDTO;
import com.example.springsecurityjwtapi.dtos.RegisterDTO;
import com.example.springsecurityjwtapi.entities.Role;
import com.example.springsecurityjwtapi.entities.UserEntity;
import com.example.springsecurityjwtapi.repositories.RoleRepository;
import com.example.springsecurityjwtapi.repositories.UserRepository;
import com.example.springsecurityjwtapi.security.JwtGenerator;
import com.example.springsecurityjwtapi.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collections;


@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private AuthenticationManager authenticationManager;
    private UserService userService;
    private JwtGenerator jwtGenerator;

    @Autowired
    public AuthController(AuthenticationManager authenticationManager, UserService userService, JwtGenerator jwtGenerator) {
        this.authenticationManager = authenticationManager;
        this.userService = userService;
        this.jwtGenerator = jwtGenerator;
    }


    @PostMapping("/login")
    public ResponseEntity<AuthResponseDTO> login(@RequestBody LoginDTO loginDTO){
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginDTO.getUsername(), loginDTO.getPassword()));
        SecurityContextHolder.getContext().setAuthentication(authentication);
        String token = jwtGenerator.generateToken(authentication);
        return ResponseEntity.ok().body(new AuthResponseDTO(token));
    }


    @PostMapping("/register")
    public ResponseEntity<UserEntity> register(@RequestBody RegisterDTO registerDTO){
        if(userService.existsByUsername(registerDTO.getUsername())){
            return ResponseEntity.badRequest().build();
        }
        UserEntity user = userService.save(registerDTO);
        return ResponseEntity.ok().body(user);
    }
}
