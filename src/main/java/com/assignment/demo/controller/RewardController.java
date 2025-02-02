package com.assignment.demo.controller;

import com.assignment.demo.entity.RewardPoint;
import com.assignment.demo.exception.ResourceNotFoundException;
import com.assignment.demo.service.reward.RewardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api")
public class RewardController {

    @Autowired
    private RewardService rewardService;

    @GetMapping("/customer-rewards/{customerId}/{endDate}")
    public ResponseEntity<Map<Long, List<Map<String, Integer>>>> getRewardsForCustomerForThreeMonths(@PathVariable Long customerId, @PathVariable String endDate) {
        LocalDate start = LocalDate.parse(endDate);
        Map<Long, List<Map<String, Integer>>> rewardPoints = rewardService.calculateRewardPointsForCustomer(customerId, start);
        return ResponseEntity.ok(rewardPoints);

        //sample output
//        {
//            "1": [
//            { "January": 50 },
//            { "February": 80 },
//            { "March": 70 },
//            { "Total": 200 }
//            ]
//        }

    }


    @GetMapping("rewards/{customerId}")
    public ResponseEntity<Map<Long, List<RewardPoint>>> getRewardsForCustomer(@PathVariable Long customerId) {
        Map<Long, List<RewardPoint>> customerRewards = rewardService.getRewardsForCustomer(customerId);
        if (customerRewards.isEmpty()) {
            throw new ResourceNotFoundException("No rewards found for the provided customer IDs");
        }
        return ResponseEntity.ok(customerRewards);
    }


    @GetMapping("/total-rewards")
    public ResponseEntity<Map<Long, List<RewardPoint>>> getAllRewards() {
        Map<Long, List<RewardPoint>> allRewards = rewardService.getAllRewardPointsGroupedByCustomer();
        return ResponseEntity.ok(allRewards);
    }
}
