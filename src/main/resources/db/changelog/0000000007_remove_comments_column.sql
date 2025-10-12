--liquibase formatted sql
--changeset pedro_tenori:remove_feedback_comments_columns
ALTER TABLE feedback DROP COLUMN IF EXISTS service_comment;
ALTER TABLE feedback DROP COLUMN IF EXISTS food_comment;
ALTER TABLE feedback DROP COLUMN IF EXISTS environment_comment;
