CREATE database sz_better;

CREATE TABLE usuario_info(

id INT NOT NULL auto_increment primary key,
nome VARCHAR(40),
idade INT,
dataCadastro DATE
)

CREATE TABLE mensagem (
  id INT NOT NULL AUTO_INCREMENT PRIMARY KEY,
  texto VARCHAR(255),
  link VARCHAR(255)
);

CREATE TABLE usuario_dias (
  id INT NOT NULL AUTO_INCREMENT PRIMARY KEY,
  usuario_id INT NOT NULL,
  data DATE,
  hora_dormir TIME,
  hora_acordar TIME,
  qualidade_sono VARCHAR(20),
  FOREIGN KEY (usuario_id) REFERENCES usuario_info (id)
);
