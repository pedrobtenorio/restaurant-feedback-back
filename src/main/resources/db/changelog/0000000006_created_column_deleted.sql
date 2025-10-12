--liquibase formatted sql

--changeset ruan_gomes:created_column_deleted
ALTER TABLE dish ADD COLUMN deleted BOOLEAN DEFAULT FALSE;
