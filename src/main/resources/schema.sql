CREATE TABLE user (
  id BIGINT AUTO_INCREMENT NOT NULL,
   username VARCHAR(255),
   password VARCHAR(255),
   firstname VARCHAR(255),
   lastname VARCHAR(255),
   email VARCHAR(255),
   CONSTRAINT pk_user PRIMARY KEY (id)
);