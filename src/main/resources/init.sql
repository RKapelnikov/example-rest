CREATE SCHEMA service;

CREATE TABLE service.account (
  login VARCHAR(32),
  password VARCHAR(32),
  balance DECIMAL(12, 2),
  PRIMARY KEY (login)
);

DROP ROLE IF EXISTS service_test;
CREATE ROLE service_test LOGIN PASSWORD '1';
GRANT CONNECT ON DATABASE postgres TO service_test;
GRANT USAGE ON SCHEMA service TO service_test;
GRANT SELECT, INSERT, UPDATE, DELETE ON ALL TABLES IN SCHEMA service TO service_test;
