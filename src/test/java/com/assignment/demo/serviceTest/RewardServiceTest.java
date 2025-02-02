package com.assignment.demo.serviceTest;

import com.assignment.demo.entity.Customer;
import com.assignment.demo.entity.CustomerTransaction;
import com.assignment.demo.entity.RewardPoint;
import com.assignment.demo.repository.CustomerRepository;
import com.assignment.demo.repository.CustomerTransactionRepository;
import com.assignment.demo.repository.RewardPointRepository;
import com.assignment.demo.service.reward.RewardServiceImpl;
import com.assignment.demo.service.transaction.TransactionService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.*;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class RewardServiceTest {

    @Mock
    private CustomerTransactionRepository transactionRepository;

    @Mock
    private RewardPointRepository rewardPointRepository;

    @Mock
    private CustomerRepository customerRepository;

    @Mock
    private TransactionService transactionService;

    @InjectMocks
    private RewardServiceImpl rewardService;

    private Long customerId;
    private LocalDate endDate;
    private CustomerTransaction transaction1, transaction2;

    @Before
    public void setUp() {
        customerId = 1L;
        endDate = LocalDate.now();

        transaction1 = new CustomerTransaction();
        transaction1.setAmount(new BigDecimal("150"));
        transaction1.setDate(endDate.minusMonths(1).atStartOfDay());
        transaction1.setCustomer(new Customer());

        transaction2 = new CustomerTransaction();
        transaction2.setAmount(new BigDecimal("75"));
        transaction2.setDate(endDate.minusMonths(2).atStartOfDay());
        transaction2.setCustomer(new Customer());
    }

    @Test
    public void testCalculateRewardPointsForCustomer() {
        when(transactionService.calculateRewardPoints(new BigDecimal("150"))).thenReturn(100);
        when(transactionService.calculateRewardPoints(new BigDecimal("75"))).thenReturn(25);
        when(transactionRepository.findTransactionsForCustomerBetweenDates(customerId, endDate.minusMonths(3), endDate))
                .thenReturn(Arrays.asList(transaction1, transaction2));
        Map<Long, List<Map<String, Integer>>> result = rewardService.calculateRewardPointsForCustomer(customerId, endDate);
        assertNotNull(result);
        assertTrue(result.containsKey(customerId));
        List<Map<String, Integer>> rewardPointsList = result.get(customerId);
        assertEquals(3, rewardPointsList.size());
        Map<String, Integer> januaryMap = rewardPointsList.get(0);
        Map<String, Integer> februaryMap = rewardPointsList.get(1);
        Map<String, Integer> marchMap = rewardPointsList.get(2);
        assertEquals(Integer.valueOf(100), januaryMap.get("January"));
        assertEquals(Integer.valueOf(25), februaryMap.get("February"));
        assertEquals(Integer.valueOf(0), marchMap.get("March"));
        Map<String, Integer> totalMap = rewardPointsList.get(3);
        assertEquals(Integer.valueOf(125), totalMap.get("Total"));
    }

    @Test
    public void testGetRewardsForCustomer() {
        RewardPoint rewardPoint1 = new RewardPoint();
        rewardPoint1.setPoints(100);
        rewardPoint1.setCustomer(new Customer());
        RewardPoint rewardPoint2 = new RewardPoint();
        rewardPoint2.setPoints(25);
        rewardPoint2.setCustomer(new Customer());
        List<RewardPoint> rewardPoints = Arrays.asList(rewardPoint1, rewardPoint2);
        when(rewardPointRepository.findRewardsByCustomerId(customerId)).thenReturn(rewardPoints);
        Map<Long, List<RewardPoint>> result = rewardService.getRewardsForCustomer(customerId);
        assertNotNull(result);
        assertTrue(result.containsKey(customerId));
        List<RewardPoint> rewards = result.get(customerId);
        assertEquals(2, rewards.size());
    }

    @Test
    public void testGetRewardsForCustomer_Empty() {
        when(rewardPointRepository.findRewardsByCustomerId(customerId)).thenReturn(Collections.emptyList());
        Map<Long, List<RewardPoint>> result = rewardService.getRewardsForCustomer(customerId);
        assertNotNull(result);
        assertTrue(result.containsKey(customerId));
        assertTrue(result.get(customerId).isEmpty());
    }

    @Test
    public void testGetAllRewardPointsGroupedByCustomer() {
        RewardPoint rewardPoint1 = new RewardPoint();
        rewardPoint1.setPoints(100);
        rewardPoint1.setCustomer(new Customer());
        RewardPoint rewardPoint2 = new RewardPoint();
        rewardPoint2.setPoints(25);
        rewardPoint2.setCustomer(new Customer());
        List<RewardPoint> allRewards = Arrays.asList(rewardPoint1, rewardPoint2);
        when(rewardPointRepository.findAll()).thenReturn(allRewards);
        Map<Long, List<RewardPoint>> result = rewardService.getAllRewardPointsGroupedByCustomer();
        assertNotNull(result);
        assertEquals(1, result.size());
        assertTrue(result.containsKey(1L));
        assertEquals(2, result.get(1L).size());
    }
}
