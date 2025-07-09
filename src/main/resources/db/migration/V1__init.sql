CREATE TABLE users
(
    id       INT GENERATED ALWAYS AS IDENTITY
        CONSTRAINT users_pk
            PRIMARY KEY,
    username VARCHAR(50) NOT NULL
        CONSTRAINT uname_unique_key
            UNIQUE,
    password VARCHAR(100) NOT NULL
);

CREATE TABLE list
(
    id       INT GENERATED ALWAYS AS IDENTITY
        CONSTRAINT list_pk
            PRIMARY KEY,
    description VARCHAR(255) NOT NULL,
    due DATE,
    created_on DATE NOT NULL,
    is_done BOOLEAN NOT NULL,
    completed_on DATE
)