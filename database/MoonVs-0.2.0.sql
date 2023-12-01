PGDMP  3                    {            MoonVs    16.0    16.0     �           0    0    ENCODING    ENCODING        SET client_encoding = 'UTF8';
                      false            �           0    0 
   STDSTRINGS 
   STDSTRINGS     (   SET standard_conforming_strings = 'on';
                      false            �           0    0 
   SEARCHPATH 
   SEARCHPATH     8   SELECT pg_catalog.set_config('search_path', '', false);
                      false            �           1262    16396    MoonVs    DATABASE        CREATE DATABASE "MoonVs" WITH TEMPLATE = template0 ENCODING = 'UTF8' LOCALE_PROVIDER = libc LOCALE = 'Portuguese_Brazil.1252';
    DROP DATABASE "MoonVs";
                postgres    false            �           0    0    MoonVs    DATABASE PROPERTIES     .   ALTER DATABASE "MoonVs" CONNECTION LIMIT = 5;
                     postgres    false            �            1259    24577    content_catalog    TABLE     �  CREATE TABLE public.content_catalog (
    id_content integer NOT NULL,
    id_tmdb bigint NOT NULL,
    original_title character varying NOT NULL,
    pt_title character varying NOT NULL,
    is_adult boolean,
    genres character varying[],
    tmdb_vote_avg real,
    tmdb_vote_count bigint,
    watch_provider character varying[],
    content_type character(6) NOT NULL,
    overview text
);
 #   DROP TABLE public.content_catalog;
       public         heap    postgres    false            �            1259    24576    content_catalog_id_content_seq    SEQUENCE     �   CREATE SEQUENCE public.content_catalog_id_content_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;
 5   DROP SEQUENCE public.content_catalog_id_content_seq;
       public          postgres    false    217            �           0    0    content_catalog_id_content_seq    SEQUENCE OWNED BY     a   ALTER SEQUENCE public.content_catalog_id_content_seq OWNED BY public.content_catalog.id_content;
          public          postgres    false    216            �            1259    16404    users    TABLE       CREATE TABLE public.users (
    id_user uuid NOT NULL,
    username character varying(255),
    email character varying(255) NOT NULL,
    password_hash character varying(60) NOT NULL,
    birth_date date NOT NULL,
    created_at timestamp without time zone NOT NULL
);
    DROP TABLE public.users;
       public         heap    postgres    false                       2604    24580    content_catalog id_content    DEFAULT     �   ALTER TABLE ONLY public.content_catalog ALTER COLUMN id_content SET DEFAULT nextval('public.content_catalog_id_content_seq'::regclass);
 I   ALTER TABLE public.content_catalog ALTER COLUMN id_content DROP DEFAULT;
       public          postgres    false    216    217    217            &           2606    24584 .   content_catalog content_catalog_id_content_key 
   CONSTRAINT     o   ALTER TABLE ONLY public.content_catalog
    ADD CONSTRAINT content_catalog_id_content_key UNIQUE (id_content);
 X   ALTER TABLE ONLY public.content_catalog DROP CONSTRAINT content_catalog_id_content_key;
       public            postgres    false    217            (           2606    24586 +   content_catalog content_catalog_id_tmdb_key 
   CONSTRAINT     i   ALTER TABLE ONLY public.content_catalog
    ADD CONSTRAINT content_catalog_id_tmdb_key UNIQUE (id_tmdb);
 U   ALTER TABLE ONLY public.content_catalog DROP CONSTRAINT content_catalog_id_tmdb_key;
       public            postgres    false    217                        2606    16414    users users_email_key 
   CONSTRAINT     Q   ALTER TABLE ONLY public.users
    ADD CONSTRAINT users_email_key UNIQUE (email);
 ?   ALTER TABLE ONLY public.users DROP CONSTRAINT users_email_key;
       public            postgres    false    215            "           2606    16410    users users_pkey 
   CONSTRAINT     S   ALTER TABLE ONLY public.users
    ADD CONSTRAINT users_pkey PRIMARY KEY (id_user);
 :   ALTER TABLE ONLY public.users DROP CONSTRAINT users_pkey;
       public            postgres    false    215            $           2606    16412    users users_username_key 
   CONSTRAINT     W   ALTER TABLE ONLY public.users
    ADD CONSTRAINT users_username_key UNIQUE (username);
 B   ALTER TABLE ONLY public.users DROP CONSTRAINT users_username_key;
       public            postgres    false    215           