CREATE TABLE if not exists usuario (
                         id text PRIMARY KEY NOT NULL,
                         login VARCHAR(255) NOT NULL UNIQUE,
                         password VARCHAR(255) NOT NULL,
                         role VARCHAR(50) NOT NULL
);