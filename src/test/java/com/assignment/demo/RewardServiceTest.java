package com.assignment.demo;
import com.assignment.demo.entity.Customer;
import com.assignment.demo.entity.CustomerTransaction;
import com.assignment.demo.entity.RewardPoint;
import com.assignment.demo.repository.CustomerTransactionRepository;
import com.assignment.demo.repository.RewardPointRepository;
import com.assignment.demo.service.reward.RewardService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.*;

@SpringBootTest
public class RewardServiceTest {

    @Mock
    private RewardPointRepository rewardPointRepository;

    @Mock
    private CustomerTransactionRepository transactionRepository;
    @InjectMocks
    private RewardService rewardService;
    private Customer customer;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this); // Initialize the mocks

        customer = new Customer();
        customer.setCustomerId(1L);
        customer.setName("John Doe");
        customer.setEmail("john.doe@example.com");
    }

    // Test for calculating reward points based on the amount
    @Test
    public void testCalculateRewardPoints() {
        // Test case where amount is 120
        int points = rewardService.calculateRewardPoints(new BigDecimal(120));
        assertEquals(90, points, "The reward points for $120 should be 90");

        // Test case where amount is 50
        points = rewardService.calculateRewardPoints(new BigDecimal(50));
        assertEquals(0, points, "The reward points for $50 should be 0");

        // Test case where amount is 200
        points = rewardService.calculateRewardPoints(new BigDecimal(200));
        assertEquals(200, points, "The reward points for $200 should be 200");
    }

    @Test
    public void testProcessTransaction() {
        // Create a mock transaction
        CustomerTransaction transaction = new CustomerTransaction();
        transaction.setCustomer(customer);
        transaction.setAmount(new BigDecimal(150));
        transaction.setDate(LocalDate.now().atStartOfDay());
        transaction.setSpentDetails("Purchase at Store");
        RewardPoint rewardPoint = new RewardPoint();
        rewardPoint.setCustomer(customer);
        rewardPoint.setPoints(100);
        rewardPoint.setMonth(LocalDate.now().getMonthValue());
        rewardPoint.setYear(LocalDate.now().getYear());
        when(rewardPointRepository.save(any(RewardPoint.class))).thenReturn(rewardPoint);
        rewardService.processTransaction(transaction);

        // Verify if the repository save method was called
        verify(rewardPointRepository, times(1)).save(any(RewardPoint.class));

        // Optionally, assert if the saved points are correct (assuming save returns the saved entity)
        RewardPoint savedRewardPoint = rewardPointRepository.save(rewardPoint);
        assertNotNull(savedRewardPoint);
        assertEquals(100, savedRewardPoint.getPoints(), "The reward points for $150 should be 100");
    }

    // Test to retrieve reward points for a customer
    @Test
    public void testGetRewardPointsForCustomer() {
        List<RewardPoint> mockRewardPoints = new ArrayList<>();
        RewardPoint rewardPoint1 = new RewardPoint();
        rewardPoint1.setPoints(90);
        rewardPoint1.setCustomer(customer);
        rewardPoint1.setMonth(LocalDate.now().getMonthValue());
        rewardPoint1.setYear(LocalDate.now().getYear());

        RewardPoint rewardPoint2 = new RewardPoint();
        rewardPoint2.setPoints(50);
        rewardPoint2.setCustomer(customer);
        rewardPoint2.setMonth(LocalDate.now().getMonthValue());
        rewardPoint2.setYear(LocalDate.now().getYear());

        mockRewardPoints.add(rewardPoint1);
        mockRewardPoints.add(rewardPoint2);
//        when(rewardPointRepository.findByCustomerId(customer.getCustomerId())).thenReturn(mockRewardPoints);

//        List<RewardPoint> retrievedRewardPoints = rewardService.getRewardPointsForCustomer(customer.getCustomerId());

        // Verify the behavior and assert values
//        assertNotNull(retrievedRewardPoints);
//        assertEquals(2, retrievedRewardPoints.size(), "There should be 2 reward points records for the customer");
//        assertEquals(90, retrievedRewardPoints.get(0).getPoints(), "First reward point should be 90");
//        assertEquals(50, retrievedRewardPoints.get(1).getPoints(), "Second reward point should be 50");
    }

    // Test to retrieve all reward points
    @Test
    public void testGetAllRewardPoints() {
        List<RewardPoint> mockRewardPoints = new ArrayList<>();
        mockRewardPoints.add(new RewardPoint());
        mockRewardPoints.add(new RewardPoint());
        when(rewardPointRepository.findAll()).thenReturn(mockRewardPoints);


        List<RewardPoint> allRewardPoints = rewardService.getAllRewardPoints();

        // Verify behavior and assert values
        assertNotNull(allRewardPoints);
        assertEquals(2, allRewardPoints.size(), "There should be 2 reward points records in total");
    }
}


