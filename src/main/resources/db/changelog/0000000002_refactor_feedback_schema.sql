--liquibase formatted sql

--changeset jeanPMartins:refactor-feedback-table

ALTER TABLE feedback RENAME COLUMN comment TO general_comment;

ALTER TABLE feedback
    ADD COLUMN attendant_name VARCHAR(255),
    ADD COLUMN recommendation_rating INT,
    ADD COLUMN service_comment TEXT,
    ADD COLUMN food_comment TEXT,
    ADD COLUMN environment_comment TEXT;

--rollback ALTER TABLE feedback RENAME COLUMN general_comment TO comment;
--rollback ALTER TABLE feedback DROP COLUMN attendant_name;
--rollback ALTER TABLE feedback DROP COLUMN recommendation_rating;
--rollback ALTER TABLE feedback DROP COLUMN service_comment;
--rollback ALTER TABLE feedback DROP COLUMN food_comment;
--rollback ALTER TABLE feedback DROP COLUMN environment_comment;
