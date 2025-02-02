package com.assignment.demo.service.reward;

import com.assignment.demo.entity.RewardPoint;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@Service
public interface RewardService {

    Map<Long, List<Map<String, Integer>>> calculateRewardPointsForCustomer(Long customerId, LocalDate endDate);

    Map<Long, List<RewardPoint>> getRewardsForCustomer(Long customerId);

    Map<Long, List<RewardPoint>> getAllRewardPointsGroupedByCustomer();
}
