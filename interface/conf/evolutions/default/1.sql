# --- !Ups

CREATE TABLE public."User"
(
  "id"       SERIAL PRIMARY KEY NOT NULL,
  "name"     TEXT               NOT NULL,
  "username" TEXT               NOT NULL,
  "level"    TEXT               NOT NULL,
  UNIQUE ("username")
);

INSERT INTO public."User" VALUES (1, 'Student 1', 'ist170001', 'Student');
INSERT INTO public."User" VALUES (2, 'Student 2', 'ist170002', 'Student');
INSERT INTO public."User" VALUES (3, 'Student 3', 'ist170003', 'Student');
INSERT INTO public."User" VALUES (4, 'Student 4', 'ist170004', 'Student');

CREATE TABLE public."UserSecrets"
(
  "id"         SERIAL PRIMARY KEY NOT NULL,
  "userId" BIGINT REFERENCES public."User",
  UNIQUE ("userId"),
  "password"   TEXT               NOT NULL,
  "privateKey" TEXT,
  UNIQUE ("privateKey"),
  "publicKey"  TEXT,
  UNIQUE ("publicKey")
);

INSERT INTO public."User" VALUES (5, 'Rafael', 'ist169801', 'Teacher');
INSERT INTO public."UserSecrets" VALUES (1, 5, '5f4dcc3b5aa765d61d8327deb882cf99', NULL, NULL);
INSERT INTO public."User" VALUES (6, 'Rodrigo', 'ist169637', 'Teacher');
INSERT INTO public."UserSecrets" VALUES (2, 6, '5f4dcc3b5aa765d61d8327deb882cf99', NULL, NULL);
INSERT INTO public."User" VALUES (7, 'Paulo', 'ist169298', 'Teacher');
INSERT INTO public."UserSecrets" VALUES (3, 7, '5f4dcc3b5aa765d61d8327deb882cf99', NULL, NULL);

CREATE TABLE public."Course"
(
  "id"         SERIAL PRIMARY KEY NOT NULL,
  "name"       TEXT               NOT NULL,
  "department" TEXT               NOT NULL
);

INSERT INTO public."Course" VALUES (1, 'Segurança Informática em Redes e Sistemas', 'DEI');
INSERT INTO public."Course" VALUES (2, 'Arquitecturas de Software', 'DEI');

CREATE TABLE public."Teaching"
(
  "id"        SERIAL PRIMARY KEY NOT NULL,
  "teacherId" BIGINT REFERENCES public."User",
  "classId"   BIGINT REFERENCES public."Course"
);

INSERT INTO public."Teaching" VALUES (1, 5, 1);

CREATE TABLE public."Enrollment"
(
  "id"        SERIAL PRIMARY KEY NOT NULL,
  "studentId" BIGINT REFERENCES public."User",
  "classId"   BIGINT REFERENCES public."Course"
);

INSERT INTO public."Enrollment" VALUES (1, 1, 1);
INSERT INTO public."Enrollment" VALUES (2, 2, 1);
INSERT INTO public."Enrollment" VALUES (3, 3, 1);
INSERT INTO public."Enrollment" VALUES (4, 4, 2);


# --- !Downs

DROP TABLE public."Enrollment" CASCADE;
DROP TABLE public."Teaching" CASCADE;
DROP TABLE public."Course" CASCADE;
DROP TABLE public."UserSecrets" CASCADE;
DROP TABLE public."User" CASCADE;
