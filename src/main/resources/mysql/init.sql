
CREATE TABLE IF NOT EXISTS yiyao_user(
  ID INT UNSIGNED NOT NULL PRIMARY KEY AUTO_INCREMENT,
  account VARCHAR(255) DEFAULT NULL,
  password VARCHAR(255) DEFAULT NULL,
  name VARCHAR(255) DEFAULT NULL,
  identity VARCHAR(255) DEFAULT NULL,
  unit VARCHAR(255) DEFAULT NULL,
  job VARCHAR(255) DEFAULT NULL,
  duty VARCHAR(255) DEFAULT NULL,
  major VARCHAR(255) DEFAULT NULL,
  email VARCHAR(255) DEFAULT NULL,
  phone VARCHAR(255) DEFAULT NULL,
  wechat VARCHAR(255) DEFAULT NULL,
  role VARCHAR(255) DEFAULT NULL,
  stamp VARCHAR(255) DEFAULT NULL
) DEFAULT CHARSET=utf8;




