DELIMITER //

CREATE FUNCTION get_next_serial_number()
RETURNS VARCHAR(15)
DETERMINISTIC
BEGIN
    DECLARE last_serial VARCHAR(15);
    DECLARE last_number INT;
    DECLARE next_number VARCHAR(15);
    DECLARE company_prefix VARCHAR(3) DEFAULT 'AWS';
    DECLARE current_year VARCHAR(4);
    DECLARE current_month VARCHAR(2);

    -- Get the current year and month
    SET current_year = DATE_FORMAT(NOW(), '%Y');
    SET current_month = LPAD(MONTH(NOW()), 2, '0');

    -- Fetch the last serial number from the bookings table for the current month
    SELECT serial_number INTO last_serial
    FROM bookings
    WHERE serial_number LIKE CONCAT(company_prefix, current_year, current_month, '%')
    ORDER BY serial_number DESC
    LIMIT 1;

    -- Extract the last digit (ticket number) and increment, resetting count each month
    IF last_serial IS NOT NULL THEN
        SET last_number = CAST(RIGHT(last_serial, 1) AS UNSIGNED) + 1;
    ELSE
        SET last_number = 1;
    END IF;

    -- Generate the next serial number
    SET next_number = CONCAT(company_prefix, current_year, current_month, last_number);

    RETURN next_number;
END //

DELIMITER ;