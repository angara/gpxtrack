# GPX Track: API

## Database

### misc

```sh

apt install postgis
;; apt install postgresql-12-postgis-3
su postgres
cratedb ...
createuser ....
psql ...
alter user ... password '...';
CREATE EXTENSION postgis;
SELECT PostGIS_version();

```

### http opts

Options:
:ip: which IP to bind, default to 0.0.0.0
:port: which port listens for incoming requests, default to 8090
:thread: How many threads to compute response from request, default to 4
:worker-name-prefix: worker thread name prefix, default to worker-: worker-1 worker-2....
:queue-size: max requests queued waiting for thread pool to compute response before rejecting, 503(Service Unavailable) is returned to client if queue is full, default to 20K
:max-body: length limit for request body in bytes, 413(Request Entity Too Large) is returned if request exceeds this limit, default to 8388608(8M)
:max-line: length limit for HTTP initial line and per header, 414(Request-URI Too Long) will be returned if exceeding this limit, default to 8192(8K), relevant discussion on Stack Overflow
