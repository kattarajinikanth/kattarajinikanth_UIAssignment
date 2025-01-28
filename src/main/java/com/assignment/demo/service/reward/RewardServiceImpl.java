package com.assignment.demo.service.reward;

import com.assignment.demo.entity.Customer;
import com.assignment.demo.entity.CustomerTransaction;
import com.assignment.demo.entity.RewardPoint;
import com.assignment.demo.exception.ResourceNotFoundException;
import com.assignment.demo.repository.CustomerRepository;
import com.assignment.demo.repository.CustomerTransactionRepository;
import com.assignment.demo.repository.RewardPointRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
public class RewardServiceImpl implements RewardService {

    @Autowired
    private CustomerTransactionRepository transactionRepository;

    @Autowired
    private RewardPointRepository rewardPointRepository;

    @Autowired
    private CustomerRepository customerRepository;

    @Override
    public int calculateRewardPoints(BigDecimal amount) {
        int points = 0;

        // Points for amount greater than $100
        if (amount.compareTo(new BigDecimal(100)) > 0) {
            points += amount.subtract(new BigDecimal(100)).intValue() * 2; // 2 points per dollar above $100
        }

        // Points for amount between $50 and $100
        if (amount.compareTo(new BigDecimal(50)) > 0) {
            points += Math.min(amount.intValue(), 100) - 50; // 1 point per dollar between $50 and $100
        }

        return points;
    }

    @Override
    @Transactional
    public void processTransaction(CustomerTransaction transaction) {
        int points = calculateRewardPoints(transaction.getAmount());

        RewardPoint rewardPoint = new RewardPoint();
        rewardPoint.setCustomer(transaction.getCustomer());
        rewardPoint.setPoints(points);
        rewardPoint.setMonth(LocalDate.now().getMonthValue());
        rewardPoint.setYear(LocalDate.now().getYear());
        rewardPoint.setCustomerTransaction(transaction);
        rewardPointRepository.save(rewardPoint);
        transactionRepository.save(transaction);
    }


    @Override
    public BigDecimal getTotalRewardPointsForCustomer(Long customerId) {
        BigDecimal totalPoints = rewardPointRepository.findTotalRewardPointsByCustomerId(customerId);
        if (totalPoints == null) {
            return BigDecimal.ZERO;  // If no reward points, return 0
        }
        return totalPoints;
    }

    @Override
    public List<RewardPoint> getAllRewardPoints() {
        return rewardPointRepository.findAll();
    }

    @Override
    public void deleteCustomerWithTransactionsAndRewards(Long customerId) {
        Optional<Customer> customer = customerRepository.findById(customerId);
        if (!customer.isPresent()) {
            throw new ResourceNotFoundException("Customer not found with ID " + customerId);
        }
        customerRepository.delete(customer.get());
    }

}
