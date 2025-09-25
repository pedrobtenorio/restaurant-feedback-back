--liquibase formatted sql

--changeset gabriela_tenorio:insert-example-feedbacks

-- FEEDBACK 1: Cliente Promotor (Notas CSAT altas, NPS alto)
WITH new_feedback AS (
    INSERT INTO feedback (
        customer_name, attendant_name, service_rating, food_rating, environment_rating, recommendation_rating,
        service_comment, food_comment, environment_comment, general_comment, "timestamp"
    ) VALUES (
        'Roberto Lima', 'Ana Paula', 5, 5, 4, 10, -- CSATs (5,5,4) e NPS (10)
        'Atendimento excelente, a Ana foi muito atenciosa!',
        'A Moqueca estava divina, muito saborosa.',
        'O ambiente é agradável, mas a música estava um pouco alta.',
        'Com certeza voltarei e recomendarei aos meus amigos.',
        NOW() - INTERVAL '2 hours'
    ) RETURNING id
)
INSERT INTO dish_feedback (feedback_id, dish_id, rating, comment) VALUES
    ((SELECT id FROM new_feedback), 2, 5, 'Melhor moqueca da cidade!'), -- Nota do prato também em escala 1-5
    ((SELECT id FROM new_feedback), 10, 4, 'O brigadeiro estava bom, mas poderia ser maior.');

-- FEEDBACK 2: Experiência Mista (CSAT de serviço baixo, comida alta. NPS de detrator)
WITH new_feedback AS (
    INSERT INTO feedback (
        customer_name, attendant_name, service_rating, food_rating, environment_rating, recommendation_rating,
        service_comment, food_comment, general_comment, "timestamp"
    ) VALUES (
        'Fernanda Costa', 'Carlos', 2, 5, 4, 6, -- CSATs (2,5,4) e NPS (6)
        'O garçom demorou muito para trazer a conta e parecia disperso.',
        'A picanha estava no ponto perfeito, suculenta e deliciosa.',
        'No geral, a comida salvou a noite. O serviço precisa melhorar.',
        NOW() - INTERVAL '6 hours'
    ) RETURNING id
)
INSERT INTO dish_feedback (feedback_id, dish_id, rating, comment) VALUES
    ((SELECT id FROM new_feedback), 6, 5, 'Picanha impecável, parabéns ao chef.');

-- FEEDBACK 3: Cliente Insatisfeito (CSATs baixos, NPS de detrator)
WITH new_feedback AS (
    INSERT INTO feedback (
        customer_name, attendant_name, service_rating, food_rating, environment_rating, recommendation_rating,
        service_comment, food_comment, general_comment, "timestamp"
    ) VALUES (
        'Thiago Alves', 'Mariana', 1, 2, 3, 1, -- CSATs (1,2,3) e NPS (1)
        'Fui ignorada pelo atendente várias vezes, tive que levantar para pedir a bebida.',
        'A lasanha veio fria por dentro e o queijo nem estava derretido.',
        'Experiência muito ruim, não pretendo voltar e não recomendo.',
        NOW() - INTERVAL '23 hours'
    ) RETURNING id
)
INSERT INTO dish_feedback (feedback_id, dish_id, rating, comment) VALUES
    ((SELECT id FROM new_feedback), 4, 1, 'Fria e com pouco recheio.');

-- FEEDBACK 4: Cliente Neutro (CSATs ok, NPS de neutro)
INSERT INTO feedback (
    customer_name, attendant_name, service_rating, food_rating, environment_rating, recommendation_rating,
    general_comment, "timestamp"
) VALUES (
    'Lúcia Pereira', 'Jorge', 4, 4, 4, 7, -- CSATs (4,4,4) e NPS (7)
    'Foi uma experiência ok, nada de especial mas sem grandes problemas.',
    NOW() - INTERVAL '15 minutes'
);

-- Verifica se os dados foram inseridos corretamente (sem alterações aqui)
SELECT
    f.id as feedback_id,
    f.customer_name,
    f.attendant_name,
    f.service_rating,
    f.food_rating,
    f.recommendation_rating,
    d.name as dish_name,
    df.rating as dish_rating
FROM feedback f
LEFT JOIN dish_feedback df ON f.id = df.feedback_id
LEFT JOIN dish d ON df.dish_id = d.id
ORDER BY f."timestamp" DESC;
