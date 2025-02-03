# Overview

This Spring Boot application calculates the reward points for customers based on their transaction history. Customers
earn points for their purchases as follows:

2 points for every dollar spent over $100 in each transaction.
1 point for every dollar spent between $50 and $100 in each transaction.
The system processes transaction data over a three-month period, calculating monthly reward points as well as the total
points accumulated by each customer.

## Features

Calculates reward points for each transaction based on purchase amount.
Accumulates points on a monthly and total basis.
Provides easy-to-understand point calculation logic for each transaction.
Handles a large number of transactions efficiently.
Points Calculation Logic
The reward points are calculated using the following rules:

## Transactions over $100:

For every dollar spent over $100, the customer receives 2 points.

## Transactions between $50 and $100:

For every dollar spent in the range of $50 to $100, the customer receives 1 point.

## Example Calculation:

For a transaction of $120:
Points for the first $100: 1 point per dollar for the first $50 = 50 points.
Points for the next $20: 2 points per dollar = 40 points.
Total points = 90 points.

### Set up the environment: Make sure you have the following installed:

Java 23
Maven (for dependency management and build)
PostgreSQL

### Configure application properties:

Edit src/main/resources/application.properties to include your database settings:
spring.datasource.url=jdbc:postgresql://localhost:5432/assignment
spring.datasource.username=root
spring.datasource.password=password

### Build and run the application:

Use Maven to build and run the application:
mvn clean install
mvn spring-boot:run

### Access the application:

Once the application is running, you can interact with the API or test it using Postman or any API client
at http://localhost:8080.