-- SCHEMA
CREATE TABLE users (
  id              serial              PRIMARY KEY,
  username        varchar(32)         UNIQUE,
  name            varchar(32),
  surname         varchar(32),
  password        varchar(255)        NOT NULL,           -- BCryptPasswordEncoder
  valid           boolean             DEFAULT true NOT NULL
);



-- DML
select * from users;
