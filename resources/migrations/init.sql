
-- -- -- -- -- -- -- -- -- -- -- -- --

create or replace function now_ms() returns bigint 
as $$ 
begin
  return extract(epoch from now()) * 1000;
end 
$$ language plpgsql;

-- -- -- -- -- -- -- -- -- -- -- -- --

create table user_base (
  user_id   varchar(80) primary key,
  ct        bigint not null default now_ms(),
  active    boolean default 't',
  atime     bigint not null default 0,    -- last accessed
  jwt_nbf   bigint not null default 0,    -- invalidate old tokens (not before)
  profile   jsonb,   -- {first_name, last_name, username, avatar, ...}
  roles     jsonb    -- ["ADMIN", ...]
);

create table user_auth (
  auth_type varchar(40) not null,   -- "login", "tg", "vb", "vk", ... ?"phone"
  auth_id   varchar(80) not null,   -- msgr_user_id, login, email, phone, ....
  user_id   varchar(80) not null references user_base(user_id),
  passhash  varchar(200),
  ts        bigint not null default now_ms(),
  --
  primary key (auth_id, auth_type)
);

create index user_auth_user_idx on user_auth (user_id);

create table user_authcode (
  authcode  varchar(40) primary key,      -- unique one time authorization code
  user_data jsonb,                        -- {uid, msgr, username, first_name, last_name, middle_name, ...}
  expire    bigint not null
);

-- cleanup: delete from user_auth_code where exp < now();

-- -- -- -- -- -- -- -- -- -- -- -- --

create table track_info (
  track_id  bigserial primary key,
  user_id   varchar(80) not null references user_base(user_id)

  -- num_points

  -- orig_file    -- S3 url
  -- orig_size
  -- orig_hash
);
-- track: id, user, start time, distance, elevation, descr


-- track_tag, group, alias

-- track_point: id, track_id, seq, lat, lon, elev, point (geography), 

