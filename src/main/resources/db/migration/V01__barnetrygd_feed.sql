CREATE TABLE barnetrygd_feed
(
    sekvens_id          BIGINT PRIMARY KEY,
    fnr_barn            VARCHAR,
    fnr_stonadsmottaker VARCHAR,
    dato_start_ny_ba    TIMESTAMP(3),
    type                VARCHAR NOT NULL,
    er_duplikat         BOOLEAN      DEFAULT FALSE,
    opprettet_dato      TIMESTAMP(3) DEFAULT NOW()
);

CREATE SEQUENCE barnetrygd_feed_seq INCREMENT BY 1 NO CYCLE;

CREATE INDEX barnetrygd_feed_idx ON barnetrygd_feed (type, fnr_barn);