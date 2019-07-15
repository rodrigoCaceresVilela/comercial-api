CREATE TABLE usuario (
	id SERIAL PRIMARY KEY,
	nome VARCHAR NOT NULL,
	email VARCHAR NOT NULL,
	senha VARCHAR NOT NULL
);

CREATE TABLE permissao (
	id SERIAL PRIMARY KEY,
	descricao VARCHAR NOT NULL
);

CREATE TABLE permissoes_usuarios (
	usuario_id INTEGER NOT NULL,
	permissao_id INTEGER NOT NULL,
	PRIMARY KEY (usuario_id, permissao_id),
	FOREIGN KEY (usuario_id) REFERENCES usuario (id),
	FOREIGN KEY (permissao_id) REFERENCES permissao (id)
);

INSERT INTO usuario (nome, email, senha) 
VALUES
('Rodrigo', 'rodrigocaceresvilela@gmail.com', '$2a$10$Ln.30cvYRb9v9UWSx0v0.uYJ7QvutaTM4NBrHUkFyoDt4TAi.lItW');

INSERT INTO permissao (descricao) VALUES ('ROLE_CADASTRAR_CATEGORIA'); 
INSERT INTO permissao (descricao) VALUES ('ROLE_PESQUISAR_CATEGORIA'); 
INSERT INTO permissao (descricao) VALUES ('ROLE_REMOVER_CATEGORIA');
INSERT INTO permissao (descricao) VALUES ('ROLE_CADASTRAR_PESSOA');
INSERT INTO permissao (descricao) VALUES ('ROLE_PESQUISAR_PESSOA');
INSERT INTO permissao (descricao) VALUES ('ROLE_REMOVER_PESSOA');
INSERT INTO permissao (descricao) VALUES ('ROLE_CADASTRAR_LANCAMENTO');
INSERT INTO permissao (descricao) VALUES ('ROLE_PESQUISAR_LANCAMENTO');
INSERT INTO permissao (descricao) VALUES ('ROLE_REMOVER_LANCAMENTO');

INSERT INTO permissoes_usuarios (usuario_id, permissao_id) 
VALUES
(1, 1);