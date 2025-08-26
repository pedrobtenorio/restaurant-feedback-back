--liquibase formatted sql

--changeset pedro_tenorio:create-user-table
CREATE TABLE users (
    id BIGSERIAL PRIMARY KEY,
    username VARCHAR(255),
    role VARCHAR(50),
    email VARCHAR(255) UNIQUE,
    password VARCHAR(255),
    created_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    last_updated_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);
