CREATE TABLE transactions
(
    id          BIGSERIAL PRIMARY KEY,
    user_id     INT            NOT NULL,
    value       NUMERIC(20, 2) NOT NULL,
    category_id INT            NOT NULL,
    time        TIMESTAMPTZ    NOT NULL DEFAULT NOW(),
    CONSTRAINT fk_transaction_user FOREIGN KEY (user_id) REFERENCES users (id),
    CONSTRAINT fk_transaction_category FOREIGN KEY (category_id) REFERENCES categories (id)
);
