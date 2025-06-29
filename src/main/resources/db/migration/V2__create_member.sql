create table  IF NOT EXISTS member (
                        id serial not null primary key,
                        name varchar(64),
                        created_at timestamp not null,
                        updated_at timestamp
)