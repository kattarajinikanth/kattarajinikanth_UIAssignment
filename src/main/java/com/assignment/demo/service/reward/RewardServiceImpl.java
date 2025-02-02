package com.assignment.demo.service.reward;

import com.assignment.demo.entity.CustomerTransaction;
import com.assignment.demo.entity.RewardPoint;
import com.assignment.demo.repository.CustomerRepository;
import com.assignment.demo.repository.CustomerTransactionRepository;
import com.assignment.demo.repository.RewardPointRepository;
import com.assignment.demo.service.transaction.TransactionService;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class RewardServiceImpl implements RewardService {

    @Autowired
    private CustomerTransactionRepository transactionRepository;

    @Autowired
    private RewardPointRepository rewardPointRepository;

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private TransactionService transactionService;

    @Override
    @Transactional
    public Map<Long, List<Map<String, Integer>>> calculateRewardPointsForCustomer(Long customerId, LocalDate endDate) {
        String[] monthNames = {"", "January", "February", "March", "April", "May", "June", "July", "August", "September", "October", "November", "December"};
        LocalDate startDate = endDate.minusMonths(3);
        List<CustomerTransaction> transactions = transactionRepository.findTransactionsForCustomerBetweenDates(customerId, startDate, endDate);
        Map<Long, List<Map<String, Integer>>> customerRewardPoints = new HashMap<>();
        Map<Integer, Integer> monthlyPointsMap = new HashMap<>();

        // Iterate through each transaction to calculate points
        for (CustomerTransaction transaction : transactions) {
            int points = transactionService.calculateRewardPoints(transaction.getAmount());
            int month = transaction.getDate().getMonthValue();

            monthlyPointsMap.put(month, monthlyPointsMap.getOrDefault(month, 0) + points);
        }

        int totalPoints = monthlyPointsMap.values().stream().mapToInt(Integer::intValue).sum();
        List<Map<String, Integer>> monthlyRewardPointsList = new ArrayList<>();

        int startMonth = startDate.getMonthValue();
        int endMonth = endDate.getMonthValue();

        // If the period spans across a year (i.e., endMonth < startMonth), handle the month rollover
        for (int month = startMonth; month <= endMonth; month++) {
            int currentMonth = month % 12;  // Handle month rollover
            if (currentMonth == 0) {
                currentMonth = 12;  // To handle December correctly
            }

            Map<String, Integer> monthlyMap = new HashMap<>();
            String monthName = monthNames[currentMonth];  // Get the month name (e.g., "January")
            monthlyMap.put(monthName, monthlyPointsMap.getOrDefault(currentMonth, 0));
            monthlyRewardPointsList.add(monthlyMap);
        }

        // Add the total rewards map to the list
        Map<String, Integer> totalRewardsMap = new HashMap<>();
        totalRewardsMap.put("Total", totalPoints);
        monthlyRewardPointsList.add(totalRewardsMap);

        // Add the data to the final result map for the given customerId
        customerRewardPoints.put(customerId, monthlyRewardPointsList);

        return customerRewardPoints;
    }


    @Override
    public Map<Long, List<RewardPoint>> getRewardsForCustomer(Long customerId) {
        Map<Long, List<RewardPoint>> customerRewardsMap = new HashMap<>();
        List<RewardPoint> rewards = rewardPointRepository.findRewardsByCustomerId(customerId);
        if (rewards != null && !rewards.isEmpty()) {
            customerRewardsMap.put(customerId, rewards);
        } else {
            customerRewardsMap.put(customerId, Collections.emptyList());
        }
        return customerRewardsMap;
    }


    @Override
    public Map<Long, List<RewardPoint>> getAllRewardPointsGroupedByCustomer() {
        List<RewardPoint> allRewards = rewardPointRepository.findAll();
        return allRewards.stream()
                .collect(Collectors.groupingBy(rewardPoint -> rewardPoint.getCustomer().getCustomerId()));
    }

}
