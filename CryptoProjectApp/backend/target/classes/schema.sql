-- Create database if it doesn't exist
CREATE DATABASE IF NOT EXISTS crypto_sim;
USE crypto_sim;

-- Drop existing tables if they exist (in correct order to handle foreign key constraints)
DROP TABLE IF EXISTS transactions;
DROP TABLE IF EXISTS portfolio;
DROP TABLE IF EXISTS users;

-- Create users table
CREATE TABLE users (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(20) NOT NULL UNIQUE,
    email VARCHAR(50) NOT NULL UNIQUE,
    password VARCHAR(100) NOT NULL,
    balance DECIMAL(19,2) NOT NULL DEFAULT 10000.00,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    CONSTRAINT chk_balance CHECK (balance >= 0)
);

-- Create portfolio table
CREATE TABLE portfolio (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    user_id BIGINT NOT NULL,
    symbol VARCHAR(10) NOT NULL,
    quantity DECIMAL(19,8) NOT NULL,
    average_price DECIMAL(19,2) NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE,
    UNIQUE KEY unique_user_symbol (user_id, symbol),
    CONSTRAINT chk_quantity CHECK (quantity >= 0),
    CONSTRAINT chk_price CHECK (average_price >= 0)
);

-- Create transactions table
CREATE TABLE transactions (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    user_id BIGINT NOT NULL,
    symbol VARCHAR(10) NOT NULL,
    quantity DECIMAL(19,8) NOT NULL,
    price DECIMAL(19,2) NOT NULL,
    type ENUM('BUY', 'SELL') NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE,
    CONSTRAINT chk_trans_quantity CHECK (quantity > 0),
    CONSTRAINT chk_trans_price CHECK (price > 0)
);

-- Create indices for better performance
CREATE INDEX idx_users_username ON users(username);
CREATE INDEX idx_users_email ON users(email);
CREATE INDEX idx_portfolio_user ON portfolio(user_id);
CREATE INDEX idx_transactions_user ON transactions(user_id);
CREATE INDEX idx_transactions_created_at ON transactions(created_at);

-- Create view for user portfolio summary
CREATE OR REPLACE VIEW user_portfolio_summary AS
SELECT 
    u.id as user_id,
    u.username,
    u.balance,
    COUNT(DISTINCT p.symbol) as total_assets,
    SUM(p.quantity * p.average_price) as total_investment
FROM users u
LEFT JOIN portfolio p ON u.id = p.user_id
GROUP BY u.id, u.username, u.balance; 