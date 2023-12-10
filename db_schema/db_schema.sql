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
    "someTextData" character varying(200) COLLATE pg_catalog."default",
    CONSTRAINT simple_table_pkey PRIMARY KEY (id)
)

TABLESPACE pg_default;

ALTER TABLE IF EXISTS public.simple_table
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

--------------------------------------------------------

INSERT INTO public.simple_table(
	first_dict_key, second_dict_key, some_text_data)
	VALUES ('PW', 'TW', 'Dane tekstowe');
	
INSERT INTO public.simple_table(
	first_dict_key, second_dict_key, some_text_data)
	VALUES ('DW', 'CW', 'Dane tekstowe 2');