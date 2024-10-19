CREATE TABLE IF NOT EXISTS users (
                                     id BIGINT AUTO_INCREMENT PRIMARY KEY,
                                     username VARCHAR(50) UNIQUE NOT NULL,
                                     password VARCHAR(255) NOT NULL,
                                     role VARCHAR(50) NOT NULL
);

CREATE TABLE IF NOT EXISTS products (
                                        barcode VARCHAR(255) PRIMARY KEY,
                                        name VARCHAR(255) NOT NULL,
                                        price DECIMAL(10, 2) NOT NULL,
                                        category_id BIGINT
);

-- Insert an admin user only if it does not exist
INSERT INTO users (username, password, role)
SELECT 'admin', 'password', 'ADMIN'
WHERE NOT EXISTS (
    SELECT 1 FROM users WHERE username = 'admin'
);
