package com.example.springsecurityjwtapi.controllers;

import com.example.springsecurityjwtapi.dtos.AuthResponseDTO;
import com.example.springsecurityjwtapi.entities.Car;
import com.example.springsecurityjwtapi.entities.UserEntity;
import com.example.springsecurityjwtapi.security.JwtGenerator;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@RequestMapping("/api")
public class CarControllerTest {

    private TestRestTemplate testRestTemplate;

    @Autowired
    private RestTemplateBuilder restTemplateBuilder;

    @LocalServerPort
    private int port;

    private String token;
    private HttpHeaders headers;

    @Autowired
    private JwtGenerator jwtGenerator;

    @BeforeEach
    void setUp() {
        restTemplateBuilder = restTemplateBuilder.rootUri("http://localhost:" + port);
        testRestTemplate = new TestRestTemplate(restTemplateBuilder);

        UserEntity user = new UserEntity();
        user.setUsername("user");
        user.setPassword("1234");

        this.headers = new HttpHeaders();
        this.headers.setContentType(MediaType.APPLICATION_JSON); // enviamos JSON
        HttpEntity<UserEntity> request = new HttpEntity<>(user, this.headers);
        ResponseEntity<String> response = testRestTemplate.exchange(
                "/api/auth/register",
                HttpMethod.POST,
                request,
                String.class);

        System.out.println(response.getBody());

        this.headers.setAccept(List.of(MediaType.APPLICATION_JSON)); // Recibimos una lista JSON
        HttpEntity<UserEntity> request1 = new HttpEntity<>(user, this.headers);
        ResponseEntity<AuthResponseDTO> response1 = testRestTemplate.exchange(
                "/api/auth/login",
                HttpMethod.POST,
                request1,
                AuthResponseDTO.class);

        this.token = response1.getBody().getAccessToken();

        this.headers.setBearerAuth(this.token);
    }

    @DisplayName("GET /api/cars")
    @Test
    @Order(1)
    void findAll(){
        HttpEntity<String> requestSuccess = new HttpEntity<>("", this.headers);
        System.out.println("Successful Request Headers " + this.headers);
        ResponseEntity<List> responseSuccess = testRestTemplate.exchange(
                "/api/cars",
                HttpMethod.GET,
                requestSuccess,
                List.class);
        assertEquals(HttpStatus.OK, responseSuccess.getStatusCode());
        System.out.println(responseSuccess.getBody());

        HttpHeaders headersFailed = new HttpHeaders();
        headersFailed.setContentType(MediaType.APPLICATION_JSON);
        headersFailed.setAccept(List.of(MediaType.APPLICATION_JSON));
        headersFailed.setBearerAuth("falseToken");
        System.out.println("Failed Request Headers " + headersFailed);

        HttpEntity<String> requestFailed = new HttpEntity<>("", headersFailed);
        ResponseEntity<Void> responseFailed = testRestTemplate.exchange(
                "/api/cars",
                HttpMethod.GET,
                requestFailed,
                Void.class);
        assertEquals(HttpStatus.UNAUTHORIZED, responseFailed.getStatusCode());
    }
}
