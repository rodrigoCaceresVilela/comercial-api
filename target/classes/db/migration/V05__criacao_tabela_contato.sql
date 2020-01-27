CREATE TABLE contato (
  	codigo SERIAL PRIMARY KEY,
	pessoa_id BIGINT NOT NULL,
	nome VARCHAR(50) NOT NULL,
	email VARCHAR(100) NOT NULL,
	telefone VARCHAR(20) NOT NULL,	
  	FOREIGN KEY (pessoa_id) REFERENCES pessoa(id)
);

insert into contato (codigo, pessoa_id, nome, email, telefone) values (1, 1, 'Rodrigo Cáceres Vilela', 'rodrigocaceresvilela@gmail.com', '00 0000-0000');