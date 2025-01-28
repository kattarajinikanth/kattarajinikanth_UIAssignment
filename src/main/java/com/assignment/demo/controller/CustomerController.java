package com.assignment.demo.controller;

import com.assignment.demo.entity.Customer;
import com.assignment.demo.entity.CustomerTransaction;
import com.assignment.demo.entity.RewardPoint;
import com.assignment.demo.exception.ResourceNotFoundException;
import com.assignment.demo.service.customer.CustomerService;
import com.assignment.demo.service.login.LoginService;
import com.assignment.demo.service.reward.RewardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping("/api/customers")
public class CustomerController {

    @Autowired
    private CustomerService customerService;

    @Autowired
    private LoginService loginService;

    @Autowired
    private RewardService rewardService;

    // 1. Registration
    @PostMapping("/register")
    public ResponseEntity<String> registerCustomer(@RequestBody @Validated Customer customer) {
        customerService.registerCustomer(customer);
        return ResponseEntity.status(HttpStatus.CREATED).body("Customer registered successfully");
    }

    // 2. Login
    @PostMapping("/login")
    public ResponseEntity<String> loginCustomer(@RequestParam String email, @RequestParam String password) {
        boolean isAuthenticated = loginService.authenticateCustomer(email, password);
        if (isAuthenticated) {
            return ResponseEntity.ok("Login successful");
        }
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid credentials");
    }

    // 3. Logout
    @PostMapping("/logout")
    public ResponseEntity<String> logoutCustomer() {
        loginService.logout();
        return ResponseEntity.ok("Logged out successfully");
    }

    @PostMapping("/transactions")
    public ResponseEntity<String> addTransaction(@RequestBody CustomerTransaction transaction) {
        rewardService.processTransaction(transaction);
        return ResponseEntity.status(HttpStatus.CREATED).body("Transaction processed");
    }

    @GetMapping("/{customerId}/total-rewards")
    public ResponseEntity<BigDecimal> getTotalRewardPoints(@PathVariable Long customerId) {
        if (customerId <= 0) {
            throw new IllegalArgumentException("Customer ID must be positive");
        }
        BigDecimal totalPoints = rewardService.getTotalRewardPointsForCustomer(customerId);
        if (totalPoints.compareTo(BigDecimal.ZERO) == 0) {
            throw new ResourceNotFoundException("No rewards found for customer with ID " + customerId);
        }
        return ResponseEntity.ok(totalPoints);
    }


    @GetMapping("/rewards")
    public ResponseEntity<List<RewardPoint>> getAllRewards() {
        List<RewardPoint> allRewards = rewardService.getAllRewardPoints();
        return ResponseEntity.ok(allRewards);
    }

    @DeleteMapping("/{customerId}")
    public ResponseEntity<String> deleteCustomer(@PathVariable Long customerId) {
        if (customerId <= 0) {
            throw new IllegalArgumentException("Customer ID must be positive");
        }

        try {
            rewardService.deleteCustomerWithTransactionsAndRewards(customerId);
            return ResponseEntity.ok("Customer with ID " + customerId + " and associated transactions and reward points deleted successfully");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error occurred while deleting customer with ID " + customerId);
        }
    }

}
