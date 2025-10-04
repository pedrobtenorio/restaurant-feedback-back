--liquibase formatted sql
--changeset pedro_tenorio:create-attendant-table

-- 1. Criação da tabela attendants
CREATE TABLE attendants (
                            id BIGSERIAL PRIMARY KEY,
                            name VARCHAR(255) NOT NULL,
                            email VARCHAR(255),
                            active BOOLEAN DEFAULT TRUE
);

-- 2. Criar um attendant padrão
INSERT INTO attendants (name, email, active)
VALUES ('Atendente Padrão', 'default@restaurant.com', TRUE);

-- 3. Remover coluna attendant_name da tabela feedback, se existir
ALTER TABLE feedback DROP COLUMN IF EXISTS attendant_name;

-- 4. Adicionar coluna attendant_id na tabela feedback
ALTER TABLE feedback ADD COLUMN attendant_id BIGINT;

-- 5. Atualizar todos os feedbacks que não possuem attendant para apontar para o padrão
UPDATE feedback
SET attendant_id = (SELECT id FROM attendants WHERE name = 'Atendente Padrão')
WHERE attendant_id IS NULL;

-- 6. Criar relacionamento entre feedback e attendants
ALTER TABLE feedback
    ADD CONSTRAINT fk_feedback_attendant
        FOREIGN KEY (attendant_id)
            REFERENCES attendants(id);
