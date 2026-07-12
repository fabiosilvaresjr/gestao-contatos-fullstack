CREATE TABLE IF NOT EXISTS contato_etiqueta
(
    id BIGSERIAL PRIMARY KEY,
    contato_id  BIGINT NOT NULL,
    etiqueta_id BIGINT NOT NULL,
    data_adicao TIMESTAMPTZ DEFAULT now(),

    CONSTRAINT uk_contato_etiqueta UNIQUE (contato_id, etiqueta_id),
    CONSTRAINT fk_contato_etiqueta_contato FOREIGN KEY (contato_id) REFERENCES contato (id) ON DELETE CASCADE,
    CONSTRAINT fk_contato_etiqueta_etiqueta FOREIGN KEY (etiqueta_id) REFERENCES etiqueta (id) ON DELETE CASCADE
);
