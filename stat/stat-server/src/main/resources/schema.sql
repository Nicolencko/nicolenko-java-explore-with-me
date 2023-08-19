-- we don't know how to generate root <with-no-name> (class Root) :(
CREATE TABLE IF NOT EXISTS hits
(
    id        serial
        CONSTRAINT pk_hits
            PRIMARY KEY,
    app       varchar(50)              NOT NULL,
    uri       varchar(100)             NOT NULL,
    ip        varchar(20)              NOT NULL,
    timestamp timestamp WITH TIME ZONE NOT NULL
);

ALTER TABLE hits
    OWNER TO postgres;

TRUNCATE hits RESTART IDENTITY;

