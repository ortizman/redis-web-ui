-- Create table ClientData
CREATE TABLE IF NOT EXISTS CLIENT_REDIS (
  ID VARCHAR(1024) PRIMARY KEY,
  CONFIG_YAML VARCHAR(163840) NOT NULL,
  NAME VARCHAR(2048),
  TYPE VARCHAR(255)
);