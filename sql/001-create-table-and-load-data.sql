DROP TABLE IF EXISTS trainers;

CREATE TABLE trainers (
  id int unsigned AUTO_INCREMENT,
  name VARCHAR(255) NOT NULL,
  email VARCHAR(255) NOT NULL UNIQUE,
  PRIMARY KEY(id)
);

INSERT INTO trainers (name, email) VALUES ("ゼイユ", "Zeiyu498@merry.bluebe");
INSERT INTO trainers (name, email) VALUES ("サザレ", "Sazare318@heisei.bluebe");
INSERT INTO trainers (name, email) VALUES ("ブライア", "Briar8931@usagica.bluebe");
