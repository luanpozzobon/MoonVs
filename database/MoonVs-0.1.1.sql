PGDMP                  
    {            MoonVs    16.0    16.0 
    �           0    0    ENCODING    ENCODING        SET client_encoding = 'UTF8';
                      false            �           0    0 
   STDSTRINGS 
   STDSTRINGS     (   SET standard_conforming_strings = 'on';
                      false            �           0    0 
   SEARCHPATH 
   SEARCHPATH     8   SELECT pg_catalog.set_config('search_path', '', false);
                      false            �           1262    16396    MoonVs    DATABASE        CREATE DATABASE "MoonVs" WITH TEMPLATE = template0 ENCODING = 'UTF8' LOCALE_PROVIDER = libc LOCALE = 'Portuguese_Brazil.1252';
    DROP DATABASE "MoonVs";
                postgres    false            �           0    0    MoonVs    DATABASE PROPERTIES     .   ALTER DATABASE "MoonVs" CONNECTION LIMIT = 5;
                     postgres    false            �            1259    16404    users    TABLE       CREATE TABLE public.users (
    id_user uuid NOT NULL,
    username character varying(255),
    email character varying(255) NOT NULL,
    password_hash character varying(60) NOT NULL,
    birth_date date NOT NULL,
    created_at timestamp without time zone NOT NULL
);
    DROP TABLE public.users;
       public         heap    postgres    false            �          0    16404    users 
   TABLE DATA           `   COPY public.users (id_user, username, email, password_hash, birth_date, created_at) FROM stdin;
    public          postgres    false    215   
                  2606    16414    users users_email_key 
   CONSTRAINT     Q   ALTER TABLE ONLY public.users
    ADD CONSTRAINT users_email_key UNIQUE (email);
 ?   ALTER TABLE ONLY public.users DROP CONSTRAINT users_email_key;
       public            postgres    false    215                       2606    16410    users users_pkey 
   CONSTRAINT     S   ALTER TABLE ONLY public.users
    ADD CONSTRAINT users_pkey PRIMARY KEY (id_user);
 :   ALTER TABLE ONLY public.users DROP CONSTRAINT users_pkey;
       public            postgres    false    215                       2606    16412    users users_username_key 
   CONSTRAINT     W   ALTER TABLE ONLY public.users
    ADD CONSTRAINT users_username_key UNIQUE (username);
 B   ALTER TABLE ONLY public.users DROP CONSTRAINT users_username_key;
       public            postgres    false    215            �   �   x��M�0 ���^7��#��<�RYA�MEQ�h�����cT�(���kc�nUF���ƙ�qʝ�ͻWo��f�åj�qW�
�m�xTy@��Hn���(��I����E{��>�Mg������SR��:��ʡ�p�ZKٿ6D���`�0��]���72�     