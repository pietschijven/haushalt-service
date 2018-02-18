create table transactions (
  id varchar(255) not null PRIMARY KEY,
  amount number not null,
  description VARCHAR(255),
  creation_date TIMESTAMP not null,
  username varchar(20) not null
);