# --- !Ups

CREATE TABLE public."User"
(
  "id"       SERIAL PRIMARY KEY,
  "name"     TEXT               NOT NULL,
  "username" TEXT               NOT NULL,
  "level"    TEXT               NOT NULL,
  UNIQUE ("username")
);

CREATE TABLE public."UserSecrets"
(
  "id"         SERIAL PRIMARY KEY,
  "userId" BIGINT REFERENCES public."User",
  UNIQUE ("userId"),
  "password"   TEXT               NOT NULL,
  "privateKey" TEXT,
  UNIQUE ("privateKey"),
  "publicKey"  TEXT,
  UNIQUE ("publicKey")
);

CREATE TABLE public."Course"
(
  "id"         SERIAL PRIMARY KEY,
  "abbrev"     TEXT               NOT NULL,
  "name"       TEXT               NOT NULL,
  "department" TEXT               NOT NULL,
  UNIQUE ("abbrev")
);

CREATE TABLE public."Teaching"
(
  "id"        SERIAL PRIMARY KEY,
  "teacherId" BIGINT REFERENCES public."User",
  "classId"   BIGINT REFERENCES public."Course"
);

CREATE TABLE public."Enrollment"
(
  "id"        SERIAL PRIMARY KEY,
  "studentId" BIGINT REFERENCES public."User",
  "classId"   BIGINT REFERENCES public."Course"
);

# --- !Downs

DROP TABLE public."Enrollment" CASCADE;
DROP TABLE public."Teaching" CASCADE;
DROP TABLE public."Course" CASCADE;
DROP TABLE public."UserSecrets" CASCADE;
DROP TABLE public."User" CASCADE;
