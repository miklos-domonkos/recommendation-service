CREATE SCHEMA crypto;

CREATE TABLE IF NOT EXISTS crypto.crypto
(
    symbol character varying(255) COLLATE pg_catalog."default" NOT NULL,
    "timestamp" timestamp with time zone NOT NULL,
    price double precision NOT NULL,
    CONSTRAINT crypto_pkey PRIMARY KEY (symbol, "timestamp")
)

TABLESPACE pg_default;
