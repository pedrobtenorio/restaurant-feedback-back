--liquibase formatted sql

--changeset pedro:remove-dining-table

-- Remove foreign key constraint da tabela feedback
ALTER TABLE feedback DROP CONSTRAINT IF EXISTS fk_feedback_table;

-- Remove índice relacionado à coluna table_id
DROP INDEX IF EXISTS idx_feedback_table_id;

-- Remove coluna table_id da tabela feedback
ALTER TABLE feedback DROP COLUMN IF EXISTS table_id;

-- Drop da tabela dining_table
DROP TABLE IF EXISTS dining_table;
