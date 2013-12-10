# --- !Ups

CREATE TABLE public."Teacher"
(
  "id"         SERIAL PRIMARY KEY NOT NULL,
  "name"       TEXT               NOT NULL,
  "username"   TEXT               NOT NULL,
  UNIQUE ("username"),
  "password"   TEXT               NOT NULL,
  "privateKey" TEXT               NOT NULL,
  UNIQUE ("privateKey"),
  "publicKey"  TEXT               NOT NULL,
  UNIQUE ("publicKey")
);

ALTER TABLE public."Teacher"
ADD CONSTRAINT unique_username UNIQUE (username);
ALTER TABLE public."Teacher"
ADD CONSTRAINT "unique_privateKey" UNIQUE ("privateKey");
ALTER TABLE public."Teacher"
ADD CONSTRAINT "unique_publicKey" UNIQUE ("publicKey");

INSERT INTO public."Teacher" VALUES (1, 'Rafael', 'ist169801', '5f4dcc3b5aa765d61d8327deb882cf99', '1fc26f265b15a6c0aeaa9f0bf5654c6b', '3bead7e3939d0311277c2f01cd8a52f9');
INSERT INTO public."Teacher" VALUES (2, 'Rodrigo', 'ist169637', '5f4dcc3b5aa765d61d8327deb882cf99', 'bcb6a39243c178b939d0e54483bd357e', '2ff1b08fa0001a1df3f9a64ebfceb4ac');
INSERT INTO public."Teacher" VALUES (3, 'Paulo', 'ist169298', '5f4dcc3b5aa765d61d8327deb882cf99', '341ff81c0dab0f16c73e7db8e2a686e7', '95e6dd98e2c23ee1762d4e0700b5b27e');

CREATE TABLE public."Student"
(
  "id"       SERIAL PRIMARY KEY NOT NULL,
  "name"     TEXT               NOT NULL,
  "username" TEXT               NOT NULL
);

INSERT INTO public."Student" VALUES (1, 'Student 1', 'ist170001');
INSERT INTO public."Student" VALUES (2, 'Student 2', 'ist170002');
INSERT INTO public."Student" VALUES (3, 'Student 3', 'ist170003');
INSERT INTO public."Student" VALUES (4, 'Student 4', 'ist170004');

CREATE TABLE public."Class"
(
  "id"         SERIAL PRIMARY KEY NOT NULL,
  "name"       TEXT               NOT NULL,
  "department" TEXT               NOT NULL
);

INSERT INTO public."Class" VALUES (1, 'Segurança Informática em Redes e Sistemas', 'DEI');
INSERT INTO public."Class" VALUES (2, 'Arquitecturas de Software', 'DEI');

CREATE TABLE public."Teaching"
(
  "id"        SERIAL PRIMARY KEY NOT NULL,
  "teacherId" BIGINT REFERENCES public."Teacher",
  "classId"   BIGINT REFERENCES public."Class"
);

INSERT INTO public."Teaching" VALUES (1, 3, 1);

CREATE TABLE public."Enrollment"
(
  "id"        SERIAL PRIMARY KEY NOT NULL,
  "studentId" BIGINT REFERENCES public."Student",
  "classId"   BIGINT REFERENCES public."Class"
);

INSERT INTO public."Enrollment" VALUES (1, 1, 1);
INSERT INTO public."Enrollment" VALUES (2, 2, 1);
INSERT INTO public."Enrollment" VALUES (3, 3, 1);
INSERT INTO public."Enrollment" VALUES (4, 4, 2);

# --- !Downs

DROP TABLE public."Enrollment" CASCADE;
DROP TABLE public."Teaching" CASCADE;
DROP TABLE public."Class" CASCADE;
DROP TABLE public."Student" CASCADE;
DROP TABLE public."Teacher" CASCADE;
