-- Create the users table
CREATE TABLE IF NOT EXISTS users(
                       id INTEGER PRIMARY KEY AUTOINCREMENT,
                       username TEXT NOT NULL UNIQUE,
                       password TEXT NOT NULL,
                       role TEXT NOT NULL
);

-- Optionally, insert some test data
-- INSERT INTO users (username, password, role) VALUES
--                                                 ('admin', 'adminpass', 'ADMIN'),
--                                                ('cashier', 'cashierpass', 'CASHIER');
-- Create the 'categories' table
CREATE TABLE IF NOT EXISTS categories (
                            id INTEGER PRIMARY KEY AUTOINCREMENT, -- Primary key (auto-incremented)
                            name TEXT NOT NULL,                   -- Category name (required)
                            description TEXT                      -- Category description (optional)
);

-- Create the 'products' table
CREATE TABLE IF NOT EXISTS products (
                          barcode TEXT PRIMARY KEY,             -- Barcode as the primary key (unique identifier)
                          name TEXT NOT NULL,                   -- Product name (required)
                          price DECIMAL(10, 2) NOT NULL,        -- Product price with precision (required)
                          category_id INTEGER,                  -- Foreign key referencing the 'categories' table
                          FOREIGN KEY (category_id) REFERENCES categories(id)
                              ON DELETE SET NULL                -- Set to NULL if the referenced category is deleted
);

-- Create the 'cart_items' table
CREATE TABLE IF NOT EXISTS cart_items (
                            id INTEGER PRIMARY KEY AUTOINCREMENT, -- Primary key (auto-incremented)
                            product_name TEXT NOT NULL,           -- Name of the product in the cart (required)
                            quantity INTEGER NOT NULL,            -- Quantity of the product in the cart (required)
                            price DECIMAL(10, 2) NOT NULL         -- Unit price of the product (required)
);

-- Create the 'payments' table
CREATE TABLE IF NOT EXISTS payments (
                          id INTEGER PRIMARY KEY AUTOINCREMENT, -- Primary key (auto-incremented)
                          amount DECIMAL(10, 2) NOT NULL,       -- Payment amount (required)
                          method TEXT NOT NULL,                 -- Payment method (e.g., Credit Card, PayPal)
                          status TEXT NOT NULL,                 -- Payment status (e.g., PENDING, COMPLETED)
                          success BOOLEAN NOT NULL              -- Indicates whether the payment was successful
);

