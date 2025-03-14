-- create bookings
CREATE TABLE bookings (
    id VARCHAR(36) NOT NULL,
    serial_number VARCHAR(15) NOT NULL,
    passenger INTEGER NOT NULL,
    per_passenger_amount DOUBLE NOT NULL,
    total_amount DOUBLE NOT NULL,
    payment_mode ENUM ('upi', 'cash', 'card') NOT NULL,
    customer_gstn VARCHAR(45) NULL,
    vehicle_last_four_digits VARCHAR(4) NOT NULL,
    vehicle_type ENUM ('cab', 'mini_bus', 'bus', 'two_wheeler', 'car') NOT NULL,
    vehicle_name VARCHAR(20) NULL,
    commission_amount DOUBLE NOT NULL,
    commission_status ENUM ('pending', 'processed', 'paid', 'failed', 'refunded', 'unavailable') NOT NULL DEFAULT 'pending',
    commission_paid_contact_name VARCHAR(20),
    commission_paid_contact_number VARCHAR(10),
    commissioned TINYINT(1) NOT NULL DEFAULT 0,
    issued_by VARCHAR(36) NOT NULL,
    booking_status ENUM ('booked', 'pending', 'deleted') NOT NULL,
    booked_on TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    deleted TINYINT(1) NOT NULL DEFAULT 0,
    deleted_on TIMESTAMP,
    deleted_by VARCHAR(36),

    -- constraints
    CONSTRAINT bookings_key_1 PRIMARY KEY (id),
    CONSTRAINT bookings_key_2 UNIQUE (serial_number),
    CONSTRAINT bookings_key_3 FOREIGN KEY (issued_by) REFERENCES users (id),
    CONSTRAINT bookings_key_4 FOREIGN KEY (deleted_by) REFERENCES users (id)
);