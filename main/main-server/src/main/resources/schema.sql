DROP TABLE IF EXISTS users CASCADE;
DROP TABLE IF EXISTS categories CASCADE;
DROP TABLE IF EXISTS locations CASCADE;
DROP TABLE IF EXISTS events CASCADE;
DROP TABLE IF EXISTS requests CASCADE;
DROP TABLE IF EXISTS compilations CASCADE;
DROP TABLE IF EXISTS compilation_event CASCADE;

CREATE TABLE users
(
    id    BIGINT GENERATED BY DEFAULT AS IDENTITY NOT NULL,
    name  VARCHAR(255)                            NOT NULL,
    email VARCHAR(512)                            NOT NULL,
    CONSTRAINT pk_user PRIMARY KEY (id),
    CONSTRAINT UQ_USER_EMAIL UNIQUE (email)
);

CREATE TABLE categories
(
    id    BIGINT GENERATED BY DEFAULT AS IDENTITY NOT NULL,
    name  VARCHAR(255)                            NOT NULL,
    CONSTRAINT pk_category PRIMARY KEY (id),
    CONSTRAINT UQ_CATEGORIES_NAME UNIQUE (name)
);

CREATE TABLE locations
(
    id    BIGINT GENERATED BY DEFAULT AS IDENTITY NOT NULL,
    lat FLOAT                                     ,
    lon FLOAT                                     ,
    CONSTRAINT pk_location PRIMARY KEY (id)
);

CREATE TABLE events
(
    id    BIGINT GENERATED BY DEFAULT AS IDENTITY NOT NULL,
    annotation VARCHAR(2000)                      NOT NULL,
    category_id BIGINT,
    confirmed_requests BIGINT,
    created_on TIMESTAMP    WITHOUT TIME ZONE,
    description VARCHAR(7000)                     NOT NULL,
    event_date TIMESTAMP    WITHOUT TIME ZONE,
    initiator_id BIGINT,
    location_id BIGINT,
    paid BOOLEAN            DEFAULT FALSE         NOT NULL,
    participant_limit BIGINT DEFAULT 0,
    published_on TIMESTAMP    WITHOUT TIME ZONE,
    request_moderation BOOLEAN DEFAULT FALSE,
    state VARCHAR(20),
    title VARCHAR(120)                      NOT NULL,
    CONSTRAINT pk_event PRIMARY KEY (id),
    CONSTRAINT fk_event_category_id FOREIGN KEY (category_id) REFERENCES categories (id),
    CONSTRAINT fk_event_initiator_id FOREIGN KEY (initiator_id) REFERENCES users (id),
    CONSTRAINT fk_event_location_id FOREIGN KEY (location_id) REFERENCES locations (id)
);

CREATE TABLE requests
(
    id    BIGINT GENERATED BY DEFAULT AS IDENTITY NOT NULL,
    created TIMESTAMP WITHOUT TIME ZONE,
    event_id BIGINT,
    requester_id BIGINT,
    status VARCHAR(20),
    CONSTRAINT pk_request PRIMARY KEY (id),
    CONSTRAINT fk_request_event_id FOREIGN KEY (event_id) REFERENCES events (id),
    CONSTRAINT fk_request_requester_id FOREIGN KEY (requester_id) REFERENCES users (id)
);

CREATE TABLE compilations
(
    id    BIGINT GENERATED BY DEFAULT AS IDENTITY NOT NULL,
    pinned BOOLEAN DEFAULT FALSE,
    title VARCHAR(50),
    CONSTRAINT pk_compilation PRIMARY KEY (id)
);

CREATE TABLE compilation_event
(
    compilation_id BIGINT NOT NULL,
    event_id BIGINT NOT NULL,
    CONSTRAINT fr_compilation_id FOREIGN KEY (compilation_id) REFERENCES compilations (id),
    CONSTRAINT fr_event_id FOREIGN KEY (event_id) REFERENCES events (id)
);

CREATE UNIQUE INDEX locations_coordinates
ON locations (lat, lon);