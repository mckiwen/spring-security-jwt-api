package com.example.springsecurityjwtapi.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.io.Serializable;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

@Component
public class JwtAuthEntryPoint implements AuthenticationEntryPoint, Serializable {
    @Override
    public void commence(HttpServletRequest request,
                         HttpServletResponse response,
                         AuthenticationException authException) throws IOException, ServletException  {
        response.setContentType("application/json");
        //response.sendError(HttpServletResponse.SC_UNAUTHORIZED, authException.getMessage());
        response.setStatus(HttpStatus.UNAUTHORIZED.value());

        Map<String, Object> data = new LinkedHashMap<>();
        data.put("status", HttpStatus.UNAUTHORIZED.value() + " UNAUTHORIZED");
        data.put("message", "Access Denied, JWT request was expired or incorrect");
        data.put("path", request.getRequestURL().toString());
        data.put("timestamp", new Date().toString());

        OutputStream out = response.getOutputStream();
        ObjectMapper mapper = new ObjectMapper();
        mapper.writeValue(out, data);
        out.flush();
    }
}
