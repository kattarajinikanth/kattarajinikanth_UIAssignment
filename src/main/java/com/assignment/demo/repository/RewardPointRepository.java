package com.assignment.demo.repository;

import com.assignment.demo.entity.RewardPoint;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;

@Repository
public interface RewardPointRepository extends JpaRepository<RewardPoint, Long> {

    @Query("SELECT r FROM RewardPoint r WHERE r.customer.id = :customerId")
    List<RewardPoint> findRewardsByCustomerId(@Param("customerId") Long customerId);
}
