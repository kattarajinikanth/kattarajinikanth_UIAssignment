package com.assignment.demo.service.login;

import org.springframework.stereotype.Service;

@Service
public interface LoginService {

    boolean authenticateCustomer(String email, String password);

    void logout();
}
