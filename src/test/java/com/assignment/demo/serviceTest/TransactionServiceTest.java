package com.assignment.demo.serviceTest;

import com.assignment.demo.entity.CustomerTransaction;
import com.assignment.demo.entity.RewardPoint;
import com.assignment.demo.repository.CustomerTransactionRepository;
import com.assignment.demo.repository.RewardPointRepository;
import com.assignment.demo.service.transaction.TransactionServiceImpl;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.math.BigDecimal;
import java.time.LocalDate;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class TransactionServiceTest {

    @Mock
    private CustomerTransactionRepository transactionRepository;

    @Mock
    private RewardPointRepository rewardPointRepository;

    @InjectMocks
    private TransactionServiceImpl transactionService;

    private CustomerTransaction transaction;

    @Before
    public void setUp() {
        transaction = new CustomerTransaction();
        transaction.setAmount(new BigDecimal("150"));
        transaction.setDate(LocalDate.now().atStartOfDay());
        transaction.setSpentDetails("Test transaction");
    }

    @Test
    public void testCalculateRewardPoints_Above100() {
        // Test case for amount greater than $100 (e.g., $150)
        int points = transactionService.calculateRewardPoints(new BigDecimal("150"));

        // Points = (150 - 100) * 2 = 100
        assertEquals(100, points);
    }

    @Test
    public void testCalculateRewardPoints_Between50And100() {
        // Test case for amount between $50 and $100 (e.g., $75)
        int points = transactionService.calculateRewardPoints(new BigDecimal("75"));

        // Points = (75 - 50) = 25
        assertEquals(25, points);
    }

    @Test
    public void testCalculateRewardPoints_LessThan50() {
        // Test case for amount less than $50 (e.g., $30)
        int points = transactionService.calculateRewardPoints(new BigDecimal("30"));

        // Points = 0 (No points for amounts less than $50)
        assertEquals(0, points);
    }

    @Test
    public void testProcessTransaction() {
        RewardPoint rewardPoint = new RewardPoint.Builder()
                .setCustomer(transaction.getCustomer())
                .setPoints(100) // Assume points calculated for $150 is 100
                .setCreatedAt(transaction.getDate())
                .setCustomerTransaction(transaction)
                .build();

        transactionService.processTransaction(transaction);
        verify(rewardPointRepository, times(1)).save(any(RewardPoint.class));
        verify(transactionRepository, times(1)).save(transaction);
    }

    @Test
    public void testCalculateRewardPoints_AmountIsZero() {
        // Test case for zero amount (e.g., $0)
        int points = transactionService.calculateRewardPoints(new BigDecimal("0"));

        // Points = 0 for a zero amount
        assertEquals(0, points);
    }

    @Test
    public void testCalculateRewardPoints_Exact100() {
        // Test case for amount exactly $100
        int points = transactionService.calculateRewardPoints(new BigDecimal("100"));

        // Points = 0 since there is no amount above $100
        assertEquals(0, points);
    }
}

