CREATE DATABASE IF NOT EXISTS mediscreen_patient;
USE mediscreen_patient;

DROP TABLE IF EXISTS patient;

CREATE TABLE patient (
   id INT NOT NULL AUTO_INCREMENT,
   family_name VARCHAR(50) NULL,
   given_name VARCHAR(50) NULL,
   sex VARCHAR(1) NULL,
   date_of_birth DATE NOT NULL,
   address VARCHAR(50) NULL,
   phone VARCHAR(15) NULL,
   PRIMARY KEY (id)
);

