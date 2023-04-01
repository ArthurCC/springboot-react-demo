-- in the db flyway keeps a history of the executed migration files checksum through "flyway_schema_history" table
-- therefore, if we modify this file an error is going to be thrown unless we recreate the db
-- if we want to change the schema we should do it in a new file

CREATE TABLE IF NOT EXISTS students (
    id UUID PRIMARY KEY,
    first_name VARCHAR(100) NOT NULL,
    last_name VARCHAR(100) NOT NULL,
    email VARCHAR(100) NOT NULL UNIQUE,
    gender VARCHAR(6) NOT NULL CHECK (gender = 'MALE' OR gender = 'FEMALE')
);