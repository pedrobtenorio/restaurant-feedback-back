--liquibase formatted sql

--changeset pedro_tenorio:create-initial-schema

-- Tabela de mesas
CREATE TABLE dining_table (
                              id BIGSERIAL PRIMARY KEY,
                              identifier VARCHAR(255)
);

-- Tabela de pratos
CREATE TABLE dish (
                      id BIGSERIAL PRIMARY KEY,
                      name VARCHAR(255),
                      description TEXT
);

-- Tabela de feedbacks gerais
CREATE TABLE feedback (
                          id BIGSERIAL PRIMARY KEY,
                          customer_name VARCHAR(255),
                          service_rating INT,
                          food_rating INT,
                          environment_rating INT,
                          comment TEXT,
                          timestamp TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                          table_id BIGINT,
                          CONSTRAINT fk_feedback_table FOREIGN KEY (table_id) REFERENCES dining_table(id)
);

-- Tabela de feedbacks de pratos
CREATE TABLE dish_feedback (
                               id BIGSERIAL PRIMARY KEY,
                               rating INT,
                               comment TEXT,
                               dish_id BIGINT,
                               feedback_id BIGINT,
                               CONSTRAINT fk_dishfeedback_dish FOREIGN KEY (dish_id) REFERENCES dish(id),
                               CONSTRAINT fk_dishfeedback_feedback FOREIGN KEY (feedback_id) REFERENCES feedback(id)
);

-- Índices úteis
CREATE INDEX idx_feedback_table_id ON feedback (table_id);
CREATE INDEX idx_dishfeedback_dish_id ON dish_feedback (dish_id);
CREATE INDEX idx_dishfeedback_feedback_id ON dish_feedback (feedback_id);
CREATE INDEX idx_feedback_timestamp ON feedback (timestamp);
CREATE INDEX idx_feedback_customer_name ON feedback (customer_name);
