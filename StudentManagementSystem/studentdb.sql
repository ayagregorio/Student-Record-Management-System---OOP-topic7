CREATE DATABASE studentdb;

CREATE TABLE students (
    id         SERIAL PRIMARY KEY,
    name       VARCHAR(100),
    course     VARCHAR(50),
    year_level VARCHAR(20)
);

INSERT INTO students (name, course, year_level) VALUES 
('Aya Gregorio', 'BSIT', 'FIRST_YEAR'),
('Yana Mendoza', 'BSIT', 'SECOND_YEAR'),
('Alex Parojinog', 'BSIT', 'THIRD_YEAR');
