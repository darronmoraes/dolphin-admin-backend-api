-- create users
CREATE TABLE users (
    id VARCHAR(36) NOT NULL,
    full_name VARCHAR(50) NOT NULL,
    email VARCHAR(50) NOT NULL,
    password VARCHAR(120) NOT NULL,
    deleted TINYINT(1) NOT NULL DEFAULT 0,
    status TINYINT(1) NOT NULL DEFAULT 1,
    type ENUM ('admin', 'dev', 'employee') NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,

    -- constraints
    CONSTRAINT bookings_key_1 PRIMARY KEY (id),
    CONSTRAINT bookings_key_2 UNIQUE (email)
);