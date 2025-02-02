package com.assignment.demo.service.customer;

import com.assignment.demo.entity.Customer;
import com.assignment.demo.exception.ResourceNotFoundException;
import com.assignment.demo.repository.CustomerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class CustomerServiceImpl implements CustomerService {

    @Autowired
    private CustomerRepository customerRepository;

    public void registerCustomer(Customer customer) {
        customerRepository.save(customer);
    }

    @Override
    public void deleteCustomerWithTransactionsAndRewards(Long customerId) {
        Optional<Customer> customer = customerRepository.findById(customerId);
        if (!customer.isPresent()) {
            throw new ResourceNotFoundException("Customer not found with ID " + customerId);
        }
        customerRepository.delete(customer.get());
    }
}
