--liquibase formatted sql

--changeset gabriela_tenorio:insert-example-feedbacks

-- FEEDBACK 1: Cliente Satisfeito
WITH new_feedback AS (
    INSERT INTO feedback (
        customer_name, attendant_name, service_rating, food_rating, environment_rating, recommendation_rating,
        service_comment, food_comment, environment_comment, general_comment, "timestamp"
    ) VALUES (
        'Roberto Lima', 'Ana Paula', 10, 9, 8, 10,
        'Atendimento excelente, a Ana foi muito atenciosa!',
        'A Moqueca estava divina, muito saborosa.',
        'O ambiente é agradável, mas a música estava um pouco alta.',
        'Com certeza voltarei e recomendarei aos meus amigos.',
        NOW() - INTERVAL '2 hours'
    ) RETURNING id
)
INSERT INTO dish_feedback (feedback_id, dish_id, rating, comment) VALUES
    ((SELECT id FROM new_feedback), 2, 10, 'Melhor moqueca da cidade!'),
    ((SELECT id FROM new_feedback), 10, 8, 'O brigadeiro estava bom, mas poderia ser maior.');

-- FEEDBACK 2: Experiência Mista
WITH new_feedback AS (
    INSERT INTO feedback (
        customer_name, attendant_name, service_rating, food_rating, environment_rating, recommendation_rating,
        service_comment, food_comment, general_comment, "timestamp"
    ) VALUES (
        'Fernanda Costa', 'Carlos', 4, 10, 7, 6,
        'O garçom demorou muito para trazer a conta e parecia disperso.',
        'A picanha estava no ponto perfeito, suculenta e deliciosa.',
        'No geral, a comida salvou a noite. O serviço precisa melhorar.',
        NOW() - INTERVAL '6 hours'
    ) RETURNING id
)
INSERT INTO dish_feedback (feedback_id, dish_id, rating, comment) VALUES
    ((SELECT id FROM new_feedback), 6, 10, 'Picanha impecável, parabéns ao chef.');

-- FEEDBACK 3: Cliente Insatisfeito
WITH new_feedback AS (
    INSERT INTO feedback (
        customer_name, attendant_name, service_rating, food_rating, environment_rating, recommendation_rating,
        service_comment, food_comment, general_comment, "timestamp"
    ) VALUES (
        'Thiago Alves', 'Mariana', 2, 3, 5, 1,
        'Fui ignorada pelo atendente várias vezes, tive que levantar para pedir a bebida.',
        'A lasanha veio fria por dentro e o queijo nem estava derretido.',
        'Experiência muito ruim, não pretendo voltar e não recomendo.',
        NOW() - INTERVAL '23 hours'
    ) RETURNING id
)
INSERT INTO dish_feedback (feedback_id, dish_id, rating, comment) VALUES
    ((SELECT id FROM new_feedback), 4, 2, 'Fria e com pouco recheio.');

-- FEEDBACK 4: Cliente Neutro
INSERT INTO feedback (
    customer_name, attendant_name, service_rating, food_rating, environment_rating, recommendation_rating,
    general_comment, "timestamp"
) VALUES (
    'Lúcia Pereira', 'Jorge', 7, 7, 8, 7,
    'Foi uma experiência ok, nada de especial mas sem grandes problemas.',
    NOW() - INTERVAL '15 minutes'
);

-- Verifica se os dados foram inseridos corretamente
SELECT
    f.id as feedback_id,
    f.customer_name,
    f.attendant_name,
    f.recommendation_rating,
    d.name as dish_name,
    df.rating as dish_rating
FROM feedback f
LEFT JOIN dish_feedback df ON f.id = df.feedback_id
LEFT JOIN dish d ON df.dish_id = d.id
ORDER BY f."timestamp" DESC;