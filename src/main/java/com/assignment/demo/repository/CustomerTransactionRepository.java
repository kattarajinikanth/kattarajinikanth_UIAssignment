package com.assignment.demo.repository;

import com.assignment.demo.entity.CustomerTransaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CustomerTransactionRepository extends JpaRepository<CustomerTransaction, Long> {
}
