CREATE TABLE categoria (
	id serial PRIMARY KEY,
	nome varchar(50) NOT NULL
);

INSERT INTO categoria (nome) VALUES ('Lazer');
INSERT INTO categoria (nome) VALUES ('Alimentação');
INSERT INTO categoria (nome) VALUES ('Treinamentos');