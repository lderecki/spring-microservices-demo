CREATE TABLE IF NOT EXISTS public.dicts
(
    dict_id character varying(20) COLLATE pg_catalog."default" NOT NULL,
    dict_name character varying(100) COLLATE pg_catalog."default" NOT NULL,
    CONSTRAINT dicts_pkey PRIMARY KEY (dict_id)
)

TABLESPACE pg_default;

ALTER TABLE IF EXISTS public.dicts
    OWNER to postgres;

------------------------------------------------------------------------------------

CREATE TABLE IF NOT EXISTS public.dict_values
(
    dict_id character varying(20) COLLATE pg_catalog."default" NOT NULL,
    dict_key character varying(20) COLLATE pg_catalog."default" NOT NULL,
    dict_value character varying(100) COLLATE pg_catalog."default" NOT NULL,
    disabled boolean NOT NULL DEFAULT false,
    CONSTRAINT dict_values_pkey PRIMARY KEY (dict_id, dict_key),
    CONSTRAINT fk_dict_values_dicts FOREIGN KEY (dict_id)
        REFERENCES public.dicts (dict_id) MATCH SIMPLE
        ON UPDATE NO ACTION
        ON DELETE NO ACTION
        NOT VALID
)

TABLESPACE pg_default;

ALTER TABLE IF EXISTS public.dict_values
    OWNER to postgres;

-------------------------------------------------------------------------------------

CREATE TABLE IF NOT EXISTS public.simple_table
(
    id bigint NOT NULL GENERATED ALWAYS AS IDENTITY ( INCREMENT 1 START 1 MINVALUE 1 MAXVALUE 9223372036854775807 CACHE 1 ),
    first_dict_key character varying(20) COLLATE pg_catalog."default",
    second_dict_key character varying(20) COLLATE pg_catalog."default",
    some_text_data character varying(200) COLLATE pg_catalog."default",
    CONSTRAINT simple_table_pkey PRIMARY KEY (id)
)

TABLESPACE pg_default;

ALTER TABLE IF EXISTS public.simple_table
    OWNER to postgres;

------------------------------------------------------------------------------------

CREATE TABLE IF NOT EXISTS public.app_user
(
    id bigint NOT NULL GENERATED ALWAYS AS IDENTITY ( INCREMENT 1 START 1 MINVALUE 1 MAXVALUE 9223372036854775807 CACHE 1 ),
    username character varying(50) COLLATE pg_catalog."default" NOT NULL,
    password character varying(500) COLLATE pg_catalog."default" NOT NULL,
    CONSTRAINT user_pkey PRIMARY KEY (id),
    CONSTRAINT unique_username UNIQUE (username)
)

TABLESPACE pg_default;

ALTER TABLE IF EXISTS public.app_user
    OWNER to postgres;

------------------------------------------------------------------------------------

CREATE TABLE IF NOT EXISTS public.user_authority
(
    id bigint NOT NULL GENERATED ALWAYS AS IDENTITY ( INCREMENT 1 START 1 MINVALUE 1 MAXVALUE 9223372036854775807 CACHE 1 ),
    authority character varying(50) COLLATE pg_catalog."default" NOT NULL,
    user_id bigint NOT NULL,
    CONSTRAINT user_authority_pkey PRIMARY KEY (user_id),
    CONSTRAINT fk_user_auth_user FOREIGN KEY (user_id)
        REFERENCES public.app_user (id) MATCH SIMPLE
        ON UPDATE NO ACTION
        ON DELETE NO ACTION
        NOT VALID
)

TABLESPACE pg_default;

ALTER TABLE IF EXISTS public.user_authority
    OWNER to postgres;


-- Basic dict values

INSERT INTO public.dicts(
	dict_id, dict_name)
	VALUES ('first_dict', 'Pierwszy słownik');

INSERT INTO public.dicts(
	dict_id, dict_name)
	VALUES ('second_dict', 'Drugi słownik');

---------------------------------------------------------

INSERT INTO public.dict_values(
	dict_id, dict_key, dict_value)
	VALUES ('first_dict', 'PW', 'Pierwsza wartość');
	
INSERT INTO public.dict_values(
	dict_id, dict_key, dict_value)
	VALUES ('first_dict', 'DW', 'Druga wartość');
	
INSERT INTO public.dict_values(
	dict_id, dict_key, dict_value)
	VALUES ('second_dict', 'TW', 'Trzecia wartość');
	
INSERT INTO public.dict_values(
	dict_id, dict_key, dict_value)
	VALUES ('second_dict', 'CW', 'Czwarta wartość');

-- Basic entities

INSERT INTO public.simple_table(
	first_dict_key, second_dict_key, some_text_data)
	VALUES ('PW', 'TW', 'Dane tekstowe');
	
INSERT INTO public.simple_table(
	first_dict_key, second_dict_key, some_text_data)
	VALUES ('DW', 'CW', 'Dane tekstowe 2');

-- Basic users

INSERT INTO public.app_user(
	username, password)
	VALUES ('user', '$2a$04$5XEWygP1D1vp64cFz7QckuhVt3gRgl.5ZUyFJNDjzOaZpstvfVnmq');
	
INSERT INTO public.user_authority(
	authority, user_id)
	VALUES ('USER', (SELECT id
					FROM public.app_user
					WHERE username = 'user'));