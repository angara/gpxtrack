
create table user (
  id  varchar(80) primary key,
);

create table user_auth (
  user_id varchar(80) not null foreign key user(id),
)