package com.assignment.demo.controller;

import com.assignment.demo.entity.CustomerTransaction;
import com.assignment.demo.service.transaction.TransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class TransactionController {

    @Autowired
    private TransactionService transactionService;

    @PostMapping("/transactions")
    public ResponseEntity<String> addTransaction(@RequestBody CustomerTransaction transaction) {
        transactionService.processTransaction(transaction);
        return ResponseEntity.status(HttpStatus.CREATED).body("Transaction processed");
    }
}
