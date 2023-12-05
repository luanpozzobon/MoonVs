PGDMP                      {            MoonVs    16.0    16.0     �           0    0    ENCODING    ENCODING        SET client_encoding = 'UTF8';
                      false            �           0    0 
   STDSTRINGS 
   STDSTRINGS     (   SET standard_conforming_strings = 'on';
                      false            �           0    0 
   SEARCHPATH 
   SEARCHPATH     8   SELECT pg_catalog.set_config('search_path', '', false);
                      false            �           1262    16396    MoonVs    DATABASE        CREATE DATABASE "MoonVs" WITH TEMPLATE = template0 ENCODING = 'UTF8' LOCALE_PROVIDER = libc LOCALE = 'Portuguese_Brazil.1252';
    DROP DATABASE "MoonVs";
                postgres    false            �           0    0    MoonVs    DATABASE PROPERTIES     .   ALTER DATABASE "MoonVs" CONNECTION LIMIT = 5;
                     postgres    false                        2615    2200    public    SCHEMA        CREATE SCHEMA public;
    DROP SCHEMA public;
                pg_database_owner    false            �           0    0    SCHEMA public    COMMENT     6   COMMENT ON SCHEMA public IS 'standard public schema';
                   pg_database_owner    false    4            �            1259    24577    content_catalog    TABLE     }  CREATE TABLE public.content_catalog (
    id_content integer NOT NULL,
    id_tmdb bigint NOT NULL,
    original_title character varying NOT NULL,
    pt_title character varying NOT NULL,
    is_adult boolean,
    genres character varying[],
    tmdb_vote_avg real,
    tmdb_vote_count bigint,
    content_type character(6) NOT NULL,
    overview text,
    watch_provider jsonb
);
 #   DROP TABLE public.content_catalog;
       public         heap    postgres    false    4            �            1259    24576    content_catalog_id_content_seq    SEQUENCE     �   CREATE SEQUENCE public.content_catalog_id_content_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;
 5   DROP SEQUENCE public.content_catalog_id_content_seq;
       public          postgres    false    217    4            �           0    0    content_catalog_id_content_seq    SEQUENCE OWNED BY     a   ALTER SEQUENCE public.content_catalog_id_content_seq OWNED BY public.content_catalog.id_content;
          public          postgres    false    216            �            1259    16404    users    TABLE       CREATE TABLE public.users (
    id_user uuid NOT NULL,
    username character varying(255),
    email character varying(255) NOT NULL,
    password_hash character varying(60) NOT NULL,
    birth_date date NOT NULL,
    created_at timestamp without time zone NOT NULL
);
    DROP TABLE public.users;
       public         heap    postgres    false    4                       2604    24580    content_catalog id_content    DEFAULT     �   ALTER TABLE ONLY public.content_catalog ALTER COLUMN id_content SET DEFAULT nextval('public.content_catalog_id_content_seq'::regclass);
 I   ALTER TABLE public.content_catalog ALTER COLUMN id_content DROP DEFAULT;
       public          postgres    false    216    217    217            �          0    24577    content_catalog 
   TABLE DATA           �   COPY public.content_catalog (id_content, id_tmdb, original_title, pt_title, is_adult, genres, tmdb_vote_avg, tmdb_vote_count, content_type, overview, watch_provider) FROM stdin;
    public          postgres    false    217   �       �          0    16404    users 
   TABLE DATA           `   COPY public.users (id_user, username, email, password_hash, birth_date, created_at) FROM stdin;
    public          postgres    false    215   �       �           0    0    content_catalog_id_content_seq    SEQUENCE SET     M   SELECT pg_catalog.setval('public.content_catalog_id_content_seq', 11, true);
          public          postgres    false    216            &           2606    24584 .   content_catalog content_catalog_id_content_key 
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
       public            postgres    false    215            �   �  x��WMo7=˿��%I��ؖr)츉���E��hwV˚KnI��M ?��������jm�p���%�]�3�ޛ7����p68%�[u�bd���*��.Km\pu��YP��Y�������p���v����Tɿo()(�ל���x��B$탢�J��S��f�qz���Ӧt�����:n�r6���b����2�v�p�a�;e;��m��Q��|��˟��`(c�!�]��!=]I�թ[m�GT����
u�cVf��.��t�o'�(<����CH�Y��vi��-J6��� H1�흶�i���?��ֲ_�G�HK�t������L�L��P�����6�� ����*�����P���$�,�Z�9^s�9�^W>�k��x�")��!���|�;=�����PRa@[���S�ʨ��ݍ����V`�A��1�Be��T(��۬b/*%%�'p�pX��{O-��L�r�+9A��'��Vm|'��,� 5��B�<���$�1㌋�-�u�T��#am����G�%�IK��ԕDBF��+����寠*Z�{�� �$s��UG����N�zpPb���-���§�O���ܝ�t�*�T>�YUL6u�*����D��QD�����٠ԈS:�����H��v+��Bd��Ѻ�|
%�<�.]�n�J񵕦Ԇ�c��3�Q(��:������J�0 NZ�cI�j�
�1��dw��+��Hs-��{KK�,b۾�)�Xxߖn�Rd��<>G?��4��s4�l�����B�@
P���Ե��ql� =oзh(��Ć.(���D)�$�̡��O��[N��Hh��Z��0[���z��J�=>�d��^��T�'"$�_rg$VPs�`4�y��'�;���q�B���N�zI3�*�L	u������cc������|-ѡYV���K�,s�y$��r�����f��jB| �Ķ��s4��#� �4�朑v��P[t���}d�A�Ɔ� ��kG� Ҙ9eɩ3	}�"�I%�%	, �����yg��wl����{r8�C�����o�n�,(0�i��8�#W�'��{l,<��Xn/2R�>�B��ȉ�8���A6S�9/ű��:/��cԧ�m,������X,Y�7���w �3i�V���W�5u_��k�7�;����Hz�}�K6'�Ҩ�fS�t0�"F�X:����%?:��Ў�\E���SE�����`:;�!��%�)#}Qx�D��5@���
Vg�K��l��ժ[2w`Z.�K�6*�9l�g2ҍSr�J��j\Qy׿�s��P�z��@��N�:��LM�r{;�5��U?�ٴV�q�Űz�m����Ȼmac���q�1R�į�0:��^&�����^J�t12$����d�Z9��Ӕf!N��3#�'�M��H��g����i�/�o�Gj(������Y��׸ŨKӄ���;��`�XL�����Nӑ�Z�����k%!����Z:qY#�%Ҳ{��`o6]��9��#)�"��$oq�(`�l]�*eԌe�k;ιFt-�5o���1%q�A�h��^���^����8����]},\^z��]��
�S�
b@G�QA�\+Еi�?�1<�kh���\�d�����k�D���Zu�~��g:��1��!��ǌ��#����:�"�w�����Ռ�A      �   �   x��M�0 ���^7��#��<�RYA�MEQ�h�����cT�(���kc�nUF���ƙ�qʝ�ͻWo��f�åj�qW�
�m�xTy@��Hn���(��I����E{��>�Mg������SR��:��ʡ�p�ZKٿ6D���`�0��]���72�     