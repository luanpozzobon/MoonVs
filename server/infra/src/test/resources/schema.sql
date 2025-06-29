DROP SCHEMA IF EXISTS mvs CASCADE;

CREATE SCHEMA IF NOT EXISTS mvs;

CREATE TABLE mvs.user (
    id         uuid         NOT NULL,
    username   varchar(255) NOT NULL,
    email      varchar(255) NOT NULL,
    "password" varchar(100) NOT NULL,
    CONSTRAINT user_pk PRIMARY KEY (id),
    CONSTRAINT user_unique UNIQUE (username),
    CONSTRAINT user_unique_1 UNIQUE (email)
);


CREATE TABLE mvs.playlist (
    id          uuid        NOT NULL,
    user_id     uuid        NOT NULL,
    title       varchar(64) NOT NULL,
    description varchar(255) NULL,
    CONSTRAINT list_pk PRIMARY KEY (id),
    CONSTRAINT list_un UNIQUE (user_id, title)
);
ALTER TABLE mvs.playlist
    ADD CONSTRAINT list_fk FOREIGN KEY (user_id) REFERENCES mvs."user" (id) ON DELETE CASCADE;


CREATE TABLE mvs.playlist_item (
    playlist_id uuid NOT NULL,
    title_id    int8 NOT NULL,
    "type"      varchar(5) NULL,
    CONSTRAINT playlist_item_pk PRIMARY KEY (playlist_id, title_id)
);
ALTER TABLE mvs.playlist_item
    ADD CONSTRAINT playlist_item_fk FOREIGN KEY (playlist_id) REFERENCES mvs.playlist (id) ON DELETE CASCADE;