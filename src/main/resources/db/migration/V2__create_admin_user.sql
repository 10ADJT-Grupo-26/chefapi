INSERT INTO usuarios (
    id,
    nome,
    email,
    login,
    senha,
    tipo,
    rua,
    numero,
    cidade,
    cep,
    uf,
    data_ultima_alteracao
)
VALUES (
           gen_random_uuid(),
           'Administrador do Sistema',
           'admin@chefapi.com',
           'admin',
           '$2a$12$6W.vwl8s5mGnpO8aUNKdMe1WfpB56HH0vpyvgPkH7znITTlyd3/oC', -- senha: admin123
           'ADMIN',
           'Rua do Sistema',
           '1',
           'Barbacena',
           '36200-000',
           'MG',
           NOW()
       );
