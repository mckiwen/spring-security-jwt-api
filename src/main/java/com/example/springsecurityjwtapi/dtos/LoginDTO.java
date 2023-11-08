package com.example.springsecurityjwtapi.dtos;

import lombok.Data;

@Data
public class LoginDTO {
    private String username;
    private String password;
}
