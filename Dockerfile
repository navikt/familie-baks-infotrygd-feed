FROM ghcr.io/navikt/baseimages/temurin:17

COPY ./target/familie-baks-infotrygd-feed.jar "app.jar"
