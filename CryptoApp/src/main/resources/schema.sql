-- Drop tables if they exist (in reverse order of dependencies)
DROP TABLE IF EXISTS transactions;
DROP TABLE IF EXISTS holdings;
DROP TABLE IF EXISTS cryptocurrencies;
DROP TABLE IF EXISTS users;

-- Create users table
CREATE TABLE users (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(50) UNIQUE NOT NULL,
    email VARCHAR(100) UNIQUE NOT NULL,
    password VARCHAR(255) NOT NULL,
    balance DECIMAL(18, 2) NOT NULL DEFAULT 10000.00,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);

-- Create cryptocurrencies table
CREATE TABLE cryptocurrencies (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    symbol VARCHAR(10) UNIQUE NOT NULL,
    name VARCHAR(100) NOT NULL,
    current_price DECIMAL(18, 8) NOT NULL,
    market_cap DECIMAL(24, 2) NOT NULL,
    volume_24h DECIMAL(24, 2) NOT NULL,
    price_change_percentage_24h DECIMAL(10, 2) NOT NULL,
    image_url VARCHAR(255),
    last_updated TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);

-- Create holdings table
CREATE TABLE holdings (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    user_id BIGINT NOT NULL,
    cryptocurrency_id BIGINT NOT NULL,
    quantity DECIMAL(18, 8) NOT NULL DEFAULT 0.0,
    average_buy_price DECIMAL(18, 8),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    UNIQUE KEY unique_user_crypto (user_id, cryptocurrency_id),
    FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE,
    FOREIGN KEY (cryptocurrency_id) REFERENCES cryptocurrencies(id) ON DELETE CASCADE
);

-- Create transactions table
CREATE TABLE transactions (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    user_id BIGINT NOT NULL,
    cryptocurrency_id BIGINT NOT NULL,
    type ENUM('BUY', 'SELL') NOT NULL,
    quantity DECIMAL(18, 8) NOT NULL,
    price DECIMAL(18, 8) NOT NULL,
    total_amount DECIMAL(18, 2) NOT NULL,
    timestamp TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE,
    FOREIGN KEY (cryptocurrency_id) REFERENCES cryptocurrencies(id) ON DELETE CASCADE
);

-- Create indexes for better performance
CREATE INDEX idx_transactions_user_id ON transactions(user_id);
CREATE INDEX idx_transactions_cryptocurrency_id ON transactions(cryptocurrency_id);
CREATE INDEX idx_holdings_user_id ON holdings(user_id);
CREATE INDEX idx_holdings_cryptocurrency_id ON holdings(cryptocurrency_id);
CREATE INDEX idx_cryptocurrencies_symbol ON cryptocurrencies(symbol);
