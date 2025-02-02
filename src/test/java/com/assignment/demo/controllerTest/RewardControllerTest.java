package com.assignment.demo.controllerTest;

import com.assignment.demo.controller.RewardController;
import com.assignment.demo.entity.RewardPoint;
import com.assignment.demo.exception.ResourceNotFoundException;
import com.assignment.demo.service.reward.RewardService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.http.ResponseEntity;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class RewardControllerTest {

    @Mock
    private RewardService rewardService;

    @InjectMocks
    private RewardController rewardController;

    private Long customerId;
    private LocalDate endDate;

    @Before
    public void setUp() {
        customerId = 1L;
        endDate = LocalDate.now();
    }

    @Test
    public void testGetRewardsForCustomerForThreeMonths() {
        Map<Long, List<Map<String, Integer>>> mockResponse = new HashMap<>();
        List<Map<String, Integer>> monthlyRewards = new ArrayList<>();

        Map<String, Integer> january = new HashMap<>();
        january.put("January", 50);
        monthlyRewards.add(january);

        Map<String, Integer> february = new HashMap<>();
        february.put("February", 80);
        monthlyRewards.add(february);

        Map<String, Integer> march = new HashMap<>();
        march.put("March", 70);
        monthlyRewards.add(march);

        Map<String, Integer> total = new HashMap<>();
        total.put("Total", 200);
        monthlyRewards.add(total);
        mockResponse.put(customerId, monthlyRewards);
        when(rewardService.calculateRewardPointsForCustomer(eq(customerId), eq(endDate))).thenReturn(mockResponse);
        ResponseEntity<Map<Long, List<Map<String, Integer>>>> response = rewardController.getRewardsForCustomerForThreeMonths(customerId, endDate.toString());
        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());
        assertEquals(mockResponse, response.getBody());
        verify(rewardService, times(1)).calculateRewardPointsForCustomer(eq(customerId), eq(endDate));
    }

    @Test
    public void testGetRewardsForCustomer() {
        List<RewardPoint> mockRewardPoints = new ArrayList<>();
        RewardPoint rewardPoint1 = new RewardPoint();
        rewardPoint1.setPoints(50);
        mockRewardPoints.add(rewardPoint1);
        Map<Long, List<RewardPoint>> mockResponse = new HashMap<>();
        mockResponse.put(customerId, mockRewardPoints);
        when(rewardService.getRewardsForCustomer(eq(customerId))).thenReturn(mockResponse);
        ResponseEntity<Map<Long, List<RewardPoint>>> response = rewardController.getRewardsForCustomer(customerId);
        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());
        assertEquals(mockResponse, response.getBody());
        verify(rewardService, times(1)).getRewardsForCustomer(eq(customerId));
    }

    @Test(expected = ResourceNotFoundException.class)
    public void testGetRewardsForCustomerNoRewards() {
        when(rewardService.getRewardsForCustomer(eq(customerId))).thenReturn(new HashMap<>());
        rewardController.getRewardsForCustomer(customerId);
    }

    @Test
    public void testGetAllRewards() {
        List<RewardPoint> mockRewardPoints = new ArrayList<>();
        RewardPoint rewardPoint1 = new RewardPoint();
        rewardPoint1.setPoints(100);
        mockRewardPoints.add(rewardPoint1);
        Map<Long, List<RewardPoint>> mockResponse = new HashMap<>();
        mockResponse.put(customerId, mockRewardPoints);
        when(rewardService.getAllRewardPointsGroupedByCustomer()).thenReturn(mockResponse);
        ResponseEntity<Map<Long, List<RewardPoint>>> response = rewardController.getAllRewards();
        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());
        assertEquals(mockResponse, response.getBody());
        verify(rewardService, times(1)).getAllRewardPointsGroupedByCustomer();
    }
}

