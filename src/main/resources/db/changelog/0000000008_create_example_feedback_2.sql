-- Script para inserir novos registros de feedback na tabela 'feedbacks'.
-- Todos os feedbacks são atribuídos ao attendant_id = 1.

INSERT INTO feedback (customer_name, service_rating, food_rating, environment_rating, general_comment, "timestamp", recommendation_rating, attendant_id) VALUES
('Ana Silva', 10, 10, 9, 'Atendimento impecável e a comida estava simplesmente divina! Ambiente muito agradável. Com certeza voltarei.', '2025-10-15 20:30:00', 10, 1),
('João Santos', 3, 9, 7, 'A moqueca estava espetacular, uma das melhores que já comi. Pena que o atendimento foi péssimo, o garçom demorou uma eternidade para nos atender.', '2025-10-15 21:15:00', 6, 1),
('Maria Oliveira', 8, 8, 4, 'Gostei da comida e o atendente foi simpático, mas a música estava alta demais. Impossível conversar.', '2025-10-15 19:45:00', 7, 1),
('Pedro Souza', 2, 3, 5, 'Experiência terrível. Comida fria, serviço lento e o lugar estava sujo. Não recomendo a ninguém.', '2025-10-15 13:00:00', 1, 1),
('Juliana Costa', 9, 10, 8, 'A picanha no ponto perfeito! Suculenta e saborosa. O atendente que nos serviu também foi muito atencioso.', '2025-10-15 12:45:00', 10, 1),
('Lucas Pereira', 7, 7, 7, 'É um bom restaurante, nada de excepcional mas também sem grandes problemas. Um lugar ok para almoçar.', '2025-10-15 14:10:00', 7, 1),
('Camila Ferreira', 10, 8, 9, 'Fomos muito bem recebidos. O serviço é o ponto alto da casa. A comida é boa, mas esperava um pouco mais pelo preço.', '2025-10-15 22:05:00', 9, 1),
('Gustavo Rodrigues', 5, 4, 6, 'Pedi uma massa que veio com pouco molho e meio sem graça. O atendimento também foi bem mediano.', '2025-10-15 20:50:00', 4, 1),
('Fernanda Almeida', 9, 9, 10, 'O ambiente é lindo, super bem decorado e aconchegante. Comida e serviço acompanham a qualidade. Ótima noite!', '2025-10-15 21:30:00', 10, 1),
('Rafael Lima', 8, 9, 7, 'O prato executivo do almoço tem um ótimo custo-benefício. Comida saborosa e serviço rápido.', '2025-10-15 12:30:00', 9, 1),
('Beatriz Gomes', 4, 8, 8, 'A comida estava boa, mas esperamos mais de uma hora pelo nosso pedido. A demora comprometeu a experiência.', '2025-10-15 13:40:00', 5, 1),
('Thiago Martins', 9, 7, 6, 'O garçom foi extremamente prestativo e nos ajudou com o cardápio. O ar condicionado, porém, estava muito frio.', '2025-10-15 19:55:00', 8, 1),
('Larissa Barbosa', 1, 2, 3, 'Pior impossível. Cabelo na comida e o banheiro estava imundo. Nunca mais volto aqui.', '2025-10-15 20:10:00', 0, 1),
('Matheus Fernandes', 10, 10, 10, 'Simplesmente perfeito. Do início ao fim. Melhor restaurante da cidade!', '2025-10-15 21:00:00', 10, 1),
('Gabriela Ribeiro', 6, 9, 8, 'A sobremesa (petit gateau) estava divina, mas o prato principal demorou muito a chegar.', '2025-10-15 15:00:00', 7, 1),
('Felipe Castro', 7, 6, 9, 'O lugar é muito bonito, ótimo para um encontro. A comida poderia ser um pouco mais caprichada.', '2025-10-15 22:30:00', 8, 1),
('Vitória Gonçalves', 8, 7, 5, 'Serviço ok, comida ok. O que me incomodou foi o barulho excessivo, mal conseguia ouvir a pessoa na minha frente.', '2025-10-15 20:00:00', 6, 1),
('Bruno Carvalho', 3, 5, 7, 'Fiz o pedido pelo delivery e veio tudo errado. Tentei ligar para reclamar e ninguém atendeu.', '2025-10-15 19:00:00', 2, 1),
('Letícia Andrade', 9, 9, 8, 'Adorei as opções vegetarianas do cardápio. Tudo muito bem feito e saboroso. O atendimento foi ótimo.', '2025-10-15 12:50:00', 9, 1),
('Daniel Mendes', 7, 8, 8, 'Preço um pouco salgado, mas a qualidade da comida compensa. Bom lugar para uma ocasião especial.', '2025-10-15 21:40:00', 8, 1),
('Sofia Moreira', 10, 9, 9, 'O atendente foi de uma gentileza ímpar. Fez toda a diferença na nossa noite. Comida muito boa também.', '2025-10-15 20:25:00', 10, 1),
('Eduardo Nogueira', 5, 5, 5, 'Não entendi o hype. Achei tudo muito mediano, do atendimento à comida. Não voltaria.', '2025-10-15 14:30:00', 5, 1);