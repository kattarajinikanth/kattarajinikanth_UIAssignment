package com.assignment.demo.controllerTest;

import com.assignment.demo.controller.CustomerController;
import com.assignment.demo.entity.Customer;
import com.assignment.demo.exception.ResourceNotFoundException;
import com.assignment.demo.service.customer.CustomerService;
import com.assignment.demo.service.login.LoginAndLogoutService;
import com.assignment.demo.service.login.LoginService;
import com.assignment.demo.service.reward.RewardService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

class CustomerControllerTest {

    @Mock
    private CustomerService customerService;

    @Mock
    private LoginAndLogoutService loginService;

    @Mock
    private RewardService rewardService;

    @InjectMocks
    private CustomerController customerController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    // Test case for registerCustomer API
    @Test
    void testRegisterCustomer() {
        Customer customer = new Customer();
        customer.setName("John Doe");
        customer.setEmail("kattarajini@example.com");
        ResponseEntity<String> response = customerController.registerCustomer(customer);
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals("Customer registered successfully", response.getBody());
    }

    // Test case for loginCustomer API (Successful login)
    @Test
    void testLoginCustomer_success() {
        String email = "kattarajini@example.com";
        String password = "password123";
        when(loginService.authenticateCustomer(email, password)).thenReturn(true);
        ResponseEntity<String> response = customerController.loginCustomer(email, password);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Login successful", response.getBody());
    }

    // Test case for loginCustomer API (Unsuccessful login)
    @Test
    void testLoginCustomer_failure() {
        String email = "kattarajini@example.com";
        String password = "incorrectPassword";
        when(loginService.authenticateCustomer(email, password)).thenReturn(false);
        ResponseEntity<String> response = customerController.loginCustomer(email, password);
        assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
        assertEquals("Invalid credentials", response.getBody());
    }

    // Test case for logoutCustomer API
    @Test
    void testLogoutCustomer() {
        ResponseEntity<String> response = customerController.logoutCustomer();
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Logged out successfully", response.getBody());
        verify(loginService, times(1)).logout();
    }

    // Test case for deleteCustomer API (Invalid customer ID)
    @Test
    void testDeleteCustomer_invalidCustomerId() {
        Long customerId = -1L;
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            customerController.deleteCustomer(customerId);
        });
        assertEquals("Customer ID must be positive", exception.getMessage());
    }

    // Test case for deleteCustomer API (Customer not found)
    @Test
    void testDeleteCustomer_customerNotFound() {
        Long customerId = 1L;
        doThrow(new ResourceNotFoundException("Customer not found"))
                .when(customerService).deleteCustomerWithTransactionsAndRewards(customerId);
        ResponseEntity<String> response = customerController.deleteCustomer(customerId);
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertEquals("Error occurred while deleting customer with ID " + customerId, response.getBody());
    }

    // Test case for deleteCustomer API (Successful deletion)
    @Test
    void testDeleteCustomer_success() {
        Long customerId = 1L;
        doNothing().when(customerService).deleteCustomerWithTransactionsAndRewards(customerId);
        ResponseEntity<String> response = customerController.deleteCustomer(customerId);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Customer with ID " + customerId + " and associated transactions and reward points deleted successfully", response.getBody());
    }
}

