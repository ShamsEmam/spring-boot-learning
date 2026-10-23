-- =====================================================================
-- Banking System - database schema (MySQL 8)
-- Run this whole file once in IntelliJ (Database tool window -> Run SQL Script).
-- It is safe to run again: tables are only created if they do not exist.
--
-- UPGRADING from the previous version? Its column names were different
-- (account_number, branch_code). Drop the old tables first:
--   DROP TABLE IF EXISTS accounts;
--   DROP TABLE IF EXISTS branches;
--   DROP TABLE IF EXISTS customers;
-- =====================================================================
DROP TABLE IF EXISTS accounts;
DROP TABLE IF EXISTS branches;
DROP TABLE IF EXISTS customers;


CREATE DATABASE IF NOT EXISTS banking_db
    CHARACTER SET utf8mb4
    COLLATE utf8mb4_unicode_ci;

USE banking_db;

-- ---------------------------------------------------------------------
-- customers
-- ---------------------------------------------------------------------
CREATE TABLE IF NOT EXISTS customers (
    customer_id VARCHAR(20)  NOT NULL,
    name        VARCHAR(100) NOT NULL,
    email       VARCHAR(150) NOT NULL,
    password    VARCHAR(255) NOT NULL,
    created_at  TIMESTAMP    NOT NULL DEFAULT CURRENT_TIMESTAMP,

    CONSTRAINT pk_customers       PRIMARY KEY (customer_id),
    CONSTRAINT uq_customers_email UNIQUE (email)
);

-- ---------------------------------------------------------------------
-- branches
-- ---------------------------------------------------------------------
CREATE TABLE IF NOT EXISTS branches (
    branch_id VARCHAR(20)  NOT NULL,
    name      VARCHAR(100) NOT NULL,
    address   VARCHAR(200) NOT NULL,
    phone     VARCHAR(20)  NOT NULL,
    email     VARCHAR(150) NOT NULL,
    manager   VARCHAR(100) NOT NULL,

    CONSTRAINT pk_branches PRIMARY KEY (branch_id)
);

-- ---------------------------------------------------------------------
-- accounts
--
-- One table for all four account types ("single table inheritance").
-- A column that belongs to one type only is NULL for the other types.
--
-- SOFT DELETE: rows are never removed. Closing an account sets
-- is_active = FALSE and records deactivated_at; all data is kept.
-- ---------------------------------------------------------------------
CREATE TABLE IF NOT EXISTS accounts (
    account_id   VARCHAR(20)   NOT NULL,
    account_type VARCHAR(20)   NOT NULL,
    balance      DECIMAL(19,2) NOT NULL,
    customer_id  VARCHAR(20)   NOT NULL,

    interest_rate    DECIMAL(5,2)  NULL,   -- SAVINGS only
    minimum_balance  DECIMAL(19,2) NULL,   -- SAVINGS only
    overdraft_limit  DECIMAL(19,2) NULL,   -- CURRENT only
    withdrawal_limit DECIMAL(19,2) NULL,   -- LIMITED only

    is_active      BOOLEAN   NOT NULL DEFAULT TRUE,
    deactivated_at TIMESTAMP NULL,
    created_at     TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,

    CONSTRAINT pk_accounts          PRIMARY KEY (account_id),
    CONSTRAINT fk_accounts_customer FOREIGN KEY (customer_id)
        REFERENCES customers (customer_id),

    CONSTRAINT ck_accounts_type CHECK (
        account_type IN ('STANDARD', 'SAVINGS', 'CURRENT', 'LIMITED')),

    -- Only a CURRENT account may go below zero (overdraft)
    CONSTRAINT ck_accounts_balance CHECK (
        account_type = 'CURRENT' OR balance >= 0),

    -- Each type must have the columns it needs
    CONSTRAINT ck_accounts_savings CHECK (
        account_type <> 'SAVINGS'
        OR (interest_rate IS NOT NULL AND minimum_balance IS NOT NULL)),
    CONSTRAINT ck_accounts_current CHECK (
        account_type <> 'CURRENT' OR overdraft_limit IS NOT NULL),
    CONSTRAINT ck_accounts_limited CHECK (
        account_type <> 'LIMITED' OR withdrawal_limit IS NOT NULL),

    -- is_active and deactivated_at must always agree
    CONSTRAINT ck_accounts_soft_delete CHECK (
        (is_active = TRUE  AND deactivated_at IS NULL)
        OR (is_active = FALSE AND deactivated_at IS NOT NULL))
);
SELECT account_id, balance, is_active, deactivated_at
FROM accounts
WHERE is_active = FALSE;

CREATE INDEX idx_accounts_customer ON accounts (customer_id);
CREATE INDEX idx_accounts_active   ON accounts (is_active);
DESCRIBE accounts;