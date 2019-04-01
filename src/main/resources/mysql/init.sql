
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

CREATE TABLE IF NOT EXISTS yiyao_item(
  ID INT UNSIGNED NOT NULL PRIMARY KEY AUTO_INCREMENT,
  service VARCHAR(255) DEFAULT NULL,
  item VARCHAR(255) DEFAULT NULL
) DEFAULT CHARSET=utf8;

CREATE TABLE IF NOT EXISTS paper (
  identifier varchar(255) character set utf8 NOT NULL default '',
  datestamp varchar(500) character set utf8 default NULL,
  setSpec varchar(500) character set utf8 default NULL,
  title text character set utf8,
  creator text character set utf8,
  subjects text character set utf8,
  description text character set utf8,
  dates varchar(500) character set utf8 default NULL,
  types varchar(500) character set utf8 default NULL,
  gooaidentifier varchar(500) character set utf8 default NULL,
  language varchar(500) character set utf8 default NULL,
  publisher varchar(500) character set utf8 default NULL,
  keywords text character set utf8,
  relation varchar(500) character set utf8 default NULL,
  source varchar(500) character set utf8 default NULL,
  doiidentifier varchar(500) character set utf8 default NULL,
  PRIMARY KEY  (identifier)
) ENGINE=InnoDB DEFAULT CHARSET=utf8




