--liquibase formatted sql

--changeset pedro_tenorio:insert-example-dishes
INSERT INTO dish (name, description) VALUES ('Feijoada', 'Prato típico brasileiro com feijão preto e carnes.');
INSERT INTO dish (name, description) VALUES ('Moqueca', 'Ensopado de peixe com leite de coco e dendê.');
INSERT INTO dish (name, description) VALUES ('Risoto de Camarão', 'Risoto cremoso com camarões frescos.');
INSERT INTO dish (name, description) VALUES ('Lasanha', 'Lasanha de carne com molho de tomate e queijo.');
INSERT INTO dish (name, description) VALUES ('Strogonoff de Frango', 'Frango ao molho cremoso com champignon.');
INSERT INTO dish (name, description) VALUES ('Picanha', 'Corte nobre de carne bovina grelhada.');
INSERT INTO dish (name, description) VALUES ('Salada Caesar', 'Salada com alface, frango, croutons e molho Caesar.');
INSERT INTO dish (name, description) VALUES ('Pizza Margherita', 'Pizza com molho de tomate, mussarela e manjericão.');
INSERT INTO dish (name, description) VALUES ('Torta de Limão', 'Sobremesa com creme de limão e merengue.');
INSERT INTO dish (name, description) VALUES ('Brigadeiro', 'Doce brasileiro feito de chocolate e leite condensado.');

