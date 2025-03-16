CREATE TABLE incomes
(
    id      SERIAL PRIMARY KEY,
    user_id INT            NOT NULL,
    source  TEXT           NOT NULL,
    amount  NUMERIC(20, 2) NOT NULL,
    period  TEXT           NOT NULL,
    CONSTRAINT fk_income_user FOREIGN KEY (user_id) REFERENCES users (id)
);
