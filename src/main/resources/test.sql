-- Initialization script for SQLite database
DROP TABLE IF EXISTS users;

CREATE TABLE IF NOT EXISTS test (
                                    id INTEGER PRIMARY KEY AUTOINCREMENT,
                                    name TEXT NOT NULL
);


CREATE TABLE users (
                       id INTEGER PRIMARY KEY AUTOINCREMENT,
                       username TEXT NOT NULL,
                       password TEXT NOT NULL,
                       role TEXT NOT NULL
);

INSERT INTO users (username, password, role) VALUES
                                                 ('admin', 'adminpass', 'ADMIN'),
                                                 ('cashier', 'cashierpass', 'CASHIER');
