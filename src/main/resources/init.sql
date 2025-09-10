-- Initialize database for Vestibulize application
-- This script runs when the MySQL container starts for the first time

-- Create database if it doesn't exist
CREATE DATABASE IF NOT EXISTS tg_vestibulize;

-- Use the database
USE tg_vestibulize;

-- Create user if it doesn't exist
CREATE USER IF NOT EXISTS 'vestibulize'@'%' IDENTIFIED BY 'vestibulize123';

-- Grant privileges
GRANT ALL PRIVILEGES ON tg_vestibulize.* TO 'vestibulize'@'%';

-- Flush privileges
FLUSH PRIVILEGES;

-- You can add initial data here if needed
-- Example:
-- INSERT INTO users (username, email, created_at) VALUES ('admin', 'admin@vestibulize.com', NOW());
