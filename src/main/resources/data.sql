DROP TABLE IF EXISTS comment cascade;
DROP TABLE IF EXISTS topic cascade;
DROP TABLE IF EXISTS user cascade;

CREATE TABLE user (
  id INT AUTO_INCREMENT  PRIMARY KEY,
  name VARCHAR(250) NOT NULL,
  password VARCHAR(250) NOT NULL
);

CREATE TABLE topic (
  id INT AUTO_INCREMENT  PRIMARY KEY,
  name VARCHAR(250) NOT NULL,
  body VARCHAR(250) NOT NULL,
  photo BLOB(300K),
  created_date TIMESTAMP NOT NULL,
  modified_date TIMESTAMP,
  user_id INT NOT NULL,
  FOREIGN KEY (user_id) REFERENCES user(id)
);

CREATE TABLE comment (
  id INT AUTO_INCREMENT  PRIMARY KEY,
  topic_id INT NOT NULL,
  body VARCHAR(250) NOT NULL,
  created_date TIMESTAMP NOT NULL,
  modified_date TIMESTAMP,
  user_id INT NOT NULL,
  FOREIGN KEY (user_id) REFERENCES user(id),
  FOREIGN KEY (topic_id) REFERENCES topic(id)
);



INSERT INTO user(name, password)
VALUES('admin', 'password'),('user', 'password');

INSERT INTO topic(name, body, created_date, modified_date, user_id)
VALUES('Administration', 'Administrative post', CURRENT_TIMESTAMP(), CURRENT_TIMESTAMP(), SELECT TOP 1 id from user);

INSERT INTO comment(body, created_date, modified_date, topic_id, user_id)
VALUES('Posts for deletion', CURRENT_TIMESTAMP(), CURRENT_TIMESTAMP(), (SELECT TOP 1 id from topic), SELECT TOP 1 id from user);
