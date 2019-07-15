CREATE TABLE pessoa (
	id serial PRIMARY KEY,
	nome VARCHAR(50) NOT NULL,
	ativo BOOLEAN NOT NULL,
	logradouro VARCHAR(50),
    numero VARCHAR(50),
    complemento VARCHAR(50),
    bairro VARCHAR(50),
    cep VARCHAR(50),
    cidade VARCHAR(50),
    estado VARCHAR(50)
);

INSERT INTO pessoa (nome, ativo, logradouro, numero, complemento, bairro, cep, cidade, estado) 
VALUES 
('Rodrigo Cáceres Vilela', 'true', 'Rua Antônio Tavares', '637', 'Apto. 43', 'Cambuci', '01542-010', 'São Paulo', 'São Paulo');