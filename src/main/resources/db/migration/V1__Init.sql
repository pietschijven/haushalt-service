create table users (
  username varchar(20) PRIMARY KEY
);

create table transactions (
  id number not null PRIMARY KEY,
  amount number not null,
  description VARCHAR(255),
  creation_date TIMESTAMP not null,
  username varchar(20) not null
);

alter table transactions add FOREIGN KEY (username) REFERENCES users(username);

insert into users (username) VALUES ('piet');
insert into users (username) values ('lucia');
