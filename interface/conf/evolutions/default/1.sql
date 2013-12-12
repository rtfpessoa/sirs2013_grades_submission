# --- !Ups

CREATE TABLE public."User"
(
  "id"       SERIAL PRIMARY KEY,
  "name"     TEXT               NOT NULL,
  "username" TEXT               NOT NULL,
  "level"    TEXT               NOT NULL,
  UNIQUE ("username")
);

INSERT INTO public."User" ("name", "username", "level") VALUES ('Student 1', 'ist170001', 'Student');
INSERT INTO public."User" ("name", "username", "level") VALUES ('Student 2', 'ist170002', 'Student');
INSERT INTO public."User" ("name", "username", "level") VALUES ('Student 3', 'ist170003', 'Student');
INSERT INTO public."User" ("name", "username", "level") VALUES ('Student 4', 'ist170004', 'Student');

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

INSERT INTO public."User" ("name", "username", "level") VALUES ('Rafael', 'ist169801', 'Admin');
INSERT INTO public."UserSecrets" ("userId", "password", "privateKey", "publicKey") VALUES (5, '5f4dcc3b5aa765d61d8327deb882cf99', NULL, NULL);
INSERT INTO public."User" ("name", "username", "level") VALUES ('Rodrigo', 'ist169637', 'Teacher');
INSERT INTO public."UserSecrets" ("userId", "password", "privateKey", "publicKey") VALUES (6, '5f4dcc3b5aa765d61d8327deb882cf99', NULL, NULL);
INSERT INTO public."User" ("name", "username", "level") VALUES ('Paulo', 'ist169298', 'Teacher');
INSERT INTO public."UserSecrets" ("userId", "password", "privateKey", "publicKey") VALUES (7, '5f4dcc3b5aa765d61d8327deb882cf99', NULL, NULL);

CREATE TABLE public."Course"
(
  "id"         SERIAL PRIMARY KEY,
  "name"       TEXT               NOT NULL,
  "department" TEXT               NOT NULL
);

INSERT INTO public."Course" ("name", "department") VALUES ('Segurança Informática em Redes e Sistemas', 'DEI');
INSERT INTO public."Course" ("name", "department") VALUES ('Arquitecturas de Software', 'DEI');

CREATE TABLE public."Teaching"
(
  "id"        SERIAL PRIMARY KEY,
  "teacherId" BIGINT REFERENCES public."User",
  "classId"   BIGINT REFERENCES public."Course"
);

INSERT INTO public."Teaching" ("teacherId", "classId") VALUES (5, 1);
INSERT INTO public."Teaching" ("teacherId", "classId") VALUES (6, 2);

CREATE TABLE public."Enrollment"
(
  "id"        SERIAL PRIMARY KEY,
  "studentId" BIGINT REFERENCES public."User",
  "classId"   BIGINT REFERENCES public."Course"
);

INSERT INTO public."Enrollment" ("studentId", "classId") VALUES (1, 1);
INSERT INTO public."Enrollment" ("studentId", "classId") VALUES (2, 1);
INSERT INTO public."Enrollment" ("studentId", "classId") VALUES (3, 1);
INSERT INTO public."Enrollment" ("studentId", "classId") VALUES (4, 2);


# --- !Downs

DROP TABLE public."Enrollment" CASCADE;
DROP TABLE public."Teaching" CASCADE;
DROP TABLE public."Course" CASCADE;
DROP TABLE public."UserSecrets" CASCADE;
DROP TABLE public."User" CASCADE;
