CREATE TABLE IF NOT EXISTS contato
(
    id       BIGSERIAL PRIMARY KEY,
    nome     VARCHAR(255) NOT NULL,
    celular  VARCHAR(20) NOT NULL UNIQUE,
    favorito BOOLEAN DEFAULT FALSE,
    grupo_id BIGINT,
    CONSTRAINT fk_contato_grupo FOREIGN KEY (grupo_id) REFERENCES grupo (id)
);
