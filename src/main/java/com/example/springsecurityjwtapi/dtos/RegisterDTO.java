package com.example.springsecurityjwtapi.dtos;

import lombok.Data;

@Data
public class RegisterDTO {
    private String username;
    private String password;
}
