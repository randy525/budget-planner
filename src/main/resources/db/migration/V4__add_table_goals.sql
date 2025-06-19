CREATE TABLE goals
(
    id             BIGSERIAL PRIMARY KEY,
    user_id        INT            NOT NULL,
    current_amount NUMERIC(20, 2) NOT NULL,
    goal_amount    NUMERIC(20, 2) NOT NULL,
    name           TEXT           NOT NULL,
    icon           TEXT,
    is_done        BOOLEAN        NOT NULL,
    CONSTRAINT fk_goal_user FOREIGN KEY (user_id) REFERENCES users (id)
);
