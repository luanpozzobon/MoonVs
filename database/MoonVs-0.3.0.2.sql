PGDMP  #                    {            MoonVs    16.1    16.1     �           0    0    ENCODING    ENCODING        SET client_encoding = 'UTF8';
                      false            �           0    0 
   STDSTRINGS 
   STDSTRINGS     (   SET standard_conforming_strings = 'on';
                      false            �           0    0 
   SEARCHPATH 
   SEARCHPATH     8   SELECT pg_catalog.set_config('search_path', '', false);
                      false            �           1262    16396    MoonVs    DATABASE        CREATE DATABASE "MoonVs" WITH TEMPLATE = template0 ENCODING = 'UTF8' LOCALE_PROVIDER = libc LOCALE = 'Portuguese_Brazil.1252';
    DROP DATABASE "MoonVs";
                postgres    false            �           0    0    MoonVs    DATABASE PROPERTIES     .   ALTER DATABASE "MoonVs" CONNECTION LIMIT = 5;
                     postgres    false            R           1247    24604    content_type    TYPE     C   CREATE TYPE public.content_type AS ENUM (
    'MOVIE',
    'TV'
);
    DROP TYPE public.content_type;
       public          postgres    false            �            1259    24577    content_catalog    TABLE     �  CREATE TABLE public.content_catalog (
    id_content integer NOT NULL,
    id_tmdb bigint NOT NULL,
    original_title character varying NOT NULL,
    pt_title character varying NOT NULL,
    is_adult boolean,
    genres character varying[],
    tmdb_vote_avg real,
    tmdb_vote_count bigint,
    overview text,
    watch_provider jsonb,
    content_type public.content_type,
    poster_path character varying(100)
);
 #   DROP TABLE public.content_catalog;
       public         heap    postgres    false    850            �            1259    24576    content_catalog_id_content_seq    SEQUENCE     �   CREATE SEQUENCE public.content_catalog_id_content_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;
 5   DROP SEQUENCE public.content_catalog_id_content_seq;
       public          postgres    false    217            �           0    0    content_catalog_id_content_seq    SEQUENCE OWNED BY     a   ALTER SEQUENCE public.content_catalog_id_content_seq OWNED BY public.content_catalog.id_content;
          public          postgres    false    216            �            1259    24609    profiles    TABLE     �   CREATE TABLE public.profiles (
    id_user uuid NOT NULL,
    biography character varying(255),
    privacy boolean,
    created_at timestamp without time zone,
    fav_movie integer,
    fav_series integer
);
    DROP TABLE public.profiles;
       public         heap    postgres    false            �            1259    24629    ratings    TABLE     �   CREATE TABLE public.ratings (
    id_user uuid NOT NULL,
    id_content integer NOT NULL,
    rating_value real NOT NULL,
    commentary character varying(255),
    added_at timestamp without time zone
);
    DROP TABLE public.ratings;
       public         heap    postgres    false            �            1259    16404    users    TABLE       CREATE TABLE public.users (
    id_user uuid NOT NULL,
    username character varying(255),
    email character varying(255) NOT NULL,
    password_hash character varying(60) NOT NULL,
    birth_date date NOT NULL,
    created_at timestamp without time zone NOT NULL
);
    DROP TABLE public.users;
       public         heap    postgres    false            )           2604    24580    content_catalog id_content    DEFAULT     �   ALTER TABLE ONLY public.content_catalog ALTER COLUMN id_content SET DEFAULT nextval('public.content_catalog_id_content_seq'::regclass);
 I   ALTER TABLE public.content_catalog ALTER COLUMN id_content DROP DEFAULT;
       public          postgres    false    217    216    217            1           2606    24584 .   content_catalog content_catalog_id_content_key 
   CONSTRAINT     o   ALTER TABLE ONLY public.content_catalog
    ADD CONSTRAINT content_catalog_id_content_key UNIQUE (id_content);
 X   ALTER TABLE ONLY public.content_catalog DROP CONSTRAINT content_catalog_id_content_key;
       public            postgres    false    217            3           2606    24586 +   content_catalog content_catalog_id_tmdb_key 
   CONSTRAINT     i   ALTER TABLE ONLY public.content_catalog
    ADD CONSTRAINT content_catalog_id_tmdb_key UNIQUE (id_tmdb);
 U   ALTER TABLE ONLY public.content_catalog DROP CONSTRAINT content_catalog_id_tmdb_key;
       public            postgres    false    217            5           2606    24613    profiles profiles_pkey 
   CONSTRAINT     Y   ALTER TABLE ONLY public.profiles
    ADD CONSTRAINT profiles_pkey PRIMARY KEY (id_user);
 @   ALTER TABLE ONLY public.profiles DROP CONSTRAINT profiles_pkey;
       public            postgres    false    218            7           2606    24633    ratings ratings_pkey 
   CONSTRAINT     c   ALTER TABLE ONLY public.ratings
    ADD CONSTRAINT ratings_pkey PRIMARY KEY (id_user, id_content);
 >   ALTER TABLE ONLY public.ratings DROP CONSTRAINT ratings_pkey;
       public            postgres    false    219    219            +           2606    16414    users users_email_key 
   CONSTRAINT     Q   ALTER TABLE ONLY public.users
    ADD CONSTRAINT users_email_key UNIQUE (email);
 ?   ALTER TABLE ONLY public.users DROP CONSTRAINT users_email_key;
       public            postgres    false    215            -           2606    16410    users users_pkey 
   CONSTRAINT     S   ALTER TABLE ONLY public.users
    ADD CONSTRAINT users_pkey PRIMARY KEY (id_user);
 :   ALTER TABLE ONLY public.users DROP CONSTRAINT users_pkey;
       public            postgres    false    215            /           2606    16412    users users_username_key 
   CONSTRAINT     W   ALTER TABLE ONLY public.users
    ADD CONSTRAINT users_username_key UNIQUE (username);
 B   ALTER TABLE ONLY public.users DROP CONSTRAINT users_username_key;
       public            postgres    false    215            8           2606    24619     profiles profiles_fav_movie_fkey    FK CONSTRAINT     �   ALTER TABLE ONLY public.profiles
    ADD CONSTRAINT profiles_fav_movie_fkey FOREIGN KEY (fav_movie) REFERENCES public.content_catalog(id_content);
 J   ALTER TABLE ONLY public.profiles DROP CONSTRAINT profiles_fav_movie_fkey;
       public          postgres    false    218    217    4657            9           2606    24624 !   profiles profiles_fav_series_fkey    FK CONSTRAINT     �   ALTER TABLE ONLY public.profiles
    ADD CONSTRAINT profiles_fav_series_fkey FOREIGN KEY (fav_series) REFERENCES public.content_catalog(id_content);
 K   ALTER TABLE ONLY public.profiles DROP CONSTRAINT profiles_fav_series_fkey;
       public          postgres    false    218    217    4657            :           2606    24614    profiles profiles_id_user_fkey    FK CONSTRAINT     �   ALTER TABLE ONLY public.profiles
    ADD CONSTRAINT profiles_id_user_fkey FOREIGN KEY (id_user) REFERENCES public.users(id_user);
 H   ALTER TABLE ONLY public.profiles DROP CONSTRAINT profiles_id_user_fkey;
       public          postgres    false    218    215    4653            ;           2606    24639    ratings ratings_id_content_fkey    FK CONSTRAINT     �   ALTER TABLE ONLY public.ratings
    ADD CONSTRAINT ratings_id_content_fkey FOREIGN KEY (id_content) REFERENCES public.content_catalog(id_content);
 I   ALTER TABLE ONLY public.ratings DROP CONSTRAINT ratings_id_content_fkey;
       public          postgres    false    217    219    4657            <           2606    24634    ratings ratings_id_user_fkey    FK CONSTRAINT     �   ALTER TABLE ONLY public.ratings
    ADD CONSTRAINT ratings_id_user_fkey FOREIGN KEY (id_user) REFERENCES public.profiles(id_user);
 F   ALTER TABLE ONLY public.ratings DROP CONSTRAINT ratings_id_user_fkey;
       public          postgres    false    4661    219    218           