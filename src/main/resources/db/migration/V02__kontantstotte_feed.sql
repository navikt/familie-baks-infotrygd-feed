CREATE TABLE kontantstotte_feed
(
    sekvens_id          BIGINT PRIMARY KEY,
    fnr_stonadsmottaker VARCHAR,
    dato_start_ny_ks    TIMESTAMP(3),
    type                VARCHAR NOT NULL,
    opprettet_dato      TIMESTAMP(3) DEFAULT NOW()
);

CREATE SEQUENCE kontantstotte_feed_seq INCREMENT BY 1 NO CYCLE;

CREATE INDEX kontantstotte_feed_idx ON kontantstotte_feed (type, fnr_stonadsmottaker);