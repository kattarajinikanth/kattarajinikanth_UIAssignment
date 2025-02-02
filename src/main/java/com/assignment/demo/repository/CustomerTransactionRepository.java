package com.assignment.demo.repository;

import com.assignment.demo.entity.CustomerTransaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface CustomerTransactionRepository extends JpaRepository<CustomerTransaction, Long> {

    @Query("SELECT t FROM CustomerTransaction t WHERE t.customer.id = :customerId AND t.date BETWEEN :startDate AND :endDate")
    List<CustomerTransaction> findTransactionsForCustomerBetweenDates(@Param("customerId") Long customerId, @Param("startDate") LocalDate startDate, @Param("endDate") LocalDate endDate);
}
