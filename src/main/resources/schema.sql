CREATE TABLE IF NOT EXISTS students (
    id uuid PRIMARY KEY,
    first_name VARCHAR NOT NULL,
    last_name VARCHAR NOT NULL,
    email VARCHAR NOT NULL,
    gender INTEGER NOT NULL
);