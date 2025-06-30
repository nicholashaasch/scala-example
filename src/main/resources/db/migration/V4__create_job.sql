create table  IF NOT EXISTS job (
   id serial not null primary key,
   company varchar(128),
   description TEXT
)