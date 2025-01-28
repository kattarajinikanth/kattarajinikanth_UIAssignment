package com.assignment.demo.service.customer;

import com.assignment.demo.entity.Customer;
import org.springframework.stereotype.Service;

@Service
public interface CustomerService {

    void registerCustomer(Customer customer);
}
