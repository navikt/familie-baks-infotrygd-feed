CREATE TABLE feed_logg
(
    logg_id       UUID PRIMARY KEY,
    type          VARCHAR      NOT NULL,
    gjeldende_fnr  VARCHAR      NOT NULL,
    opprettet_dato TIMESTAMP(3) NOT NULL
);

CREATE INDEX feed_logg_idx ON feed_logg (logg_id, type, gjeldende_fnr);