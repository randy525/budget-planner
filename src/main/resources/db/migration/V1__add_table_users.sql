CREATE TABLE users
(
    id            SERIAL PRIMARY KEY,
    email         TEXT NOT NULL,
    name          TEXT NOT NULL,
    password      TEXT NOT NULL,
    plan          TEXT,
    plan_due_date DATE
);
