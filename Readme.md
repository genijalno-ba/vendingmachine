### Vending Machine API 

MVP Match Java Assignment

### Prerequisite

1. You need to have Maven installed
2. You need to have Postgres installed
3. You need to create a database called `vendingmachine` and a user with `create table privileges`
4. You need to set DB access properties in the `src/main/resources/liquibase.properties` file

Once you test or run the app, Liquibase will be invoked, and it will create/update tables and columns.

### Test and Run

Test with `mvn clean test` and run with `mvn exec:java`

### Description

This repository was created as a result of a test assignment
and is meant to showcase how different components can work together to create clean, secure and test driven API implementation. 
The setup is based on JAX-RS Specification with the Jersey implementation.
It features Repository pattern accompanied by DAO, jersey-hk2 CDI, JWT Authentication and role based Authorization 
(simple single role per user). There is a Client for each Contract, which allows for easier IT testing.

### Exercise brief

Design an API for a vending machine, allowing users with a “seller” role to add, update or remove products, while users with a “buyer” role can deposit coins into the machine and make purchases. Your vending machine should only accept 5, 10, 20, 50 and 100 cent coins

Tasks

- REST API should be implemented consuming and producing “application/json”
- Implement product model with amountAvailable, cost, productName and sellerId fields
- Implement user model with username, password, deposit and role fields
- Implement an authentication method (basic, oAuth, JWT or something else, the choice is yours)
- All the endpoints should be authenticated unless stated otherwise
- Implement CRUD for users (POST /user should not require authentication to allow new user registration)
- Implement CRUD for a product model (GET can be called by anyone, while POST, PUT and DELETE can be called only by the seller user who created the product)
- Implement /deposit endpoint so users with a “buyer” role can deposit only 5, 10, 20, 50 and 100 cent coins into their vending machine account
- Implement /buy endpoint (accepts productId, amount of products) so users with a “buyer” role can buy products with the money they’ve deposited. API should return total they’ve spent, products they’ve purchased and their change if there’s any (in an array of 5, 10, 20, 50 and 100 cent coins)
- Implement /reset endpoint so users with a “buyer” role can reset their deposit back to 0
- Take time to think about possible edge cases and access issues that should be solved

Evaluation criteria:

- Language/Framework of choice best practices
- Edge cases covered
- Write tests for /deposit, /buy and one CRUD endpoint of your choice
- Code readability and optimization

Bonus (not implemented):

- If somebody is already logged in with the same credentials, the user should be given a message "There is already an active session using your account". In this case the user should be able to terminate all the active sessions on their account via an endpoint i.e. /logout/all
- Attention to security
