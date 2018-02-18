create table transactions (
  id varchar(255) not null PRIMARY KEY,
  amount decimal(20,2) not null,
  description VARCHAR(255),
  creation_date TIMESTAMP not null,
  username varchar(20) not null
);