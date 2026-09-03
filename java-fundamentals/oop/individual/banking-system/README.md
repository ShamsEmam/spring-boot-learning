# Banking System

A Java console-based project designed to practice and demonstrate core Object-Oriented Programming principles through a simplified banking domain.

This document defines the **shared project specification** for the Banking System individual exercise.

Each developer implements the same requirements independently in a separate directory and feature branch. The implementations can then be reviewed and compared in terms of object-oriented design, code organization, and implementation decisions.

> **Scope:** This is an educational project intended for practicing Java and Object-Oriented Programming. It is not a production-ready banking application.

---

## Project Objective

The goal of this exercise is to model a small banking system using clean object-oriented design.

The system should demonstrate how responsibilities can be distributed between collaborating objects rather than placing all business logic inside a single class.

Each developer should independently design and implement the solution while satisfying the same functional requirements and business rules.

---

## Functional Requirements

The application should support the following operations:

- Create customers.
- Create bank accounts.
- Support multiple account types.
- Deposit money into an account.
- Withdraw money from an account.
- Transfer money between accounts.
- Display account information.
- Display customer information.
- Check the current account balance.
- Reject invalid banking operations.

---

## Account Types

The initial version should support at least two account types.

### Savings Account

Represents an account intended primarily for storing customer funds.

### Current Account

Represents an account intended for regular banking transactions.

Both account types should share common account behavior while allowing specialized behavior when required.

A possible inheritance structure is:

```text
             Account
            /       \
           /         \
SavingsAccount     CurrentAccount
```

The exact implementation is left to each developer.

---

## Business Rules

The first version of the system should satisfy the following rules:

1. Deposit amounts must be greater than zero.
2. Withdrawal amounts must be greater than zero.
3. An account cannot withdraw more than its available balance.
4. Transfer amounts must be greater than zero.
5. A transfer requires sufficient funds in the source account.
6. Every account must have a unique account number.
7. An account balance must not be modified directly from outside the account.
8. A customer may own one or more accounts.
9. Every account must belong to a valid customer.
10. Invalid operations must not modify account balances.

Additional rules may be introduced in later versions.

---

## Core Banking Operations

### Deposit

Adds money to an account.

Example:

```text
Current Balance : 1000 EGP
Deposit         :  500 EGP
--------------------------
New Balance     : 1500 EGP
```

A deposit must be rejected when the amount is zero or negative.

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

- the amount is zero;
- the amount is negative;
- the amount exceeds the available balance.

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

- the amount is zero or negative;
- the source account has insufficient funds;
- the destination account is invalid.

---

## Suggested Domain Model

The following model is provided as a starting point:

```text
Bank
│
├── Customers
│   └── Accounts
│
└── Accounts
    ├── SavingsAccount
    └── CurrentAccount
```

Possible domain types include:

```text
Bank
Customer
Account
SavingsAccount
CurrentAccount
Transferable
Main
```

These names are recommendations rather than strict implementation requirements.

Each developer is encouraged to make their own design decisions while keeping the business requirements intact.

---

## Suggested Responsibilities

### `Bank`

May be responsible for:

- managing customers;
- managing accounts;
- finding customers;
- finding accounts;
- coordinating operations between accounts.

---

### `Customer`

Represents a bank customer.

A customer may own one or more accounts.

Example:

```text
Customer
│
└── Accounts
    ├── SavingsAccount
    └── CurrentAccount
```

---

### `Account`

Represents the common abstraction for bank accounts.

Possible common state:

- account number;
- account holder;
- balance.

Possible common behavior:

```java
deposit(...)
withdraw(...)
getBalance()
displayAccountInfo()
```

---

### `SavingsAccount`

Represents a savings account and may extend the common `Account` abstraction.

---

### `CurrentAccount`

Represents a current account and may provide behavior different from a savings account when required.

---

### `Transferable`

A possible interface for objects that support money transfers.

For example:

```java
public interface Transferable {

    void transfer(Account targetAccount, double amount);

}
```

Using an interface is optional if a developer chooses another clean design that satisfies the requirements.

---

### `Main`

Acts as the entry point for the console application.

It should primarily demonstrate the system.

Business logic should remain inside the appropriate domain classes rather than being implemented directly in `Main`.

---

## OOP Concepts to Practice

The project is intended to reinforce the following concepts.

### Classes and Objects

Model banking entities using separate classes with clearly defined responsibilities.

### Encapsulation

Protect object state from uncontrolled modification.

For example:

```java
private double balance;
```

Instead of modifying the balance directly:

```java
account.balance = 5000;
```

use controlled behavior:

```java
account.deposit(5000);
account.withdraw(1000);
```

---

### Inheritance

Reuse common account behavior when multiple account types share the same characteristics.

```text
             Account
            /       \
           /         \
SavingsAccount     CurrentAccount
```

---

### Abstraction

Represent common concepts through higher-level abstractions rather than depending only on concrete implementations.

Example:

```java
Account account = new SavingsAccount(...);
```

---

### Polymorphism

Allow multiple account implementations to be handled through the same abstraction.

Example:

```java
Account savings = new SavingsAccount(...);
Account current = new CurrentAccount(...);
```

---

### Method Overriding

Allow subclasses to customize inherited behavior when different business rules are required.

Example:

```java
@Override
public void withdraw(double amount) {
    // Account-specific behavior
}
```

---

### Interfaces

Use interfaces when appropriate to define behavior independently from implementation.

---

### Composition and Association

Model relationships between objects such as:

```text
Bank
│
└── Customers
    │
    └── Accounts
```

The goal is to let objects collaborate while keeping responsibilities separated.

---

## Example Scenario

Consider the following customers:

| Customer | Account Type | Initial Balance |
|---|---|---:|
| Ahmed | Savings Account | 5000 EGP |
| Omar | Current Account | 3000 EGP |

The application performs:

```text
Ahmed deposits 1000 EGP.

Omar withdraws 500 EGP.

Ahmed transfers 1500 EGP to Omar.
```

Expected final balances:

```text
Ahmed -> 4500 EGP
Omar  -> 4000 EGP
```

---

## Acceptance Criteria

An implementation is considered complete when:

- Customers can be created.
- Accounts can be created and associated with customers.
- At least two account types are supported.
- Deposits update balances correctly.
- Withdrawals update balances correctly.
- Invalid withdrawals are rejected.
- Transfers update both accounts correctly.
- Invalid transfers are rejected.
- Account balances cannot be modified directly.
- The application demonstrates the required OOP concepts.
- The application can be run successfully from `Main`.

---

## Repository Structure

The shared project specification and individual implementations are organized as follows:

```text
spring-boot-learning/
└── java-fundamentals/
    └── oop/
        └── individual/
            └── banking-system/
                │
                ├── README.md
                │
                ├── shams-emam/
                │   └── src/
                │
                └── <teammate-name>/
                    └── src/
```

`README.md` contains the shared project requirements.

Each developer owns their own implementation directory.

For example:

```text
banking-system/
├── README.md
│
├── shams-emam/
│   └── src/
│
└── teammate-name/
    └── src/
```

Developers should not modify another developer's implementation unless explicitly collaborating on a change.

---

## Individual Implementations

The same requirements are implemented independently by each developer.

### Shams Emam

```text
banking-system/
└── shams-emam/
    └── src/
```

### Teammate

```text
banking-system/
└── <teammate-name>/
    └── src/
```

This allows different designs and implementations of the same problem to be reviewed and compared.

---

## Git Workflow

The shared specification and individual implementations use separate branches.

### Shared Specification

Documentation changes use a documentation branch:

```text
docs/oop/banking-system-specification
```

Workflow:

```text
main
  │
  └── docs/oop/banking-system-specification
                │
                ├── documentation
                ├── commit
                ├── push
                └── pull request
                         │
                         ▼
                    Coach Review
                         │
                         ▼
                   Merge into main
```

---

### Individual Implementation

Each developer creates a separate feature branch from the latest `main`.

Branch convention:

```text
feature/oop/<developer>/<project>
```

Example:

```text
feature/oop/shams-emam/banking-system
```

Another developer may use:

```text
feature/oop/<teammate-name>/banking-system
```

Workflow:

```text
main
  │
  ├── feature/oop/shams-emam/banking-system
  │             │
  │             └── Shams implementation
  │
  └── feature/oop/<teammate-name>/banking-system
                │
                └── Teammate implementation
```

Each developer follows:

```text
Update main
    ↓
Create branch
    ↓
Implement
    ↓
Commit
    ↓
Push
    ↓
Pull Request
    ↓
Coach Review
    ↓
Coach Merge
```

Changes should not be committed directly to `main`.

---

## Commit Convention

Meaningful commit messages should be used throughout development.

Examples:

```text
feat: implement account base class
feat: add savings and current account types
feat: implement deposit and withdrawal operations
feat: implement account transfers
refactor: improve account validation logic
docs: update banking system documentation
fix: prevent withdrawal with insufficient balance
```

---

## Requirements

- JDK 8 or newer
- Java compiler (`javac`)
- Java Runtime Environment (`java`)

The initial version does not require:

- Spring Boot
- Database integration
- External libraries
- Maven
- Gradle
- GUI

---

## Implementation Guidelines

Each implementation should:

- use meaningful class and method names;
- keep fields properly encapsulated;
- avoid putting all logic inside `Main`;
- assign clear responsibilities to classes;
- avoid unnecessary duplicated code;
- validate banking operations before changing state;
- use inheritance and interfaces only where they improve the design;
- remain understandable and maintainable.

The goal is not to use every OOP feature unnecessarily.

The goal is to make appropriate design decisions.

---

## Running an Implementation

Each developer's implementation should contain its own executable `Main` class.

For example:

```text
banking-system/
└── shams-emam/
    └── src/
```

The exact compile command depends on the package structure chosen by the developer.

The implementation should also be runnable directly from IntelliJ IDEA.

---

## Future Improvements

Possible extensions include:

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

These features are intentionally outside the scope of the initial OOP exercise.

---

## Learning Outcomes

After completing the project, developers should have practical experience with:

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
- Domain modeling
- Clean object-oriented design
- Git branches
- Commits
- Pull Requests
- Code Review

---

## Project Purpose

This project is part of our Java and Spring Boot backend development learning journey.

It is designed as an individual OOP exercise where each developer implements the same Banking System requirements independently.

The resulting implementations can then be reviewed and compared to improve understanding of Object-Oriented Programming, software design, Git, and collaborative development.

---

## Project Status

**Status:** 🚧 Specification Ready / Implementations In Development