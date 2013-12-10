# --- !Ups

CREATE TABLE public."Teacher"
(
  "id"           SERIAL PRIMARY KEY NOT NULL,
  "name"         TEXT               NOT NULL,
  "username"     TEXT               NOT NULL,
  UNIQUE("username"),
  "password"     TEXT               NOT NULL,
  "privateKey" TEXT               NOT NULL,
  UNIQUE("privateKey"),
  "publicKey"  TEXT               NOT NULL,
  UNIQUE("publicKey")
);

INSERT INTO public."Teacher" VALUES (1, 'Rafael', 'ist169801', '5f4dcc3b5aa765d61d8327deb882cf99', 'rafael_pvt_key', 'rafael_pub_key');
INSERT INTO public."Teacher" VALUES (2, 'Rodrigo', 'ist169637', '5f4dcc3b5aa765d61d8327deb882cf99', 'rodrigo_pvt_key', 'rodrigo_pub_key');
INSERT INTO public."Teacher" VALUES (3, 'Paulo', 'ist169298', '5f4dcc3b5aa765d61d8327deb882cf99', 'paulo_pvt_key', 'paulo_pub_key');

# --- !Downs

DROP TABLE public."Teacher";
