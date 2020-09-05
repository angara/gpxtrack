
create table "user" (
  id      varchar(80) primary key,
  ct      timestamptz not null default now(),
  active  boolean default 't',
  profile jsonb,  -- {first_name, last_name, username, avatar, ...}
  roles   jsonb   -- ["ADMIN", ...]
);

create table user_auth (
  auth_type varchar(40) not null,   -- "un", "em", "tg". ...
  auth_id   varchar(80) not null,
  user_id   varchar(80) not null,
  passhash  varchar(200),
  ct        timestamptz not null default now(),
  active    boolean default 't',
  --
  primary key (auth_type, auth_id),
  foreign key (user_id) references "user"(id)
);
