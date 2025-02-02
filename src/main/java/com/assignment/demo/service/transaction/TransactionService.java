package com.assignment.demo.service.transaction;

import com.assignment.demo.entity.CustomerTransaction;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
public interface TransactionService {

    int calculateRewardPoints(BigDecimal amount);

    void processTransaction(CustomerTransaction transaction);
}
