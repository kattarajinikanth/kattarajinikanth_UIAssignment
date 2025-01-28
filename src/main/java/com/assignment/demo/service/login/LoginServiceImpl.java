package com.assignment.demo.service.login;

import com.assignment.demo.entity.Customer;
import com.assignment.demo.repository.CustomerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class LoginServiceImpl implements LoginService {


    @Autowired
    private CustomerRepository customerRepository;

    private Customer loggedInCustomer = null;

    public boolean authenticateCustomer(String email, String password) {
        Optional<Customer> customer = customerRepository.findByEmail(email);
        if (customer.isPresent() && password.equals("password123")) {
            loggedInCustomer = customer.get();
            return true;
        }
        return false;
    }

    public void logout() {
        loggedInCustomer = null;
    }
}
