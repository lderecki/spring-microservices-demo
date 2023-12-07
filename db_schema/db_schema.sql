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

-- Basic dict values

INSERT INTO public.dicts(
	dict_id, dict_name)
	VALUES ('first_dict', 'Pierwszy słownik');

INSERT INTO public.dict_values(
	dict_id, dict_key, dict_value)
	VALUES ('first_dict', 'FV', 'First value');
	
INSERT INTO public.dict_values(
	dict_id, dict_key, dict_value)
	VALUES ('first_dict', 'SV', 'Second value');