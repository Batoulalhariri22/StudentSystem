package com.example.SchoolSpring.service;

import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Service;

@Service
@Setter
@Getter
public class AuthenticationService {
    private boolean isAuthenticated = false;
    private int authenticatedId = -1;
    private String authenticatedName = "";
}
