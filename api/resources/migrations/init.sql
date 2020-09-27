
-- -- -- -- -- -- -- -- -- -- -- -- --

create or replace function now_ms() returns bigint as $$
begin
  return extract(epoch from now()) * 1000;
end 
$$ language plpgsql;

-- -- -- -- -- -- -- -- -- -- -- -- --

create table user_base (
  id      varchar(80) primary key,
  ct      bigint not null default now_ms(),
  active  boolean default 't',
  profile jsonb,  -- {first_name, last_name, username, avatar, ...}
  roles   jsonb   -- ["ADMIN", ...]
);

create table user_auth (
  id        varchar(80) not null,
  type      varchar(40) not null,   -- "un", "em", "tg". ...
  user_id   varchar(80) not null,
  passhash  varchar(200),
  ct        bigint not null default now_ms(),
  active    boolean default 't',
  --
  primary key (type, id),
  foreign key (user_id) references user_base(id)
);

create index user_auth_user_idx on user_auth (user_id);

create table user_auth_code (
  id        varchar(40) primary key,      -- unique one time authorization code
  ct        bigint not null default now_ms(),
  exp       bigint not null,         -- valid until
  user_id   varchar(80) not null references user_base(id),
  auth_type varchar(40) -- optional        
);

-- cleanup: delete from user_auth_code where exp < now();

-- -- -- -- -- -- -- -- -- -- -- -- --

