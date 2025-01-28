package com.assignment.demo.service.reward;

import com.assignment.demo.entity.CustomerTransaction;
import com.assignment.demo.entity.RewardPoint;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
public interface RewardService {
    int calculateRewardPoints(BigDecimal amount);

    void processTransaction(CustomerTransaction transaction);

    BigDecimal getTotalRewardPointsForCustomer(Long customerId);

    List<RewardPoint> getAllRewardPoints();

    void deleteCustomerWithTransactionsAndRewards(Long customerId);
}
