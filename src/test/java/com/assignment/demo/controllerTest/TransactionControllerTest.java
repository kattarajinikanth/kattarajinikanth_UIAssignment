package com.assignment.demo.controllerTest;

import com.assignment.demo.controller.TransactionController;
import com.assignment.demo.entity.CustomerTransaction;
import com.assignment.demo.service.transaction.TransactionService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

public class TransactionControllerTest {
    @InjectMocks
    private TransactionController transactionController;

    @Mock
    private TransactionService transactionService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    // Test case for addTransaction API
    @Test
    void testAddTransaction() {
        CustomerTransaction transaction = new CustomerTransaction();
        transaction.setAmount(new BigDecimal("150"));
        transaction.setSpentDetails("Purchase at store");
        ResponseEntity<String> response = transactionController.addTransaction(transaction);
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals("Transaction processed", response.getBody());
        verify(transactionService, times(1)).processTransaction(transaction);
    }
}
