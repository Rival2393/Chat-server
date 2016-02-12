create table users (
login VARCHAR2(25),
ip VARCHAR2(30) UNIQUE,
status VARCHAR2(20));