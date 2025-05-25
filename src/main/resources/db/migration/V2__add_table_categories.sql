CREATE TABLE categories
(
    id   SERIAL PRIMARY KEY,
    name TEXT NOT NULL,
    icon TEXT NOT NULL,
    is_income BOOLEAN NOT NULL DEFAULT FALSE
);
