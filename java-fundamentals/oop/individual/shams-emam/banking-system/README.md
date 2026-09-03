# Banking System

A Java console application that simulates core banking operations while demonstrating fundamental Object-Oriented Programming principles.

The system manages customers and their bank accounts, supports multiple account types, and provides common banking operations such as deposits, withdrawals, and transfers.

This project is part of a Java and Spring Boot learning journey and focuses specifically on designing clean, maintainable object-oriented code before introducing frameworks, databases, or external dependencies.

> **Project Scope:** This is an educational project designed for practicing Java OOP concepts. It is not intended to represent a production-ready banking system.

---

## Overview

The application models a simplified banking environment in which:

- A bank manages multiple customers.
- A customer can own one or more accounts.
- Multiple account types can share common behavior.
- Customers can deposit and withdraw funds.
- Money can be transferred between accounts.
- Account and customer information can be displayed.
- Invalid banking operations are rejected.

The primary goal is to model the domain using proper object relationships and OOP principles rather than placing the entire application logic inside a single class.

---

## Features

The initial version of the system includes:

- Customer creation
- Bank account creation
- Multiple account types
- Deposit operations
- Withdrawal operations
- Transfers between accounts
- Balance inquiries
- Account information display
- Customer information display
- Basic transaction validation
- Protection against invalid account operations

---

## Account Types

The application currently supports two account types.

### Savings Account

Represents a standard savings account.

```text
SavingsAccount
      │
      └── extends Account
```

It inherits the common state and behavior defined by the `Account` abstraction and can introduce savings-specific rules when required.

### Current Account

Represents an account intended for regular banking transactions.

```text
CurrentAccount
      │
      └── extends Account
```

It shares the common account behavior while allowing current-account-specific behavior to be implemented independently.

---

## Domain Model

The core relationships are designed around the following structure:

```text
Bank
│
├── Customer
│   └── Account
│
└── Accounts
    ├── SavingsAccount
    └── CurrentAccount
```

At a high level:

- `Bank` manages customers and accounts.
- `Customer` represents an account holder.
- `Account` defines the common behavior of all bank accounts.
- `SavingsAccount` and `CurrentAccount` specialize `Account`.
- `Transferable` defines the contract for money transfers.

---

## Project Structure

```text
banking-system/
│
├── README.md
│
└── src/
    └── com/
        └── learning/
            └── banking/
                ├── Main.java
                ├── Bank.java
                ├── Customer.java
                ├── Account.java
                ├── SavingsAccount.java
                ├── CurrentAccount.java
                └── Transferable.java
```

All Java source files use the package:

```java
package com.learning.banking;
```

> The project structure may evolve as additional requirements and Java concepts are introduced.

---

## Class Responsibilities

### `Bank`

Represents the banking system and coordinates the main domain objects.

Responsibilities may include:

- Managing registered customers
- Managing bank accounts
- Finding customers
- Finding accounts
- Coordinating operations between accounts

---

### `Customer`

Represents a customer registered with the bank.

A customer can own one or more bank accounts.

Example relationship:

```text
Customer
│
└── Accounts
    ├── SavingsAccount
    └── CurrentAccount
```

The class is responsible for storing customer-related information and maintaining the customer's account relationships.

---

### `Account`

The base abstraction for all account types.

It stores common account information such as:

- Account number
- Account holder
- Balance

It also provides common banking operations such as:

```java
deposit(...)
withdraw(...)
getBalance()
displayAccountInfo()
```

The `Account` abstraction prevents duplicated logic between different account types.

---

### `SavingsAccount`

Represents a savings account.

It inherits common state and behavior from `Account`:

```java
SavingsAccount extends Account
```

Additional savings-specific rules can be introduced without modifying unrelated account types.

---

### `CurrentAccount`

Represents a current account.

It also inherits from `Account`:

```java
CurrentAccount extends Account
```

The class can override inherited behavior when the business rules for current accounts differ from savings accounts.

---

### `Transferable`

Defines the contract for objects that support money transfers.

Example:

```java
public interface Transferable {

    void transfer(Account targetAccount, double amount);

}
```

Using an interface separates the transfer contract from a specific implementation and demonstrates interface-based design.

---

### `Main`

The entry point of the application.

`Main` is responsible for creating demo objects, performing sample banking operations, and displaying the application results.

Business logic should remain inside the appropriate domain classes instead of being implemented directly in `Main`.

---

## Banking Operations

### Deposit

Adds money to an account.

Example:

```text
Current Balance : 1000 EGP
Deposit         :  500 EGP
--------------------------
New Balance     : 1500 EGP
```

Business rule:

```text
deposit amount > 0
```

Zero or negative deposits must be rejected.

---

### Withdraw

Removes money from an account when sufficient funds are available.

Example:

```text
Current Balance : 1500 EGP
Withdrawal      :  300 EGP
--------------------------
New Balance     : 1200 EGP
```

A withdrawal must fail when:

- The requested amount is zero.
- The requested amount is negative.
- The requested amount exceeds the available balance.

---

### Transfer

Moves money from one account to another.

Example:

```text
Before Transfer

Account A : 2000 EGP
Account B :  500 EGP

Transfer  :  600 EGP

After Transfer

Account A : 1400 EGP
Account B : 1100 EGP
```

A transfer must fail when:

- The amount is zero or negative.
- The source account has insufficient funds.
- The destination account is invalid.

---

## Business Rules

The initial version follows these rules:

1. Deposit amounts must be greater than zero.
2. Withdrawal amounts must be greater than zero.
3. An account cannot withdraw more than its available balance.
4. Transfer amounts must be greater than zero.
5. A transfer requires sufficient funds in the source account.
6. Every account must have a unique account number.
7. Account balances cannot be modified directly from outside the account class.
8. A customer can own multiple accounts.
9. Every account must belong to a valid customer.
10. Invalid operations must not modify account balances.

Additional rules can be introduced as the project evolves.

---

## Object-Oriented Programming Concepts

This project is specifically designed to practice the major OOP concepts in Java.

### Encapsulation

Sensitive object state is kept private and modified only through controlled behavior.

For example:

```java
private double balance;
```

Instead of modifying the value directly:

```java
account.balance = 5000;
```

the system exposes controlled operations:

```java
account.deposit(5000);
account.withdraw(1000);
```

This protects the internal state of the object and allows validation rules to be applied consistently.

---

### Inheritance

Shared account behavior is defined once in `Account`.

```text
             Account
            /       \
           /         \
SavingsAccount     CurrentAccount
```

Both account types reuse the common implementation while retaining the ability to define their own specialized behavior.

---

### Abstraction

`Account` represents the general concept of a bank account without requiring the rest of the application to depend on a specific account implementation.

For example:

```java
Account account = new SavingsAccount(...);
```

The application works with the abstraction while the concrete account type provides its implementation.

---

### Polymorphism

Different account implementations can be handled through the same parent type.

```java
Account savings = new SavingsAccount(...);
Account current = new CurrentAccount(...);
```

The actual object determines which overridden behavior executes at runtime.

---

### Method Overriding

Subclasses can customize inherited behavior when their business rules differ.

Example:

```java
@Override
public void withdraw(double amount) {
    // Account-specific withdrawal rules
}
```

This allows account types to share the same public contract while implementing different behavior.

---

### Interfaces

The `Transferable` interface defines a common transfer contract:

```java
public interface Transferable {

    void transfer(Account targetAccount, double amount);

}
```

This encourages programming against abstractions instead of tightly coupling the system to a single concrete implementation.

---

### Composition and Association

The project models relationships between domain objects.

For example:

```text
Bank
│
└── Customers
    │
    └── Accounts
```

Instead of placing every responsibility inside one class, objects collaborate to perform banking operations.

---

## Example Scenario

Consider two customers:

| Customer | Account Type | Initial Balance |
|---|---|---:|
| Ahmed | Savings Account | 5000 EGP |
| Omar | Current Account | 3000 EGP |

The application performs the following operations:

```text
Ahmed deposits 1000 EGP.

Omar withdraws 500 EGP.

Ahmed transfers 1500 EGP to Omar.
```

The resulting balances are:

```text
Ahmed -> 4500 EGP
Omar  -> 4000 EGP
```

---

## Example Output

A possible console output could look like:

```text
Customer created: Ahmed
Savings account created successfully.

Customer created: Omar
Current account created successfully.

Deposit successful.
Ahmed balance: 6000 EGP

Withdrawal successful.
Omar balance: 2500 EGP

Transfer successful.
Transferred 1500 EGP from Ahmed to Omar.

Final Account Balances
----------------------
Ahmed -> 4500 EGP
Omar  -> 4000 EGP
```

The exact output may change as the implementation evolves.

---

## Requirements

- JDK 8 or newer
- Java compiler (`javac`)
- Java Runtime Environment (`java`)

The first version does not require:

- Spring Boot
- A database
- External libraries
- Maven
- Gradle
- A graphical user interface

---

## Running the Project

### IntelliJ IDEA

1. Open the project in IntelliJ IDEA.
2. Mark `src` as the Sources Root if necessary.
3. Navigate to `Main.java`.
4. Run `Main.main()`.

---

### Windows PowerShell

From the `banking-system` directory:

```powershell
New-Item -ItemType Directory -Force out | Out-Null
javac -d out (Get-ChildItem -Recurse src -Filter *.java).FullName
java -cp out com.learning.banking.Main
```

---

### Linux / macOS

From the `banking-system` directory:

```bash
mkdir -p out
javac -d out $(find src -name "*.java")
java -cp out com.learning.banking.Main
```

---

## Technologies

- Java
- Object-Oriented Programming
- Git
- GitHub

Java Collections may be introduced where they naturally fit the implementation.

---

## Learning Objectives

By completing this project, the goal is to strengthen understanding of:

- Classes and Objects
- Constructors
- Encapsulation
- Inheritance
- Abstraction
- Polymorphism
- Method Overriding
- Interfaces
- Composition
- Object Associations
- Responsibility separation
- Basic domain modeling
- Clean object-oriented design

The project also provides practical experience with the Git/GitHub workflow used throughout this learning repository.

---

## Git Workflow

Development is performed on a dedicated feature branch:

```text
feature/oop/banking-system
```

The workflow is:

```text
main
  │
  └── feature/oop/banking-system
              │
              ├── implementation
              ├── commits
              ├── push
              └── pull request
                        │
                        ▼
                   Code Review
                        │
                        ▼
                  Merge into main
```

Changes are not committed directly to `main`.

The completed project is submitted through a Pull Request for review before being merged.

---

## Future Improvements

Possible future improvements include:

- Transaction history
- Transaction model
- Account statements
- Savings interest calculation
- Current-account overdraft rules
- Account search
- Account closing
- Customer search
- Custom exception handling
- PIN-based authentication
- File persistence
- Unit testing
- Maven or Gradle
- Database persistence
- Spring Boot REST API
- MySQL or PostgreSQL integration

These improvements are intentionally outside the scope of the initial OOP version.

---

## Project Status

**Status:** 🚧 In Development

The project will evolve as additional Java and Object-Oriented Programming concepts are learned and applied.

---

## Author

**Shams Emam**

Created as part of my Java and Spring Boot backend development learning journey, with a focus on strengthening Object-Oriented Programming and software design fundamentals.
