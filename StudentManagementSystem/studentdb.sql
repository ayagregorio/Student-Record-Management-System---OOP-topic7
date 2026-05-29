CREATE DATABASE studentdb;

CREATE TABLE students (
    id         SERIAL PRIMARY KEY,
    name       VARCHAR(100),
    course     VARCHAR(50),
    year_level VARCHAR(20)
);

INSERT INTO students (name, course, year_level) VALUES
('Aya Gregorio', 'BSIT', '1st Year'),
('Yana Mendoza', 'BSIT', '2nd Year'),
('Alex Parojinog', 'BSIT', '3rd Year');
