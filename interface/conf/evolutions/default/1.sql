# --- !Ups

CREATE TABLE public."Teacher"
(
  id           SERIAL PRIMARY KEY NOT NULL,
  name         TEXT               NOT NULL,
  username     TEXT               NOT NULL,
  password     TEXT               NOT NULL,
  "privateKey" TEXT               NOT NULL,
  "publicKey"  TEXT               NOT NULL
);

ALTER TABLE public."Teacher"
ADD CONSTRAINT unique_username UNIQUE (username);
ALTER TABLE public."Teacher"
ADD CONSTRAINT "unique_privateKey" UNIQUE ("privateKey");
ALTER TABLE public."Teacher"
ADD CONSTRAINT "unique_publicKey" UNIQUE ("publicKey");

INSERT INTO public."Teacher" VALUES (1, 'Rafael', 'ist169801', '5f4dcc3b5aa765d61d8327deb882cf99', '', '');
INSERT INTO public."Teacher" VALUES (2, 'Rodrigo', 'ist169637', '5f4dcc3b5aa765d61d8327deb882cf99', '', '');
INSERT INTO public."Teacher" VALUES (3, 'Paulo', 'ist169298', '5f4dcc3b5aa765d61d8327deb882cf99', '', '');

# --- !Downs

DROP TABLE public."Teacher";
