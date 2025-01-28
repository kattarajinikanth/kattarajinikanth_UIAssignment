package com.assignment.demo.service.customer;

import com.assignment.demo.entity.Customer;
import com.assignment.demo.repository.CustomerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CustomerServiceImpl implements CustomerService {

    @Autowired
    private CustomerRepository customerRepository;

    public void registerCustomer(Customer customer) {
        customerRepository.save(customer);
    }
}
