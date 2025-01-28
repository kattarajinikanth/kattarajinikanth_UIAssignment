package com.assignment.demo.entity;

import jakarta.persistence.*;

@Entity
public class RewardPoint {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long rewardPointId;

    private int points;
    private int month;
    private int year;

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

    public int getMonth() {
        return month;
    }

    public void setMonth(int month) {
        this.month = month;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
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
}
