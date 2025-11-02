CREATE TABLE usuarios (
                          id UUID PRIMARY KEY,
                          nome VARCHAR(255) NOT NULL,
                          email VARCHAR(255) UNIQUE NOT NULL,
                          login VARCHAR(100) UNIQUE NOT NULL,
                          senha VARCHAR(255) NOT NULL,
                          tipo VARCHAR(50) NOT NULL,
                          rua VARCHAR(255),
                          numero VARCHAR(50),
                          cidade VARCHAR(100),
                          cep VARCHAR(20),
                          uf VARCHAR(10),
                          data_ultima_alteracao TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);
