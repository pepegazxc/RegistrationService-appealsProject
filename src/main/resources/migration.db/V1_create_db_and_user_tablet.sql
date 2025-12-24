CREATE DATABASE registration_service;
CREATE TABLE users(
    id BIGSERIAL PRIMARY KEY,
    name VARCHAR(50) NOT NULL,
    surname VARCHAR(50) NOT NULL,
    user_identifier VARCHAR(255) NOT NULL UNIQUE,
    cipher_email VARCHAR(255) NOT NULL UNIQUE,
    cipher_phone_number VARCHAR(255) NOT NULL UNIQUE,
    hash_password TEXT NOT NULL,

    created_at TIMESTAMP WITH TIME ZONE DEFAULT now() NOT NULL,
    updated_at TIMESTAMP WITH TIME ZONE DEFAULT now()
)