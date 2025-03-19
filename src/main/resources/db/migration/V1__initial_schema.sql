-- Create accounts table
CREATE TABLE accounts
(
    id         BIGINT(20) auto_increment PRIMARY KEY,
    balance    DECIMAL(38,2) NOT NULL,
    created_at DATETIME(6) NOT NULL
);

-- Create withdrawal_transactions table
CREATE TABLE transactions
(
    id             BIGINT(20) auto_increment PRIMARY KEY,
    account_id     BIGINT(20) NOT NULL,
    balance_after  DECIMAL(38,2) NOT NULL,
    balance_before DECIMAL(38,2) NOT NULL,
    created_at     DATETIME(6) NOT NULL,
    CONSTRAINT fk_account_id FOREIGN KEY (account_id) REFERENCES accounts(id)
);