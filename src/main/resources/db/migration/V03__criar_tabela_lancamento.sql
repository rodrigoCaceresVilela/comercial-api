CREATE TABLE lancamento (
	id SERIAL PRIMARY KEY,
	descricao VARCHAR NOT NULL,
	data_vencimento DATE NOT NULL,
	data_pagamento DATE,
	valor NUMERIC(10,2) NOT NULL,
	observacao VARCHAR(100),
	tipo VARCHAR(20) NOT NULL,
	categoria_id INTEGER NOT NULL,
	pessoa_id INTEGER NOT NULL,
	FOREIGN KEY (categoria_id) REFERENCES categoria (id),
	FOREIGN KEY (pessoa_id) REFERENCES pessoa (id)
);

INSERT INTO lancamento (descricao, data_vencimento, data_pagamento, valor, observacao, tipo, categoria_id, pessoa_id) 
VALUES
('Mouse', CURRENT_DATE, CURRENT_DATE, 115.9, 'Corsair', '', 10, 1);