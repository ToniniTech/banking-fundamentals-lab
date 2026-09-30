CREATE TABLE bank_account (
    id UUID PRIMARY KEY,
    balance NUMERIC(19, 2) NOT NULL,
    currency_code VARCHAR(3) NOT NULL,
    status VARCHAR(20) NOT NULL,
    CONSTRAINT ck_bank_account_balance_non_negative CHECK (balance >= 0),
    CONSTRAINT ck_bank_account_currency_code_length CHECK (char_length(currency_code) = 3),
    CONSTRAINT ck_bank_account_status CHECK (status IN ('ACTIVE', 'BLOCKED'))
);
