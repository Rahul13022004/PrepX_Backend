package com.prepXBackend.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class AdminService {

    @Value("${admin.password}")  // The password is stored securely in application.properties
    private String adminPassword;

    public boolean isAdmin(String password) {
        return adminPassword.equals(password);
    }
}
