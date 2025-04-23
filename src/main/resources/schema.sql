-- Enable UUID extension
CREATE EXTENSION IF NOT EXISTS "uuid-ossp";

-- Users table
CREATE TABLE users (
  id UUID PRIMARY KEY,
  email VARCHAR(255) UNIQUE NOT NULL,
  access_type VARCHAR(50) NOT NULL DEFAULT 'user',
  created_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
  updated_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP
);

-- Cars table
CREATE TABLE cars (
  id UUID PRIMARY KEY,
  user_id UUID REFERENCES users(id),
  name VARCHAR(255) NOT NULL,
  registration_number VARCHAR(255) UNIQUE NOT NULL,
  manufacture_date DATE NOT NULL,
  purchase_date DATE NOT NULL,
  seller_name VARCHAR(255),
  listing_platforms VARCHAR(255),
  documents CLOB,
  created_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
  updated_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP
);

-- Ownership details table
CREATE TABLE ownership_details (
  id UUID PRIMARY KEY,
  car_id UUID REFERENCES cars(id),
  owner_name VARCHAR(255) NOT NULL,
  aadhaar_number VARCHAR(255) NOT NULL,
  pan_number VARCHAR(255) NOT NULL,
  driving_license VARCHAR(255) NOT NULL,
  phone_number VARCHAR(255) NOT NULL,
  location VARCHAR(255) NOT NULL,
  documents CLOB,
  created_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
  updated_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP
);

-- Loan details table
CREATE TABLE loan_details (
  id UUID PRIMARY KEY,
  car_id UUID REFERENCES cars(id),
  bank_name VARCHAR(255) NOT NULL,
  loan_amount DECIMAL(10,2) NOT NULL,
  loan_status VARCHAR(50) NOT NULL CHECK (loan_status IN ('pending', 'cleared')),
  created_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
  updated_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP
);

-- Brokers table
CREATE TABLE brokers (
  id UUID PRIMARY KEY,
  name VARCHAR(255) NOT NULL,
  phone_number VARCHAR(255) NOT NULL,
  created_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
  updated_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP
);

-- Transactions table
CREATE TABLE transactions (
  id UUID PRIMARY KEY,
  car_id UUID REFERENCES cars(id),
  broker_id UUID REFERENCES brokers(id),
  transaction_type VARCHAR(50) NOT NULL CHECK (transaction_type IN ('sale', 'purchase', 'loan_payment')),
  payment_method VARCHAR(50) NOT NULL CHECK (payment_method IN ('cash', 'upi', 'bank_transfer', 'finance')),
  amount DECIMAL(10,2) NOT NULL,
  transaction_id VARCHAR(255),
  bank_details CLOB,
  notes TEXT,
  status VARCHAR(50) NOT NULL DEFAULT 'pending' CHECK (status IN ('pending', 'completed', 'failed')),
  created_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
  updated_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP
);

-- Broker fees table
CREATE TABLE broker_fees (
  id UUID PRIMARY KEY,
  broker_id UUID REFERENCES brokers(id),
  transaction_id UUID REFERENCES transactions(id),
  fee_amount DECIMAL(10,2) NOT NULL,
  created_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
  updated_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP
);

-- Create indexes for better query performance
CREATE INDEX idx_cars_user_id ON cars(user_id);
CREATE INDEX idx_ownership_car_id ON ownership_details(car_id);
CREATE INDEX idx_loan_car_id ON loan_details(car_id);
CREATE INDEX idx_transactions_car_id ON transactions(car_id);
CREATE INDEX idx_transactions_broker_id ON transactions(broker_id);
CREATE INDEX idx_broker_fees_broker_id ON broker_fees(broker_id);
CREATE INDEX idx_broker_fees_transaction_id ON broker_fees(transaction_id); 