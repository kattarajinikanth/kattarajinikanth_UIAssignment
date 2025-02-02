package com.assignment.demo.entity;

import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
public class RewardPoint {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long rewardPointId;

    private int points;
    private LocalDateTime createdAt;

    @ManyToOne
    @JoinColumn(name = "customer_id")
    private Customer customer;

    @ManyToOne
    @JoinColumn(name = "transaction_id")  // Foreign key for the relationship
    private CustomerTransaction customerTransaction;

    public Long getRewardPointId() {
        return rewardPointId;
    }

    public void setRewardPointId(Long rewardPointId) {
        this.rewardPointId = rewardPointId;
    }

    public int getPoints() {
        return points;
    }

    public void setPoints(int points) {
        this.points = points;
    }

    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    public CustomerTransaction getCustomerTransaction() {
        return customerTransaction;
    }

    public void setCustomerTransaction(CustomerTransaction customerTransaction) {
        this.customerTransaction = customerTransaction;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public static class Builder {
        private int points;
        private LocalDateTime createdAt;
        private Customer customer;
        private CustomerTransaction customerTransaction;

        public Builder setPoints(int points) {
            this.points = points;
            return this;
        }

        public Builder setCreatedAt(LocalDateTime createdAt) {
            this.createdAt = createdAt;
            return this;
        }

        public Builder setCustomer(Customer customer) {
            this.customer = customer;
            return this;
        }

        public Builder setCustomerTransaction(CustomerTransaction customerTransaction) {
            this.customerTransaction = customerTransaction;
            return this;
        }

        public RewardPoint build() {
            RewardPoint rewardPoint = new RewardPoint();
            rewardPoint.points = this.points;
            rewardPoint.createdAt = this.createdAt;
            rewardPoint.customer = this.customer;
            rewardPoint.customerTransaction = this.customerTransaction;
            return rewardPoint;
        }
    }
}
