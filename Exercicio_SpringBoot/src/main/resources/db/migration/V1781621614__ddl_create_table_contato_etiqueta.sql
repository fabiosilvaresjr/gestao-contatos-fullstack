CREATE TABLE IF NOT EXISTS contato_etiqueta
(
    contato_id  BIGINT NOT NULL,
    etiqueta_id BIGINT NOT NULL,
    PRIMARY KEY (contato_id, etiqueta_id),
    CONSTRAINT fk_contato_etiqueta_contato FOREIGN KEY (contato_id) REFERENCES contato (id),
    CONSTRAINT fk_contato_etiqueta_etiqueta FOREIGN KEY (etiqueta_id) REFERENCES etiqueta (id)
);