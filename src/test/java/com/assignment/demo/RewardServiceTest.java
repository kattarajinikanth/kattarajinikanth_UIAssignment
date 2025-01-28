package com.assignment.demo;

import com.assignment.demo.entity.Customer;
import com.assignment.demo.entity.CustomerTransaction;
import com.assignment.demo.entity.RewardPoint;
import com.assignment.demo.exception.ResourceNotFoundException;
import com.assignment.demo.repository.CustomerRepository;
import com.assignment.demo.repository.CustomerTransactionRepository;
import com.assignment.demo.repository.RewardPointRepository;
import com.assignment.demo.service.reward.RewardServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class RewardServiceTest {

    @Mock
    private CustomerTransactionRepository transactionRepository;

    @Mock
    private RewardPointRepository rewardPointRepository;

    @Mock
    private CustomerRepository customerRepository;

    @InjectMocks
    private RewardServiceImpl rewardService;

    // Test case for calculateRewardPoints() when amount is above 100
    @Test
    void testCalculateRewardPoints_above100() {
        BigDecimal amount = new BigDecimal("150.00");
        int expectedPoints = 100; // (150 - 100) * 2 points = 100
        int result = rewardService.calculateRewardPoints(amount);
        assertEquals(expectedPoints, result);
    }

    // Test case for calculateRewardPoints() when amount is between 50 and 100
    @Test
    void testCalculateRewardPoints_between50And100() {
        BigDecimal amount = new BigDecimal("75.00");
        int expectedPoints = 25; // (75 - 50) * 1 point = 25
        int result = rewardService.calculateRewardPoints(amount);
        assertEquals(expectedPoints, result);
    }

    // Test case for calculateRewardPoints() when amount is below 50
    @Test
    void testCalculateRewardPoints_below50() {
        BigDecimal amount = new BigDecimal("40.00");
        int expectedPoints = 0; // No points for amount below $50
        int result = rewardService.calculateRewardPoints(amount);
        assertEquals(expectedPoints, result);
    }

    // Test case for processTransaction() - successfully processes transaction
    @Test
    void testProcessTransaction() {
        Customer customer = new Customer();
        customer.setCustomerId(1L);

        CustomerTransaction transaction = new CustomerTransaction();
        transaction.setCustomer(customer);
        transaction.setAmount(new BigDecimal("150.00"));

        RewardPoint rewardPoint = new RewardPoint();
        rewardPoint.setCustomer(customer);
        rewardPoint.setPoints(100); // (150 - 100) * 2 = 100 points
        doNothing().when(transactionRepository).save(any(CustomerTransaction.class));
        doNothing().when(rewardPointRepository).save(any(RewardPoint.class));
        rewardService.processTransaction(transaction);
        verify(transactionRepository, times(1)).save(transaction);
        verify(rewardPointRepository, times(1)).save(any(RewardPoint.class));
    }

    // Test case for getTotalRewardPointsForCustomer() - retrieves total points for customer
    @Test
    void testGetTotalRewardPointsForCustomer() {
        Long customerId = 1L;
        BigDecimal totalPoints = new BigDecimal("250.00");
        when(rewardPointRepository.findTotalRewardPointsByCustomerId(customerId)).thenReturn(totalPoints);
        BigDecimal result = rewardService.getTotalRewardPointsForCustomer(customerId);
        assertEquals(totalPoints, result);
    }

    // Test case for getTotalRewardPointsForCustomer() when no points are found
    @Test
    void testGetTotalRewardPointsForCustomer_noPoints() {
        Long customerId = 1L;
        when(rewardPointRepository.findTotalRewardPointsByCustomerId(customerId)).thenReturn(null);
        BigDecimal result = rewardService.getTotalRewardPointsForCustomer(customerId);
        assertEquals(BigDecimal.ZERO, result); // If no points, return zero
    }

    // Test case for getAllRewardPoints() - retrieves all reward points
    @Test
    void testGetAllRewardPoints() {
        RewardPoint reward1 = new RewardPoint();
        RewardPoint reward2 = new RewardPoint();
        List<RewardPoint> rewardPoints = List.of(reward1, reward2);
        when(rewardPointRepository.findAll()).thenReturn(rewardPoints);
        List<RewardPoint> result = rewardService.getAllRewardPoints();
        assertEquals(2, result.size());
        verify(rewardPointRepository, times(1)).findAll();
    }

    // Test case for deleteCustomerWithTransactionsAndRewards() - successfully deletes customer
    @Test
    void testDeleteCustomerWithTransactionsAndRewards_success() {
        Long customerId = 1L;
        Customer customer = new Customer();
        customer.setCustomerId(customerId);
        when(customerRepository.findById(customerId)).thenReturn(Optional.of(customer));
        doNothing().when(customerRepository).delete(any(Customer.class));
        rewardService.deleteCustomerWithTransactionsAndRewards(customerId);
        verify(customerRepository, times(1)).delete(customer);
    }

    // Test case for deleteCustomerWithTransactionsAndRewards() when customer not found
    @Test
    void testDeleteCustomerWithTransactionsAndRewards_customerNotFound() {
        Long customerId = 1L;
        when(customerRepository.findById(customerId)).thenReturn(Optional.empty());
        assertThrows(ResourceNotFoundException.class, () ->
                rewardService.deleteCustomerWithTransactionsAndRewards(customerId));
    }
}


