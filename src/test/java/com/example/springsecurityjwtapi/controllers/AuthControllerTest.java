package com.example.springsecurityjwtapi.controllers;

import com.example.springsecurityjwtapi.dtos.AuthResponseDTO;
import com.example.springsecurityjwtapi.dtos.LoginDTO;
import com.example.springsecurityjwtapi.dtos.RegisterDTO;
import com.example.springsecurityjwtapi.entities.UserEntity;
import com.example.springsecurityjwtapi.repositories.RoleRepository;
import com.example.springsecurityjwtapi.repositories.UserRepository;
import com.example.springsecurityjwtapi.security.JwtGenerator;
import com.example.springsecurityjwtapi.services.UserService;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.*;
import org.springframework.security.core.userdetails.User;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.swing.text.html.parser.Entity;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@RequestMapping("/api/auth")
public class AuthControllerTest {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserService userService;

    @Autowired
    private RoleRepository roleRepository;

    private TestRestTemplate testRestTemplate;

    @Autowired
    private RestTemplateBuilder restTemplateBuilder;

    @Autowired
    private JwtGenerator jwtGenerator;

    @LocalServerPort
    private int port;

    @BeforeEach
    void setUp() {
        restTemplateBuilder = restTemplateBuilder.rootUri("http://localhost:" + port);
        testRestTemplate = new TestRestTemplate(restTemplateBuilder);
    }

    @DisplayName("POST /api/auth/register")
    @Test
    @Order(1)
    void register(){
        RegisterDTO registerDto = new RegisterDTO();
        registerDto.setUsername("user");
        registerDto.setPassword("1234");

        UserEntity user = new UserEntity();
        user.setUsername(registerDto.getUsername());
        user.setPassword(registerDto.getPassword());

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON); // enviamos JSON
        HttpEntity<UserEntity> request = new HttpEntity<>(user,headers);
        ResponseEntity<UserEntity> response = testRestTemplate.exchange(
                "/api/auth/register",
                HttpMethod.POST,
                request,
                UserEntity.class);

        UserEntity result = response.getBody();
        System.out.println(result);
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @DisplayName("POST /api/auth/login")
    @Test
    @Order(2)
    void login(){
        LoginDTO loginDto = new LoginDTO();
        loginDto.setUsername("user");
        loginDto.setPassword("1234");

        UserEntity user = new UserEntity();
        user.setUsername(loginDto.getUsername());
        user.setPassword(loginDto.getPassword());

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON); // enviamos JSON
        headers.setAccept(List.of(MediaType.APPLICATION_JSON)); // Recibimos una lista JSON
        HttpEntity<UserEntity> request = new HttpEntity<>(user,headers);
        ResponseEntity<AuthResponseDTO> response = testRestTemplate.exchange(
                "/api/auth/login",
                HttpMethod.POST,
                request,
                AuthResponseDTO.class);

        AuthResponseDTO result = response.getBody();
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assert result != null;
        assertTrue(jwtGenerator.validateToken(result.getAccessToken()));
        assertEquals("Bearer", result.getTokenType());

        user.setPassword("0000");
        HttpEntity<UserEntity> requestError = new HttpEntity<>(user,headers);
        ResponseEntity<AuthResponseDTO> responseError = testRestTemplate.exchange(
                "/api/auth/login",
                HttpMethod.POST,
                requestError,
                AuthResponseDTO.class);
        
        assertEquals(HttpStatus.UNAUTHORIZED, responseError.getStatusCode());
    }
}
