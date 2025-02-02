package com.assignment.demo.serviceTest;

import com.assignment.demo.entity.Customer;
import com.assignment.demo.exception.ResourceNotFoundException;
import com.assignment.demo.repository.CustomerRepository;
import com.assignment.demo.service.customer.CustomerServiceImpl;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.Optional;

import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class CustomerServiceTest {

    @Mock
    private CustomerRepository customerRepository;

    @InjectMocks
    private CustomerServiceImpl customerService;

    private Customer customer;
    private Long customerId;

    @Before
    public void setUp() {
        customerId = 1L;
        customer = new Customer();
        customer.setCustomerId(customerId);
        customer.setName("John Doe");
        customer.setEmail("john.doe@example.com");
    }

    @Test
    public void testRegisterCustomer() {
        customerService.registerCustomer(customer);
        verify(customerRepository, times(1)).save(customer);
    }

    @Test
    public void testDeleteCustomerWithTransactionsAndRewards_CustomerExists() {
        when(customerRepository.findById(customerId)).thenReturn(Optional.of(customer));
        customerService.deleteCustomerWithTransactionsAndRewards(customerId);
        verify(customerRepository, times(1)).delete(customer);
    }

    @Test(expected = ResourceNotFoundException.class)
    public void testDeleteCustomerWithTransactionsAndRewards_CustomerNotFound() {
        when(customerRepository.findById(customerId)).thenReturn(Optional.empty());
        customerService.deleteCustomerWithTransactionsAndRewards(customerId);
    }
}

