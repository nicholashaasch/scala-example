create table member_email (
      id serial not null primary key,
      member_id int not null,
      address varchar(256) not null
    )